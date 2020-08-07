package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Login;
import com.example.frontmynbnb.models.Message;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @POST("login")
    Call<String> login(@Body Login login);

    @GET("messages")
    Call<List<Message>> getAllMessages();

}
