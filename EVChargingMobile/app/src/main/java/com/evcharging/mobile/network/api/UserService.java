package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.EVOwner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * UserService Interface
 * 
 * This service interface defines user management API endpoints.
 * It handles EV owner profile operations and user data retrieval.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface UserService {

    /**
     * Get EV owner by NIC
     * 
     * @param nic EV owner NIC
     * @return Call<EVOwner> containing EV owner details
     */
    @GET("EVOwner/{nic}")
    Call<EVOwner> getEVOwnerByNIC(@Path("nic") String nic);

    /**
     * Get all EV owners
     * 
     * @return Call<List<EVOwner>> containing all EV owners
     */
    @GET("EVOwner")
    Call<List<EVOwner>> getAllEVOwners();
}
