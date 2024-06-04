package com.hav.vt_ticket.Api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  ApiClient {
    public static ApiService createClientApi(String token){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new Interceptor() {
            @NonNull
            @Override public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("access-token", token).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl("https://vt-app-api.onrender.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(ApiService.class);
    }
}
