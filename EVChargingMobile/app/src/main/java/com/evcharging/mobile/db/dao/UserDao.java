package com.evcharging.mobile.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evcharging.mobile.db.entities.UserLocal;

/**
 * UserDao Interface
 * 
 * This DAO (Data Access Object) provides methods to interact with the user_local table.
 * It defines database operations for user data management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Dao
public interface UserDao {

    /**
     * Insert a new user or replace if exists
     * 
     * @param user User entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserLocal user);

    /**
     * Update existing user
     * 
     * @param user User entity to update
     */
    @Update
    void updateUser(UserLocal user);

    /**
     * Delete user
     * 
     * @param user User entity to delete
     */
    @Delete
    void deleteUser(UserLocal user);

    /**
     * Get user by NIC
     * 
     * @param nic User NIC
     * @return UserLocal entity or null if not found
     */
    @Query("SELECT * FROM user_local WHERE nic = :nic")
    UserLocal getUserByNic(String nic);

    /**
     * Get user by email
     * 
     * @param email User email
     * @return UserLocal entity or null if not found
     */
    @Query("SELECT * FROM user_local WHERE email = :email")
    UserLocal getUserByEmail(String email);

    /**
     * Check if user exists
     * 
     * @param nic User NIC
     * @return true if user exists, false otherwise
     */
    @Query("SELECT COUNT(*) > 0 FROM user_local WHERE nic = :nic")
    boolean userExists(String nic);

    /**
     * Delete all users (for logout)
     */
    @Query("DELETE FROM user_local")
    void deleteAllUsers();

    /**
     * Update user's auth token
     * 
     * @param nic User NIC
     * @param authToken New auth token
     * @param lastSyncAt Last sync timestamp
     */
    @Query("UPDATE user_local SET authToken = :authToken, lastSyncAt = :lastSyncAt WHERE nic = :nic")
    void updateAuthToken(String nic, String authToken, long lastSyncAt);

    /**
     * Update user's last sync timestamp
     * 
     * @param nic User NIC
     * @param lastSyncAt Last sync timestamp
     */
    @Query("UPDATE user_local SET lastSyncAt = :lastSyncAt WHERE nic = :nic")
    void updateLastSync(String nic, long lastSyncAt);
}

