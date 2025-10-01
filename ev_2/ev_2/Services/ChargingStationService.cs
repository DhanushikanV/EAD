using EV_2.Models;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EV_2.Services
{
    public class ChargingStationService
    {
        private readonly IMongoCollection<ChargingStation> _stations;

        public ChargingStationService(IOptions<DatabaseSettings> dbSettings, IMongoClient client)
        {
            var database = client.GetDatabase(dbSettings.Value.DatabaseName);
            _stations = database.GetCollection<ChargingStation>(dbSettings.Value.ChargingStationCollectionName);
        }

        public async Task<List<ChargingStation>> GetAllAsync() =>
            await _stations.Find(_ => true).ToListAsync();

        public async Task<ChargingStation?> GetByIdAsync(string id) =>
            await _stations.Find(s => s.Id == id).FirstOrDefaultAsync();

        public async Task CreateAsync(ChargingStation station) =>
            await _stations.InsertOneAsync(station);

        public async Task UpdateAsync(string id, ChargingStation updatedStation) =>
            await _stations.ReplaceOneAsync(s => s.Id == id, updatedStation);

        public async Task DeleteAsync(string id) =>
            await _stations.DeleteOneAsync(s => s.Id == id);
    }
}
