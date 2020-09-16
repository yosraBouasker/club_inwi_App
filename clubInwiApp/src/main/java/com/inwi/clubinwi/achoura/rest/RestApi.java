package com.inwi.clubinwi.achoura.rest;

import com.inwi.clubinwi.achoura.models.listing.CurrentCadeau;
import com.inwi.clubinwi.achoura.models.listing.Luck;
import com.inwi.clubinwi.achoura.models.reserver.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @GET("api/cadeaux")
    // @Headers("Content-Type: application/json")
    Call<Luck> listCadeaux(@Query("phone") String phone,
                           @Query("token") String token,
                           @Query("lang") String lang);

    @GET("api/cadeaux")
    Call<Luck> getCadeauxList(@Query("phone") String phone,
                              @Query("token") String token);

    @FormUrlEncoded
    @POST("api/cadeaux/reserver")
    Call<Reservation> reserver(@Field("phone") String phone,
                               @Field("token") String token,
                               @Field("lang") String lang,
                               @Field("cadeau_id") String cadeauId,
                               @Field("compagnie_id") String compagnieId,
                               @Field("niveau_id") String niveauId,
                               @Field("points") String points);

}
