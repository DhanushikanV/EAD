package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.EVOwner;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * EVOwnerService Interface
 * 
 * This service interface defines EV Owner management API endpoints.
 * It handles CRUD operations for EV owner profiles.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface EVOwnerService {

    /**
     * Get EV owner by NIC
     * 
     * @param nic User NIC
     * @return Call<EVOwner> containing owner details
     */
    @GET("evowner/{nic}")
    Call<EVOwner> getOwner(@Path("nic") String nic);

    /**
     * Update EV owner profile
     * 
     * @param nic User NIC
     * @param owner Updated owner data
     * @return Call<Void> for update confirmation
     */
    @PUT("evowner/{nic}")
    Call<Void> updateOwner(@Path("nic") String nic, @Body EVOwner owner);

    /**
     * Deactivate EV owner account
     * 
     * @param nic User NIC
     * @return Call<Void> for deactivation confirmation
     */
    @POST("evowner/{nic}/deactivate")
    Call<Void> deactivateOwner(@Path("nic") String nic);

    /**
     * Reactivate EV owner account (if supported by backend)
     * 
     * @param nic User NIC
     * @return Call<Void> for reactivation confirmation
     */
    @POST("evowner/{nic}/reactivate")
    Call<Void> reactivateOwner(@Path("nic") String nic);
}

