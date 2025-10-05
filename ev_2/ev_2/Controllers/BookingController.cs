using Microsoft.AspNetCore.Mvc;
using EV_2.Models;
using EV_2.Services;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class BookingController : ControllerBase
    {
        private readonly BookingService _bookingService;

        public BookingController(BookingService bookingService)
        {
            _bookingService = bookingService;
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
            await _bookingService.CreateAsync(newBooking);
            return CreatedAtAction(nameof(GetById), new { id = newBooking.Id }, newBooking);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, Booking updatedBooking)
        {
            var existing = await _bookingService.GetAsync(id);
            if (existing is null) return NotFound();
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
    }
}
