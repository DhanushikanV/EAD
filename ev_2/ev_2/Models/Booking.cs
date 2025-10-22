using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
//
namespace EV_2.Models 
{
    public class Booking
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }  // MongoDB ObjectId, nullable

        [BsonElement("EVOwnerNIC")]
        public string EVOwnerNIC { get; set; } = null!; // Reference to EVOwner

        [BsonElement("StationId")]
        public string StationId { get; set; } = null!; // Reference to ChargingStation

        [BsonElement("ReservationDateTime")]
        public DateTime ReservationDateTime { get; set; }

        [BsonElement("Status")]
        public string Status { get; set; } = "Pending"; // Pending | Confirmed | InProgress | Completed | Cancelled

        [BsonElement("CreatedAt")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [BsonElement("UpdatedAt")]
        public DateTime? UpdatedAt { get; set; } = null;

        [BsonElement("qrToken")]
        public string? QrToken { get; set; } = null; // Generated when Confirmed

        [BsonElement("validatedAt")]
        public DateTime? ValidatedAt { get; set; } = null; // First successful QR validation

        [BsonElement("StartedAt")]
        public DateTime? StartedAt { get; set; } = null;

        [BsonElement("CompletedAt")]
        public DateTime? CompletedAt { get; set; } = null;
    }
}
