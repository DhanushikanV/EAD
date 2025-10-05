using EV_2.Models;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EV_2.Services
{
    public class StationSlotService
    {
        private readonly IMongoCollection<StationSlot> _stationSlotsCollection;
        private readonly IMongoCollection<ChargingStation> _stationsCollection;

        public StationSlotService(IMongoClient mongoClient, IOptions<DatabaseSettings> options)
        {
            var settings = options.Value;
            var database = mongoClient.GetDatabase(settings.DatabaseName);
            _stationSlotsCollection = database.GetCollection<StationSlot>(settings.StationSlotsCollectionName);
            _stationsCollection = database.GetCollection<ChargingStation>(settings.ChargingStationCollectionName);

            // Ensure indexes (idempotent)
            var indexModels = new List<CreateIndexModel<StationSlot>>
            {
                new CreateIndexModel<StationSlot>(
                    Builders<StationSlot>.IndexKeys.Ascending(s => s.Id),
                    new CreateIndexOptions { Name = "idx_slot_id" }),
                new CreateIndexModel<StationSlot>(
                    Builders<StationSlot>.IndexKeys.Ascending(s => s.Available),
                    new CreateIndexOptions { Name = "idx_available" })
            };
            _stationSlotsCollection.Indexes.CreateMany(indexModels);
        }

        /// <summary>
        /// Atomically decrement available slots for a station at a specific time
        /// Returns true if successful, false if no slots available
        /// </summary>
        public async Task<bool> TryDecrementAvailableSlotsAsync(string stationId, DateTime reservationDateTime)
        {
            var slotId = StationSlot.GenerateSlotId(stationId, reservationDateTime);
            
            // Get station capacity or use default
            var station = await _stationsCollection
                .Find(s => s.Id == stationId)
                .FirstOrDefaultAsync();
            
            var capacity = station?.AvailableSlots ?? 10; // Default capacity

            // First, try to decrement if slot exists and has available slots
            var existingFilter = Builders<StationSlot>.Filter.And(
                Builders<StationSlot>.Filter.Eq(s => s.Id, slotId),
                Builders<StationSlot>.Filter.Gt(s => s.Available, 0)
            );
            
            var decrementUpdate = Builders<StationSlot>.Update
                .Inc(s => s.Available, -1)
                .Set(s => s.UpdatedAt, DateTime.UtcNow);

            var result = await _stationSlotsCollection.UpdateOneAsync(existingFilter, decrementUpdate);
            
            if (result.ModifiedCount > 0)
            {
                return true; // Successfully decremented existing slot
            }

            // If no existing slot was modified, try to create a new slot with capacity-1
            var createFilter = Builders<StationSlot>.Filter.Eq(s => s.Id, slotId);
            var createUpdate = Builders<StationSlot>.Update
                .SetOnInsert(s => s.StationId, stationId)
                .SetOnInsert(s => s.SlotStart, new DateTime(reservationDateTime.Year, reservationDateTime.Month, 
                                                           reservationDateTime.Day, reservationDateTime.Hour, 0, 0, DateTimeKind.Utc))
                .SetOnInsert(s => s.Capacity, capacity)
                .SetOnInsert(s => s.Available, capacity - 1)
                .SetOnInsert(s => s.UpdatedAt, DateTime.UtcNow);

            try
            {
                var createOptions = new UpdateOptions { IsUpsert = true };
                var createResult = await _stationSlotsCollection.UpdateOneAsync(createFilter, createUpdate, createOptions);
                
                return createResult.UpsertedId != null;
            }
            catch (MongoWriteException ex) when (ex.WriteError?.Category == ServerErrorCategory.DuplicateKey)
            {
                // Slot was created by another request, try to decrement it
                var retryResult = await _stationSlotsCollection.UpdateOneAsync(existingFilter, decrementUpdate);
                return retryResult.ModifiedCount > 0;
            }
        }

        /// <summary>
        /// Atomically increment available slots for a station at a specific time
        /// </summary>
        public async Task IncrementAvailableSlotsAsync(string stationId, DateTime reservationDateTime)
        {
            var slotId = StationSlot.GenerateSlotId(stationId, reservationDateTime);
            
            var filter = Builders<StationSlot>.Filter.Eq(s => s.Id, slotId);
            var update = Builders<StationSlot>.Update
                .Inc(s => s.Available, 1)
                .Set(s => s.UpdatedAt, DateTime.UtcNow);

            await _stationSlotsCollection.UpdateOneAsync(filter, update);
        }

        /// <summary>
        /// Get available slots for a station at a specific time
        /// </summary>
        public async Task<int> GetAvailableSlotsAsync(string stationId, DateTime reservationDateTime)
        {
            var slotId = StationSlot.GenerateSlotId(stationId, reservationDateTime);
            
            var slot = await _stationSlotsCollection
                .Find(s => s.Id == slotId)
                .FirstOrDefaultAsync();

            if (slot == null)
            {
                // Get station capacity or use default
                var station = await _stationsCollection
                    .Find(s => s.Id == stationId)
                    .FirstOrDefaultAsync();
                
                var capacity = station?.AvailableSlots ?? 10; // Default capacity
                
                // Create the slot if it doesn't exist
                var newSlot = new StationSlot
                {
                    Id = slotId,
                    StationId = stationId,
                    SlotStart = new DateTime(reservationDateTime.Year, reservationDateTime.Month, 
                                           reservationDateTime.Day, reservationDateTime.Hour, 0, 0, DateTimeKind.Utc),
                    Capacity = capacity,
                    Available = capacity,
                    UpdatedAt = DateTime.UtcNow
                };
                
                try
                {
                    await _stationSlotsCollection.InsertOneAsync(newSlot);
                    return capacity;
                }
                catch (MongoWriteException ex) when (ex.WriteError?.Category == ServerErrorCategory.DuplicateKey)
                {
                    // Slot was created by another request, fetch it
                    slot = await _stationSlotsCollection
                        .Find(s => s.Id == slotId)
                        .FirstOrDefaultAsync();
                    return slot?.Available ?? capacity;
                }
            }

            return slot.Available;
        }

        /// <summary>
        /// Get all slots for a station within a date range
        /// </summary>
        public async Task<List<StationSlot>> GetStationSlotsAsync(string stationId, DateTime startDate, DateTime endDate)
        {
            var startSlotId = StationSlot.GenerateSlotId(stationId, startDate);
            var endSlotId = StationSlot.GenerateSlotId(stationId, endDate);

            return await _stationSlotsCollection
                .Find(s => s.StationId == stationId && s.SlotStart >= startDate && s.SlotStart <= endDate)
                .ToListAsync();
        }
    }
}
