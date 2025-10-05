namespace EV_2.Settings
{
    public class DatabaseSettings
    {
        public string ConnectionString { get; set; } = null!;
        public string DatabaseName { get; set; } = null!;
        public string BookingCollectionName { get; set; } = "Bookings";
        public string ChargingStationCollectionName { get; set; } = "ChargingStations";
        public string EVOwnerCollectionName { get; set; } = "EVOwners";
        public string UserCollectionName { get; set; } = "Users";
        public string StationSlotsCollectionName { get; set; } = "StationSlots";
    }
}
