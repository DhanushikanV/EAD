using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using MongoDB.Bson;

namespace EV_2.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class HealthController : ControllerBase
    {
        private readonly IMongoClient _mongoClient;

        public HealthController(IMongoClient mongoClient)
        {
            _mongoClient = mongoClient;
        }

        [HttpGet("mongo")]
        public async Task<IActionResult> Mongo()
        {
            try
            {
                var adminDb = _mongoClient.GetDatabase("admin");
                var command = new BsonDocument("ping", 1);
                await adminDb.RunCommandAsync<BsonDocument>(command);
                return Ok(new { status = "ok" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { status = "error", message = ex.Message });
            }
        }
    }
}


