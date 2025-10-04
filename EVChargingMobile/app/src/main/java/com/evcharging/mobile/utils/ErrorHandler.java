package com.evcharging.mobile.utils;

import android.content.Context;
import android.widget.Toast;

import com.evcharging.mobile.R;

import retrofit2.HttpException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * ErrorHandler
 * 
 * This utility class provides centralized error handling
 * for network operations and API calls.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class ErrorHandler {

    /**
     * Handle API errors and show appropriate messages
     * 
     * @param context Application context
     * @param throwable Error throwable
     */
    public static void handleError(Context context, Throwable throwable) {
        String errorMessage = getErrorMessage(context, throwable);
        showErrorToast(context, errorMessage);
    }

    /**
     * Get appropriate error message based on error type
     * 
     * @param context Application context
     * @param throwable Error throwable
     * @return Error message string
     */
    public static String getErrorMessage(Context context, Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            return getHttpErrorMessage(context, httpException.code());
        } else if (throwable instanceof IOException) {
            if (throwable instanceof SocketTimeoutException) {
                return context.getString(R.string.error_timeout);
            } else if (throwable instanceof ConnectException) {
                return context.getString(R.string.error_connection_failed);
            } else if (throwable instanceof UnknownHostException) {
                return context.getString(R.string.error_server_unreachable);
            } else {
                return context.getString(R.string.error_network);
            }
        } else if (throwable instanceof SecurityException) {
            return context.getString(R.string.error_permission_denied);
        } else {
            return context.getString(R.string.error_unknown);
        }
    }

    /**
     * Get HTTP error message based on status code
     * 
     * @param context Application context
     * @param statusCode HTTP status code
     * @return Error message string
     */
    private static String getHttpErrorMessage(Context context, int statusCode) {
        switch (statusCode) {
            case 400:
                return context.getString(R.string.error_bad_request);
            case 401:
                return context.getString(R.string.error_unauthorized);
            case 403:
                return context.getString(R.string.error_forbidden);
            case 404:
                return context.getString(R.string.error_not_found);
            case 409:
                return context.getString(R.string.error_conflict);
            case 422:
                return context.getString(R.string.error_validation);
            case 500:
                return context.getString(R.string.error_server);
            case 502:
                return context.getString(R.string.error_bad_gateway);
            case 503:
                return context.getString(R.string.error_service_unavailable);
            default:
                return context.getString(R.string.error_http, statusCode);
        }
    }

    /**
     * Show error toast message
     * 
     * @param context Application context
     * @param message Error message
     */
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Handle authentication errors
     * 
     * @param context Application context
     * @param throwable Error throwable
     * @return true if authentication error was handled
     */
    public static boolean handleAuthError(Context context, Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (httpException.code() == 401) {
                // Handle unauthorized - clear session and redirect to login
                SharedPreferencesManager preferencesManager = new SharedPreferencesManager(context);
                preferencesManager.clearUserData();
                
                showErrorToast(context, context.getString(R.string.error_session_expired));
                return true;
            }
        }
        return false;
    }

    /**
     * Check if error is network related
     * 
     * @param throwable Error throwable
     * @return true if network error
     */
    public static boolean isNetworkError(Throwable throwable) {
        return throwable instanceof IOException ||
               throwable instanceof ConnectException ||
               throwable instanceof SocketTimeoutException ||
               throwable instanceof UnknownHostException;
    }

    /**
     * Check if error is authentication related
     * 
     * @param throwable Error throwable
     * @return true if authentication error
     */
    public static boolean isAuthError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            return httpException.code() == 401 || httpException.code() == 403;
        }
        return false;
    }

    /**
     * Log error for debugging
     * 
     * @param tag Log tag
     * @param throwable Error throwable
     */
    public static void logError(String tag, Throwable throwable) {
        android.util.Log.e(tag, "Error occurred", throwable);
    }
}
