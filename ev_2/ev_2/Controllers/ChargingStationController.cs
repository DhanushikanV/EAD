using EV_2.Models;
using EV_2.Services;
using Microsoft.AspNetCore.Mvc;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ChargingStationController : ControllerBase
    {
        private readonly ChargingStationService _stationService;

        public ChargingStationController(ChargingStationService stationService)
        {
            _stationService = stationService;
        }

        [HttpGet]
        public async Task<ActionResult<List<ChargingStation>>> GetAll()
        {
            var stations = await _stationService.GetAllAsync();
            return Ok(stations);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ChargingStation>> GetById(string id)
        {
            var station = await _stationService.GetByIdAsync(id);
            if (station == null) return NotFound();
            return Ok(station);
        }

        [HttpPost]
        public async Task<ActionResult> Create(ChargingStation station)
        {
            await _stationService.CreateAsync(station);
            return CreatedAtAction(nameof(GetById), new { id = station.Id }, station);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Update(string id, ChargingStation updatedStation)
        {
            var existing = await _stationService.GetByIdAsync(id);
            if (existing == null) return NotFound();

            updatedStation.Id = existing.Id; // Keep the same ObjectId
            await _stationService.UpdateAsync(id, updatedStation);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(string id)
        {
            var existing = await _stationService.GetByIdAsync(id);
            if (existing == null) return NotFound();

            await _stationService.DeleteAsync(id);
            return NoContent();
        }
    }
}
