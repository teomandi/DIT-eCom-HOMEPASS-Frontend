package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

//    @GET("places")
//    Call<List<Place>> getPlaces();
//
    @GET("messages")
    Call<List<Message>> getAllMessages();
}
