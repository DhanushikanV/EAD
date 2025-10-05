using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace EV_2.Models
{
    public class StationSlot
    {
        [BsonId]
        public string Id { get; set; } = null!; // Format: "{StationId}#{YYYY-MM-DDTHH:mm}:00Z"

        [BsonElement("StationId")]
        public string StationId { get; set; } = null!;

        [BsonElement("SlotStart")]
        public DateTime SlotStart { get; set; }

        [BsonElement("Capacity")]
        public int Capacity { get; set; }

        [BsonElement("Available")]
        public int Available { get; set; }

        [BsonElement("UpdatedAt")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        // Helper method to generate slot ID
        public static string GenerateSlotId(string stationId, DateTime reservationDateTime)
        {
            // Round to the nearest hour for slot management
            var slotStart = new DateTime(reservationDateTime.Year, reservationDateTime.Month, 
                                       reservationDateTime.Day, reservationDateTime.Hour, 0, 0, DateTimeKind.Utc);
            return $"{stationId}#{slotStart:yyyy-MM-ddTHH:mm}:00Z";
        }
    }
}
