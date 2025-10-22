using Microsoft.AspNetCore.Mvc;
using EV_2.Models;
using EV_2.Services;
using System.Security.Cryptography;
using MongoDB.Driver;
//test
namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class BookingController : ControllerBase
    {
        private readonly BookingService _bookingService;
        private readonly StationSlotService _stationSlotService;
        private readonly IMongoClient _mongoClient;

        public BookingController(BookingService bookingService, StationSlotService stationSlotService, IMongoClient mongoClient)
        {
            _bookingService = bookingService;
            _stationSlotService = stationSlotService;
            _mongoClient = mongoClient;
        }

        [HttpGet]
        public async Task<IActionResult> Get([FromQuery] string? nic = null)
        {
            if (!string.IsNullOrEmpty(nic))
            {
                // Get bookings for specific user
                var userBookings = await _bookingService.GetByEvOwnerNICAsync(nic);
                return Ok(userBookings);
            }
            else
            {
                // Get all bookings
                return Ok(await _bookingService.GetAsync());
            }
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(string id)
        {
            var booking = await _bookingService.GetAsync(id);
            return booking is null ? NotFound() : Ok(booking);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Booking newBooking)
        {
            // Business: within 7 days
            var now = DateTime.UtcNow;
            if (!BookingService.IsWithinSevenDays(now, newBooking.ReservationDateTime))
                return BadRequest(new { error = "Date must be within 7 days" });

            newBooking.Status = "Pending";
            newBooking.CreatedAt = now;
            newBooking.UpdatedAt = now;
            await _bookingService.CreateAsync(newBooking);
            return CreatedAtAction(nameof(GetById), new { id = newBooking.Id }, new { bookingId = newBooking.Id, status = newBooking.Status });
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, Booking updatedBooking)
        {
            var existing = await _bookingService.GetAsync(id);
            if (existing is null) return NotFound();
            // Only allow update if >=12h before reservation
            var now = DateTime.UtcNow;
            if (!BookingService.CanModifyOrCancel(now, existing.ReservationDateTime))
                return BadRequest(new { error = "Operation not allowed within 12 hours of start time" });

            // Keep immutable fields
            updatedBooking.Id = existing.Id;
            updatedBooking.Status = existing.Status;
            updatedBooking.QrToken = existing.QrToken;
            updatedBooking.ValidatedAt = existing.ValidatedAt;
            updatedBooking.CreatedAt = existing.CreatedAt;
            updatedBooking.UpdatedAt = now;
            await _bookingService.UpdateAsync(id, updatedBooking);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var existing = await _bookingService.GetAsync(id);
            if (existing is null) return NotFound();
            await _bookingService.DeleteAsync(id);
            return NoContent();
        }

        // ---------------- Custom Actions ----------------

        [HttpPut("{id}/confirm")]
        public async Task<IActionResult> Confirm(string id)
        {
            try
            {
                var booking = await _bookingService.GetAsync(id);
                if (booking is null) return NotFound();

                if (booking.Status != "Pending")
                    return BadRequest(new { error = "Only Pending bookings can be confirmed" });

                // 7-day rule (already enforced on create, but safe to re-check)
                if (!BookingService.IsWithinSevenDays(DateTime.UtcNow, booking.ReservationDateTime))
                    return BadRequest(new { error = "Reservation must be within 7 days" });

                // Check and decrement station slots
                var chargingStationService = HttpContext.RequestServices.GetRequiredService<ChargingStationService>();
                var station = await chargingStationService.GetByIdAsync(booking.StationId);
                
                if (station == null)
                    return BadRequest(new { error = "Station not found" });
                
                if (station.AvailableSlots <= 0)
                    return BadRequest(new { error = "No available slots for this time" });

                // Decrement available slots
                station.AvailableSlots -= 1;
                await chargingStationService.UpdateAsync(booking.StationId, station);

                // Generate unique qrToken (128-bit random -> base64url w/o padding)
                string qrToken;
                using (var rng = RandomNumberGenerator.Create())
                {
                    var bytes = new byte[16];
                    rng.GetBytes(bytes);
                    qrToken = Convert.ToBase64String(bytes)
                        .Replace('+', '-')
                        .Replace('/', '_')
                        .TrimEnd('=');
                }

                // Update booking status to Confirmed
                await _bookingService.TrySetStatusAsync(id, "Confirmed", b =>
                {
                    b.QrToken = qrToken;
                });

                return Ok(new { bookingId = booking.Id, status = "Confirmed", qrToken });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpPut("{id}/cancel")]
        public async Task<IActionResult> Cancel(string id)
        {
            try
            {
                var booking = await _bookingService.GetAsync(id);
                if (booking is null) return NotFound();

                var now = DateTime.UtcNow;
                if (!BookingService.CanModifyOrCancel(now, booking.ReservationDateTime))
                    return BadRequest(new { error = "Cannot cancel within 12 hours of start" });

                // If booking was Confirmed, increment available slots back
                if (booking.Status == "Confirmed")
                {
                    var chargingStationService = HttpContext.RequestServices.GetRequiredService<ChargingStationService>();
                    var station = await chargingStationService.GetByIdAsync(booking.StationId);
                    if (station != null)
                    {
                        station.AvailableSlots += 1;
                        await chargingStationService.UpdateAsync(booking.StationId, station);
                    }
                }

                // Update booking status to Cancelled
                await _bookingService.TrySetStatusAsync(id, "Cancelled", b =>
                {
                    b.QrToken = null;
                    b.ValidatedAt = null;
                });

                return Ok(new { bookingId = booking.Id, status = "Cancelled" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        public class ScanRequest { public string qrToken { get; set; } = null!; }
        [HttpPost("operator/scan")]
        public async Task<IActionResult> Scan([FromBody] ScanRequest req)
        {
            if (string.IsNullOrWhiteSpace(req.qrToken)) return BadRequest(new { error = "qrToken is required" });
            var booking = await _bookingService.GetByQrTokenAsync(req.qrToken);
            if (booking is null) return NotFound(new { error = "Invalid or expired QR token" });
            if (booking.Status != "Confirmed") return BadRequest(new { error = "Only Confirmed bookings can be scanned" });
            if (booking.ValidatedAt.HasValue) return BadRequest(new { error = "QR token already used" });

            booking.ValidatedAt = DateTime.UtcNow;
            booking.UpdatedAt = DateTime.UtcNow;
            await _bookingService.UpdateAsync(booking.Id!, booking);

            return Ok(new { message = "Valid booking", bookingId = booking.Id, nic = booking.EVOwnerNIC, stationId = booking.StationId, status = booking.Status });
        }

        [HttpPost("operator/bookings/{id}/start")]
        public async Task<IActionResult> Start(string id)
        {
            var booking = await _bookingService.GetAsync(id);
            if (booking is null) return NotFound();
            if (booking.Status != "Confirmed") return BadRequest(new { error = "Only Confirmed bookings can be started" });
            if (!booking.ValidatedAt.HasValue) return BadRequest(new { error = "Scan required before start" });
            // Ensure token single-use: after scan or at start
            await _bookingService.TrySetStatusAsync(id, "InProgress", b =>
            {
                b.QrToken = null; // consume token
                if (!b.ValidatedAt.HasValue) b.ValidatedAt = DateTime.UtcNow;
                b.StartedAt = DateTime.UtcNow;
            });
            return NoContent();
        }

        [HttpPut("{id}/reset")]
        public async Task<IActionResult> Reset(string id)
        {
            try
            {
                var booking = await _bookingService.GetAsync(id);
                if (booking is null) return NotFound();

                // If booking was Confirmed, increment available slots back
                if (booking.Status == "Confirmed")
                {
                    await _stationSlotService.IncrementAvailableSlotsAsync(
                        booking.StationId, booking.ReservationDateTime);
                }

                // Update booking status back to Pending
                await _bookingService.TrySetStatusAsync(id, "Pending", b =>
                {
                    b.QrToken = null;
                    b.ValidatedAt = null;
                    b.StartedAt = null;
                    b.CompletedAt = null;
                });

                return Ok(new { bookingId = booking.Id, status = "Pending" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("slots/{stationId}/availability")]
        public async Task<IActionResult> GetSlotAvailability(string stationId, [FromQuery] DateTime? date = null)
        {
            var targetDate = date ?? DateTime.UtcNow.Date;
            var availableSlots = await _stationSlotService.GetAvailableSlotsAsync(stationId, targetDate);
            
            return Ok(new { 
                stationId, 
                date = targetDate, 
                availableSlots 
            });
        }

        [HttpGet("debug/slot/{stationId}/{dateTime}")]
        public async Task<IActionResult> DebugSlot(string stationId, DateTime dateTime)
        {
            var slotId = StationSlot.GenerateSlotId(stationId, dateTime);
            var availableSlots = await _stationSlotService.GetAvailableSlotsAsync(stationId, dateTime);
            
            return Ok(new { 
                stationId, 
                dateTime,
                slotId,
                availableSlots 
            });
        }

        [HttpPost("debug/decrement/{stationId}/{dateTime}")]
        public async Task<IActionResult> DebugDecrement(string stationId, DateTime dateTime)
        {
            try
            {
                var slotId = StationSlot.GenerateSlotId(stationId, dateTime);
                var availableSlotsBefore = await _stationSlotService.GetAvailableSlotsAsync(stationId, dateTime);
                
                var result = await _stationSlotService.TryDecrementAvailableSlotsAsync(stationId, dateTime);
                var availableSlotsAfter = await _stationSlotService.GetAvailableSlotsAsync(stationId, dateTime);
                
                return Ok(new { 
                    stationId, 
                    dateTime,
                    slotId,
                    availableSlotsBefore,
                    availableSlotsAfter,
                    decrementSuccess = result
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message, stackTrace = ex.StackTrace });
            }
        }

        public class FinalizeBody { public string? notes { get; set; } }
        [HttpPost("operator/bookings/{id}/finalize")]
        public async Task<IActionResult> Finalize(string id, [FromBody] FinalizeBody _)
        {
            var booking = await _bookingService.GetAsync(id);
            if (booking is null) return NotFound();
            if (booking.Status != "InProgress") return BadRequest(new { error = "Only InProgress bookings can be finalized" });
            await _bookingService.TrySetStatusAsync(id, "Completed", b => { b.CompletedAt = DateTime.UtcNow; });
            return NoContent();
        }
    }
}
