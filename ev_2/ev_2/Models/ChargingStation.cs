using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace EV_2.Models
{
    public class ChargingStation
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        [BsonElement("Name")]
        public string Name { get; set; }

        [BsonElement("Location")]
        public string Location { get; set; }

        [BsonElement("Type")]
        public string Type { get; set; } // AC / DC

        [BsonElement("TotalSlots")]
        public int TotalSlots { get; set; }

        [BsonElement("AvailableSlots")]
        public int AvailableSlots { get; set; }

        [BsonElement("Status")]
        public string Status { get; set; } // Active / Deactivated

        [BsonElement("Schedule")]
        public List<DateTime> Schedule { get; set; } = new List<DateTime>();
        [BsonElement("Latitude")]
        public double Latitude { get; set; } = 0.0;

        [BsonElement("Longitude")]
        public double Longitude { get; set; } = 0.0;


    }
}
