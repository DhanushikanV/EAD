using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace EV_2.Models
{
    public class User
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; } = null!;

        [BsonElement("Username")]
        public string Username { get; set; } = null!;

        [BsonElement("Email")]
        public string Email { get; set; } = null!;  // Add email

        [BsonElement("PasswordHash")]
        public string PasswordHash { get; set; } = null!;
        
        // Temporary field for signup (will be hashed into PasswordHash)
        public string? Password { get; set; }

        [BsonElement("Role")]
        public string Role { get; set; } = "Backoffice";

        [BsonElement("Status")]
        public string Status { get; set; } = "Active"; // Active / Inactive

        [BsonElement("CreatedAt")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
