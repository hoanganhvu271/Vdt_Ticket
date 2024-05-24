package com.hav.vt_ticket.Api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.hav.vt_ticket.Model.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hav.vt_ticket.Model.Ticket;

public interface ApiService {

    //link: https://vt-app-api.onrender.com/api/

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://vt-app-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("get-stop")
    Call<ApiResponse<Location>> getLocation();

    @GET("search-ticket")
    Call<ApiResponse<Ticket>>findTicket(@Query("fr") String from, @Query("to") String to, @Query("time") String time);

    @GET("get-price")
    Call<ApiResponse<Ticket>>getPrice(@Query("id") int id);
}
