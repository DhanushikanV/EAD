package com.evcharging.mobile.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.evcharging.mobile.db.dao.BookingDao;
import com.evcharging.mobile.db.dao.StationDao;
import com.evcharging.mobile.db.dao.StationSlotsDao;
import com.evcharging.mobile.db.dao.UserDao;

/**
 * DatabaseManager
 * 
 * Centralized database management class for raw SQLite operations.
 * Provides access to all DAO classes and manages database lifecycle.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private AppDbHelper dbHelper;
    private SQLiteDatabase database;
    
    // DAO instances
    private UserDao userDao;
    private StationDao stationDao;
    private BookingDao bookingDao;
    private StationSlotsDao stationSlotsDao;

    private DatabaseManager(Context context) {
        this.dbHelper = new AppDbHelper(context);
        initializeDaos(context);
    }

    /**
     * Get singleton instance
     * 
     * @param context Application context
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Initialize DAO instances
     * 
     * @param context Application context
     */
    private void initializeDaos(Context context) {
        this.userDao = new UserDao(context);
        this.stationDao = new StationDao(context);
        this.bookingDao = new BookingDao(context);
        this.stationSlotsDao = new StationSlotsDao(context);
    }

    /**
     * Open database connection
     */
    public void openDatabase() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Close database connection
     */
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * Get UserDao instance
     * 
     * @return UserDao instance
     */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
     * Get StationDao instance
     * 
     * @return StationDao instance
     */
    public StationDao getStationDao() {
        return stationDao;
    }

    /**
     * Get BookingDao instance
     * 
     * @return BookingDao instance
     */
    public BookingDao getBookingDao() {
        return bookingDao;
    }

    /**
     * Get StationSlotsDao instance
     * 
     * @return StationSlotsDao instance
     */
    public StationSlotsDao getStationSlotsDao() {
        return stationSlotsDao;
    }

    /**
     * Clear all data from database
     * Note: This method should be used carefully, preferably only for testing
     */
    public void clearAllTables() {
        openDatabase();
        userDao.open();
        stationDao.open();
        bookingDao.open();
        stationSlotsDao.open();
        
        userDao.deleteAllUsers();
        stationDao.deleteAllStations();
        bookingDao.deleteAllBookings();
        stationSlotsDao.deleteAllSlots();
        
        userDao.close();
        stationDao.close();
        bookingDao.close();
        stationSlotsDao.close();
        closeDatabase();
    }

    /**
     * Check if database is open
     * 
     * @return boolean true if database is open
     */
    public boolean isDatabaseOpen() {
        return database != null && database.isOpen();
    }

    /**
     * Get database instance
     * 
     * @return SQLiteDatabase instance
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }
}
