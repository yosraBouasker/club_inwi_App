package com.inwi.clubinwi.achoura.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inwi.clubinwi.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {


    public static final String BASE_URL = BuildConfig.BASE_URL;
    private Retrofit retrofit;

    public static RestClient newInstance() {
        return new RestClient();
    }

    private RestClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new BasicAuthInterceptor("Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4"))
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit client() {
        return retrofit;
    }

    public RestApi api() {
        return retrofit.create(RestApi.class);
    }

}
