namespace EV_2.Models
{
    public class UpdateUserDto
    {
        public string? Username { get; set; }  // optional
        public string? Email { get; set; }     // optional
        public string? Role { get; set; }      // optional
        public string? Status { get; set; }    // optional
        public string? Password { get; set; }  // optional, plain text
    }
}
