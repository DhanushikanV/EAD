using EV_2.Models;
using EV_2.Services;
using Microsoft.AspNetCore.Mvc;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class EVOwnerController : ControllerBase
    {
        private readonly EVOwnerService _evoOwnerService;

        public EVOwnerController(EVOwnerService evoOwnerService)
        {
            _evoOwnerService = evoOwnerService;
        }

        [HttpGet]
        public async Task<IActionResult> Get() => Ok(await _evoOwnerService.GetAsync());

        [HttpGet("{nic}")]
        public async Task<IActionResult> Get(string nic)
        {
            var evoOwner = await _evoOwnerService.GetAsync(nic);
            return evoOwner is null ? NotFound() : Ok(evoOwner);
        }

        [HttpPost]
        public async Task<IActionResult> Create(EVOwner newOwner)
        {
            await _evoOwnerService.CreateAsync(newOwner);
            return CreatedAtAction(nameof(Get), new { nic = newOwner.NIC }, newOwner);
        }

        [HttpPut("{nic}")]
        public async Task<IActionResult> Update(string nic, EVOwner updatedOwner)
        {
            var existing = await _evoOwnerService.GetAsync(nic);
            if (existing is null) return NotFound();

            await _evoOwnerService.UpdateAsync(nic, updatedOwner);
            return NoContent();
        }

        [HttpDelete("{nic}")]
        public async Task<IActionResult> Delete(string nic)
        {
            var existing = await _evoOwnerService.GetAsync(nic);
            if (existing is null) return NotFound();

            await _evoOwnerService.DeleteAsync(nic);
            return NoContent();
        }
    }
}
