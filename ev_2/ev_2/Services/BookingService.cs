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
    }
}
