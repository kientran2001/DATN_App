package com.example.datnapp;

import com.example.datnapp.model.LoginRequest;
import com.example.datnapp.model.Record;
import com.example.datnapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.106:4000/app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("loginApp")
    Call<User> loginApp(@Body LoginRequest loginRequest);

    @GET("recentRecord/{waterMeterId}")
    Call<Record> recentRecord(@Path("waterMeterId") String waterMeterId);

    @POST("addRecord")
    Call<String> addRecord(@Body Record record);

}
