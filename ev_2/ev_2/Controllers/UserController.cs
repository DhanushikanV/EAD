using EV_2.Models;
using EV_2.Services;
using Microsoft.AspNetCore.Mvc;
using BCrypt.Net;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly UserService _userService;
        private readonly JwtService _jwtService;

        public UserController(UserService userService, JwtService jwtService)
        {
            _userService = userService;
            _jwtService = jwtService;
        }

        // ------------------ LOGIN ------------------
        public class LoginRequest
        {
            public string Email { get; set; } = null!;
            public string Password { get; set; } = null!;
        }
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            if (request == null || string.IsNullOrEmpty(request.Email) || string.IsNullOrEmpty(request.Password))
                return BadRequest(new { message = "Email and password are required" });

            // Find user by email
            var user = await _userService.GetByEmailAsync(request.Email);
            if (user == null)
                return Unauthorized(new { message = "User not found" });

            // Check password
            if (!BCrypt.Net.BCrypt.Verify(request.Password, user.PasswordHash))
                return Unauthorized(new { message = "Invalid password" });

            // Generate JWT
            var token = _jwtService.GenerateToken(user);

            return Ok(new { token });
        }


        // ------------------ CRUD ------------------
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var users = await _userService.GetAsync();
            return Ok(users);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(string id)
        {
            var user = await _userService.GetAsync(id);
            if (user is null) return NotFound();
            return Ok(user);
        }

        [HttpPost]
        public async Task<IActionResult> Create(User newUser)
        {
            // Hash password before saving
            newUser.PasswordHash = BCrypt.Net.BCrypt.HashPassword(newUser.PasswordHash);
            await _userService.CreateAsync(newUser);
            return CreatedAtAction(nameof(Get), new { id = newUser.Id }, newUser);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, [FromBody] UpdateUserDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var existingUser = await _userService.GetAsync(id);
            if (existingUser == null) return NotFound();

            if (!string.IsNullOrWhiteSpace(dto.Password))
                existingUser.PasswordHash = BCrypt.Net.BCrypt.HashPassword(dto.Password);

            existingUser.Role = dto.Role ?? existingUser.Role;
            existingUser.Status = dto.Status ?? existingUser.Status;
            existingUser.Email = dto.Email ?? existingUser.Email;
            existingUser.Username = dto.Username ?? existingUser.Username;

            await _userService.UpdateAsync(id, existingUser);
            return NoContent();
        }








        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var existingUser = await _userService.GetAsync(id);
            if (existingUser is null) return NotFound();

            await _userService.DeleteAsync(id);
            return NoContent();
        }
    }
}
