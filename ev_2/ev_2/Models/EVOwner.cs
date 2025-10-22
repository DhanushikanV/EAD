using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace EV_2.Models
{
    public class EVOwner
    {
        [BsonId] // Primary key
        [BsonRepresentation(BsonType.String)]
        public string NIC { get; set; } = null!;

        [BsonElement("Name")]
        public string Name { get; set; } = null!;

        [BsonElement("Email")]
        public string Email { get; set; } = null!;

        [BsonElement("Phone")]
        public string Phone { get; set; } = null!;

        [BsonElement("PasswordHash")]
        public string PasswordHash { get; set; } = null!; // Store hashed password

        [BsonElement("Status")]
        public string Status { get; set; } = "Active"; // Active / Deactivated

        [BsonElement("CreatedAt")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [BsonElement("EVModels")]
        public List<string> EVModels { get; set; } = new List<string>(); // Multiple vehicles
    }
}
