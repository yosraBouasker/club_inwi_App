package com.inwi.clubinwi.achoura.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

    private String basicAuthString;

    public BasicAuthInterceptor(String basicAuthString) {
        this.basicAuthString = basicAuthString;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request authRequest = request.newBuilder()
                .addHeader("Authorization", basicAuthString)
                .build();

        return chain.proceed(authRequest);
    }
}
