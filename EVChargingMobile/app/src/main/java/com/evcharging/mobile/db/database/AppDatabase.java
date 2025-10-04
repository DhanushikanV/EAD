package com.evcharging.mobile.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.evcharging.mobile.db.dao.BookingDao;
import com.evcharging.mobile.db.dao.StationDao;
import com.evcharging.mobile.db.dao.StationSlotsDao;
import com.evcharging.mobile.db.dao.UserDao;
import com.evcharging.mobile.db.entities.BookingLocal;
import com.evcharging.mobile.db.entities.StationCache;
import com.evcharging.mobile.db.entities.StationSlotsCache;
import com.evcharging.mobile.db.entities.UserLocal;

/**
 * AppDatabase
 * 
 * This is the main Room database class that defines the database configuration,
 * entities, and provides access to DAOs. It follows the singleton pattern for
 * efficient database access throughout the application.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Database(
    entities = {
        UserLocal.class,
        StationCache.class,
        StationSlotsCache.class,
        BookingLocal.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "ev_charging_db";
    private static volatile AppDatabase INSTANCE;

    /**
     * Get database instance (Singleton pattern)
     * 
     * @param context Application context
     * @return AppDatabase instance
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // For development - remove in production
            .build();
        }
        return INSTANCE;
    }

    /**
     * Get UserDao instance
     * 
     * @return UserDao instance
     */
    public abstract UserDao userDao();

    /**
     * Get StationDao instance
     * 
     * @return StationDao instance
     */
    public abstract StationDao stationDao();

    /**
     * Get StationSlotsDao instance
     * 
     * @return StationSlotsDao instance
     */
    public abstract StationSlotsDao stationSlotsDao();

    /**
     * Get BookingDao instance
     * 
     * @return BookingDao instance
     */
    public abstract BookingDao bookingDao();

    /**
     * Clear all data from database
     * Note: This method should be used carefully, preferably only for testing
     */
    public void clearAllTables() {
        userDao().deleteAllUsers();
        stationDao().deleteAllStations();
        stationSlotsDao().deleteAllSlots();
        bookingDao().deleteAllBookings();
    }

    /**
     * Close database connection
     */
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
