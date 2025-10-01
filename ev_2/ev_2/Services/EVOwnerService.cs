using EV_2.Models;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EV_2.Services
{
    public class EVOwnerService
    {
        private readonly IMongoCollection<EVOwner> _owners;

        public EVOwnerService(IOptions<DatabaseSettings> dbSettings, IMongoClient mongoClient)
        {
            var database = mongoClient.GetDatabase(dbSettings.Value.DatabaseName);
            _owners = database.GetCollection<EVOwner>("EVOwners"); // MongoDB collection name
        }

        // Get all EVOwners
        public async Task<List<EVOwner>> GetAsync() =>
            await _owners.Find(_ => true).ToListAsync();

        // Get a specific EVOwner by NIC
        public async Task<EVOwner?> GetAsync(string nic) =>
            await _owners.Find(o => o.NIC == nic).FirstOrDefaultAsync();

        // Create a new EVOwner
        public async Task CreateAsync(EVOwner newOwner) =>
            await _owners.InsertOneAsync(newOwner);

        // Update existing EVOwner
        public async Task UpdateAsync(string nic, EVOwner updatedOwner) =>
            await _owners.ReplaceOneAsync(o => o.NIC == nic, updatedOwner);

        // Delete EVOwner
        public async Task DeleteAsync(string nic) =>
            await _owners.DeleteOneAsync(o => o.NIC == nic);
    }
}
