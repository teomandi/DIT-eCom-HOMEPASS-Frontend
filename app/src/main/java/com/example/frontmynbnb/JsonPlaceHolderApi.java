package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
//    @POST("login")
//    Call<String> login(@Body Login login);
//
//    @POST("user")
//    Call

    @GET("messages")
    Call<List<Message>> getAllMessages();

}
