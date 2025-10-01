using EV_2.Models;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EV_2.Services
{
    public class UserService
    {
        private readonly IMongoCollection<User> _users;

        public UserService(IOptions<DatabaseSettings> dbSettings, IMongoClient mongoClient)
        {
            var database = mongoClient.GetDatabase(dbSettings.Value.DatabaseName);
            _users = database.GetCollection<User>("Users"); // MongoDB collection name
        }

        // Get all users
        public async Task<List<User>> GetAsync() =>
            await _users.Find(_ => true).ToListAsync();

        // Get user by Id
        public async Task<User?> GetAsync(string id) =>
            await _users.Find(u => u.Id == id).FirstOrDefaultAsync();
        // Get user by Email (for login)
        public async Task<User?> GetByEmailAsync(string email) =>
            await _users.Find(u => u.Email == email).FirstOrDefaultAsync();

        // Create new user
        public async Task CreateAsync(User newUser) =>
            await _users.InsertOneAsync(newUser);

        // Update existing user
        public async Task UpdateAsync(string id, User updatedUser) =>
            await _users.ReplaceOneAsync(u => u.Id == id, updatedUser);

        // Delete user by Id
        public async Task DeleteAsync(string id) =>
            await _users.DeleteOneAsync(u => u.Id == id);
    }
}
