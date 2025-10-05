package com.evcharging.mobile.network;

import android.content.Context;

import com.evcharging.mobile.utils.SharedPreferencesManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.io.IOException;

/**
 * API Client
 * 
 * This class handles the Retrofit client setup and configuration.
 * It includes authentication interceptors and logging configuration.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class ApiClient {

    // TODO: Replace with your actual backend URL
    // Backend API Configuration - Load from environment
    // Using machine's actual IP address for emulator connection
    private static final String BASE_URL = System.getenv("BACKEND_API_URL") != null ? 
        System.getenv("BACKEND_API_URL") : "http://192.168.8.111:5263/api/";
    // For emulator to connect to host machine backend using actual IP
    
    private static Retrofit retrofit;
    private static SharedPreferencesManager preferencesManager;

    /**
     * Get Retrofit instance
     * 
     * @param context Application context
     * @return Retrofit instance
     */
    public static Retrofit getRetrofitInstance(Context context) {
        if (preferencesManager == null) {
            preferencesManager = new SharedPreferencesManager(context);
        }

        if (retrofit == null) {
            retrofit = createRetrofitInstance(context);
        }
        return retrofit;
    }

    /**
     * Create Retrofit instance with interceptors
     * 
     * @param context Application context
     * @return Configured Retrofit instance
     */
    private static Retrofit createRetrofitInstance(Context context) {
        // Logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Auth interceptor
        Interceptor authInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                
                String authToken = preferencesManager.getAuthToken();
                if (authToken != null && !authToken.isEmpty()) {
                    Request newRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", "Bearer " + authToken)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
                
                Request newRequest = originalRequest.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        // OkHttp client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();

        // Retrofit instance
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    /**
     * Reset Retrofit instance (useful for logout)
     */
    public static void resetInstance() {
        retrofit = null;
    }
}
