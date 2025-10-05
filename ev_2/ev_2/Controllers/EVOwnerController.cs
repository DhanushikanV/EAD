using EV_2.Models;
using EV_2.Services;
using Microsoft.AspNetCore.Mvc;
using BCrypt.Net;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class EVOwnerController : ControllerBase
    {
        private readonly EVOwnerService _evoOwnerService;
        private readonly JwtService _jwtService;

        public EVOwnerController(EVOwnerService evoOwnerService, JwtService jwtService)
        {
            _evoOwnerService = evoOwnerService;
            _jwtService = jwtService;
        }

        [HttpGet]
        public async Task<IActionResult> Get() => Ok(await _evoOwnerService.GetAsync());

        [HttpGet("{nic}")]
        public async Task<IActionResult> Get(string nic)
        {
            var evoOwner = await _evoOwnerService.GetAsync(nic);
            return evoOwner is null ? NotFound() : Ok(evoOwner);
        }

        // Login endpoint for EVOwner users
        public class EVOwnerLoginRequest
        {
            public string Email { get; set; } = null!;
            public string Password { get; set; } = null!;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] EVOwnerLoginRequest request)
        {
            if (request == null || string.IsNullOrEmpty(request.Email) || string.IsNullOrEmpty(request.Password))
                return BadRequest(new { message = "Email and password are required" });

            // Find EVOwner by email
            var evOwner = await _evoOwnerService.GetByEmailAsync(request.Email);
            if (evOwner == null)
                return Unauthorized(new { message = "User not found" });

            // Check password
            if (!BCrypt.Net.BCrypt.Verify(request.Password, evOwner.PasswordHash))
                return Unauthorized(new { message = "Invalid password" });

            // Generate JWT
            var token = _jwtService.GenerateTokenForEVOwner(evOwner);

            // Return token and user info
            return Ok(new { 
                token,
                user = new {
                    id = evOwner.NIC,
                    nic = evOwner.NIC,
                    name = evOwner.Name,
                    email = evOwner.Email,
                    phone = evOwner.Phone,
                    role = "EVOwner",
                    status = evOwner.Status
                }
            });
        }

        [HttpPost]
        public async Task<IActionResult> Create(EVOwner newOwner)
        {
            // Check if user with this NIC already exists
            var existingOwner = await _evoOwnerService.GetAsync(newOwner.NIC);
            if (existingOwner != null)
            {
                return Conflict(new { 
                    message = "User with this NIC already exists",
                    nic = newOwner.NIC,
                    existingEmail = existingOwner.Email
                });
            }

            // Check if user with this email already exists
            var existingByEmail = await _evoOwnerService.GetByEmailAsync(newOwner.Email);
            if (existingByEmail != null)
            {
                return Conflict(new { 
                    message = "User with this email already exists",
                    email = newOwner.Email
                });
            }

            try
            {
                // Hash password before saving (expecting plain text password in PasswordHash field)
                newOwner.PasswordHash = BCrypt.Net.BCrypt.HashPassword(newOwner.PasswordHash);
                
                await _evoOwnerService.CreateAsync(newOwner);
                return CreatedAtAction(nameof(Get), new { nic = newOwner.NIC }, newOwner);
            }
            catch (Exception ex)
            {
                return BadRequest(new { 
                    message = "Failed to create user: " + ex.Message,
                    error = ex.GetType().Name
                });
            }
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
