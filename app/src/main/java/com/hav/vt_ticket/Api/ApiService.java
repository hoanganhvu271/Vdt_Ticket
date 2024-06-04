package com.hav.vt_ticket.Api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.hav.vt_ticket.Model.Car;
import com.hav.vt_ticket.Model.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.Model.User;
import com.hav.vt_ticket.RoomDatabase.UserRoom;

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

    @GET("get-ticket")
    Call<ObjectResponse<Ticket>>getTicketById(@Query("id") int id);

    @GET("get-car")
    Call<ApiResponse<Car>>getCarById(@Query("id") int id);

    @POST("login")
    Call<ObjectResponse<User>> checkLogin(@Body LoginData loginData);

    @FormUrlEncoded
    @POST("follow")
    Call<Void> updateFollowingTicket(@Field("tbId") String tbId, @Field("cdId") String ticketArray, @Field("price") String priceArray);


    @FormUrlEncoded
    @POST("billing")
    Call<ObjectResponse<String>> sendBilling(@Field("id_chuyen_di") int ticketId, @Field("time") String time, @Field("z_token") String z_token);

    @GET("purchased-ticket")
    Call<ApiResponse<Ticket>> getPurchasedTicket();

    @FormUrlEncoded
    @POST("register")
    Call<ObjectResponse<String>> register(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("email") String email,
                                         @Field("ten") String name,
                                         @Field("ngay_sinh") String dob,
                                         @Field("gioi_tinh") String sex,
                                         @Field("cccd") String cccd);




}



