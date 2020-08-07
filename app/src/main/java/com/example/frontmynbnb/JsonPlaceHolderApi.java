package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Login;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolderApi {
    @POST("login")
    Call<String> login(@Body Login login);

    @Multipart
    @POST("users")
    Call<User> register(@Part("username") RequestBody username,
                        @Part("email") RequestBody email,
                        @Part("password") RequestBody password,
                        @Part("firstName") RequestBody firstName,
                        @Part("lastName") RequestBody lastName,
                        @Part("phone") RequestBody phone,
                        @Part("host") RequestBody isHost,
                        @Part MultipartBody.Part imageFile);

    @GET("messages")
    Call<List<Message>> getAllMessages();

}
