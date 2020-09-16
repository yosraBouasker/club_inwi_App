package com.inwi.clubinwi.achoura.rest;

import com.inwi.clubinwi.achoura.models.listing.Luck;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CkdoApi {

    @GET("api/cadeaux")
    Call<Luck> getCadeaux(@Query("phone") String phone,
                           @Query("token") String token,
                           @Query("lang") String lang);
}
