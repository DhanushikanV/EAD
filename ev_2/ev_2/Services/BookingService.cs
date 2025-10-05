using EV_2.Models;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EV_2.Services
{
    public class BookingService
    {
        private readonly IMongoCollection<Booking> _bookingCollection;

        // <-- Change constructor to use IOptions<DatabaseSettings>
        public BookingService(IMongoClient mongoClient, IOptions<DatabaseSettings> options)
        {
            var settings = options.Value; // get DatabaseSettings from IOptions
            var database = mongoClient.GetDatabase(settings.DatabaseName);
            _bookingCollection = database.GetCollection<Booking>(settings.BookingCollectionName);

            // Ensure indexes (idempotent)
            var indexModels = new List<CreateIndexModel<Booking>>
            {
                new CreateIndexModel<Booking>(Builders<Booking>.IndexKeys.Ascending(b => b.QrToken),
                    new CreateIndexOptions { Name = "idx_qrToken_unique", Unique = true, Sparse = true }),
                new CreateIndexModel<Booking>(Builders<Booking>.IndexKeys
                    .Ascending(b => b.Status)
                    .Ascending(b => b.ReservationDateTime), new CreateIndexOptions { Name = "idx_status_reservation" }),
                new CreateIndexModel<Booking>(Builders<Booking>.IndexKeys.Ascending(b => b.EVOwnerNIC),
                    new CreateIndexOptions { Name = "idx_owner_nic" })
            };
            _bookingCollection.Indexes.CreateMany(indexModels);
        }

        // CRUD operations
        public async Task<List<Booking>> GetAsync() =>
            await _bookingCollection.Find(_ => true).ToListAsync();

        public async Task<Booking?> GetAsync(string id) =>
            await _bookingCollection.Find(b => b.Id == id).FirstOrDefaultAsync();

        public async Task<List<Booking>> GetByEvOwnerNICAsync(string evOwnerNIC) =>
            await _bookingCollection.Find(b => b.EVOwnerNIC == evOwnerNIC).ToListAsync();

        public async Task CreateAsync(Booking newBooking) =>
            await _bookingCollection.InsertOneAsync(newBooking);

        public async Task UpdateAsync(string id, Booking updatedBooking) =>
            await _bookingCollection.ReplaceOneAsync(b => b.Id == id, updatedBooking);

        public async Task DeleteAsync(string id) =>
            await _bookingCollection.DeleteOneAsync(b => b.Id == id);

        // Business helpers
        public static bool IsWithinSevenDays(DateTime reservationUtcNow, DateTime reservation) =>
            reservation >= reservationUtcNow && reservation <= reservationUtcNow.AddDays(7);

        public static bool CanModifyOrCancel(DateTime reservationUtcNow, DateTime reservation) =>
            reservation.Subtract(reservationUtcNow) >= TimeSpan.FromHours(12);

        public async Task<bool> TrySetStatusAsync(string id, string newStatus, Action<Booking>? mutate = null)
        {
            var existing = await GetAsync(id);
            if (existing is null) return false;
            existing.Status = newStatus;
            existing.UpdatedAt = DateTime.UtcNow;
            mutate?.Invoke(existing);
            await UpdateAsync(id, existing);
            return true;
        }

        public async Task<bool> TrySetStatusAsync(IClientSessionHandle session, string id, string newStatus, Action<Booking>? mutate = null)
        {
            var existing = await _bookingCollection.Find(session, b => b.Id == id).FirstOrDefaultAsync();
            if (existing is null) return false;
            existing.Status = newStatus;
            existing.UpdatedAt = DateTime.UtcNow;
            mutate?.Invoke(existing);
            await _bookingCollection.ReplaceOneAsync(session, b => b.Id == id, existing);
            return true;
        }

        public async Task<Booking?> GetByQrTokenAsync(string token) =>
            await _bookingCollection.Find(b => b.QrToken == token).FirstOrDefaultAsync();
    }
}
