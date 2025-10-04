package com.evcharging.mobile.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.evcharging.mobile.db.database.AppDbHelper;
import com.evcharging.mobile.db.entities.BookingLocal;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDao - Raw SQLite Implementation
 * 
 * This DAO provides raw SQLite operations for the bookings_local table.
 * Uses SQLiteOpenHelper directly without Room ORM.
 */
public class BookingDao {
    private SQLiteDatabase database;
    private AppDbHelper dbHelper;

    public BookingDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Insert a new booking
     */
    public long insertBooking(BookingLocal booking) {
        ContentValues values = new ContentValues();
        values.put("bookingId", booking.getBookingId());
        values.put("nic", booking.getNic());
        values.put("stationId", booking.getStationId());
        values.put("startTs", booking.getReservationDateTime());
        values.put("status", booking.getStatus());
        values.put("qrPayload", booking.getQrPayload());
        values.put("updatedAt", booking.getUpdatedAt());
        
        return database.insert("bookings_local", null, values);
    }

    /**
     * Get booking by ID
     */
    public BookingLocal getBookingById(String bookingId) {
        Cursor cursor = database.query("bookings_local", null, 
            "bookingId = ?", new String[]{bookingId}, null, null, null);
        
        if (cursor.moveToFirst()) {
            BookingLocal booking = cursorToBooking(cursor);
            cursor.close();
            return booking;
        }
        cursor.close();
        return null;
    }

    /**
     * Get all bookings
     */
    public List<BookingLocal> getAllBookings() {
        List<BookingLocal> bookings = new ArrayList<>();
        Cursor cursor = database.query("bookings_local", null, null, null, null, null, "startTs DESC");
        
        while (cursor.moveToNext()) {
            bookings.add(cursorToBooking(cursor));
        }
        cursor.close();
        return bookings;
    }

    /**
     * Update booking status
     */
    public int updateBookingStatus(String bookingId, String status, long updatedAt) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        values.put("updatedAt", updatedAt);
        
        return database.update("bookings_local", values, "bookingId = ?", 
            new String[]{bookingId});
    }

    /**
     * Update booking QR payload
     */
    public int updateBookingQRPayload(String bookingId, String qrPayload, long updatedAt) {
        ContentValues values = new ContentValues();
        values.put("qrPayload", qrPayload);
        values.put("updatedAt", updatedAt);
        
        return database.update("bookings_local", values, "bookingId = ?", 
            new String[]{bookingId});
    }

    /**
     * Delete all bookings
     */
    public void deleteAllBookings() {
        database.delete("bookings_local", null, null);
    }

    /**
     * Convert cursor to BookingLocal object
     */
    private BookingLocal cursorToBooking(Cursor cursor) {
        BookingLocal booking = new BookingLocal();
        booking.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow("bookingId")));
        booking.setNic(cursor.getString(cursor.getColumnIndexOrThrow("nic")));
        booking.setStationId(cursor.getString(cursor.getColumnIndexOrThrow("stationId")));
        booking.setReservationDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("startTs")));
        booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        booking.setQrPayload(cursor.getString(cursor.getColumnIndexOrThrow("qrPayload")));
        booking.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow("updatedAt")));
        return booking;
    }
}
