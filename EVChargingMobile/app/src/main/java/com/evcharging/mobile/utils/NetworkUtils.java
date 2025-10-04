package com.evcharging.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtils
 * 
 * This utility class provides network-related functionality
 * for checking connectivity and managing network operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class NetworkUtils {

    /**
     * Check if device is connected to internet
     * 
     * @param context Application context
     * @return true if connected, false otherwise
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        
        return false;
    }

    /**
     * Check if device is connected to WiFi
     * 
     * @param context Application context
     * @return true if connected to WiFi, false otherwise
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && 
                   networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                   networkInfo.isConnected();
        }
        
        return false;
    }

    /**
     * Check if device is connected to mobile data
     * 
     * @param context Application context
     * @return true if connected to mobile data, false otherwise
     */
    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && 
                   networkInfo.getType() == ConnectivityManager.TYPE_MOBILE &&
                   networkInfo.isConnected();
        }
        
        return false;
    }

    /**
     * Get current network type
     * 
     * @param context Application context
     * @return Network type string
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        return "WiFi";
                    case ConnectivityManager.TYPE_MOBILE:
                        return "Mobile Data";
                    case ConnectivityManager.TYPE_ETHERNET:
                        return "Ethernet";
                    default:
                        return "Unknown";
                }
            }
        }
        
        return "No Connection";
    }
}

