package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Login;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rule;
import com.example.frontmynbnb.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
                        @Part("address") RequestBody address,
                        @Part MultipartBody.Part imageFile);

    @GET("users/{username}/username")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("users/{id}/image")
    Call<ResponseBody> getUserImage(@Path("id") int id);

    @PUT("users/{id}/host")
    Call<User> switchUserHost(@Path("id") int id);

    @Multipart
    @PUT("users/{id}")
    Call<User> editUser(
            @Path("id") int id,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("phone") RequestBody phone,
            @Part("host") RequestBody isHost,
            @Part("address") RequestBody address,
            @Part MultipartBody.Part imageFile);

    @GET("users/{id}/places")
    Call<Place> getUsersPlaceById(@Path("id") int id);

    @GET("users/username/{username}/places")
    Call<Place> getUsersPlaceByUsername(@Path("username") String username);

    @GET("places/{id}/mainimage")
    Call<ResponseBody> getPlaceMainImage(@Path("id") int id);

    @GET("images/{id}")
    Call<ResponseBody> getImageById(@Path("id") int id);

    @Multipart
    @POST("users/{id}/places")
    Call<Place> postUsersPlace(
            @Path("id") int id,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("maxGuests") RequestBody maxGuests,
            @Part("minCost") RequestBody minCost,
            @Part("costPerPerson") RequestBody costPerPerson,
            @Part("type") RequestBody type,
            @Part("description") RequestBody description,
            @Part("beds") RequestBody beds,
            @Part("baths") RequestBody baths,
            @Part("bedrooms") RequestBody bedrooms,
            @Part("livingRoom") RequestBody livingRoom,
            @Part("area") RequestBody area,
            @Part MultipartBody.Part imageFile);

    @FormUrlEncoded
    @POST("places/{id}/benefits")
    Call<Benefit> postPlaceBenefit(@Path("id") int id, @Field("content") String content);

    @FormUrlEncoded
    @POST("places/{id}/rules")
    Call<Rule> postPlaceRule(@Path("id") int id, @Field("content") String content);

    @FormUrlEncoded
    @POST("places/{id}/availabilities")
    Call<Availability> postPlaceAvailability(
            @Path("id") int id,
            @Field("from") String from,
            @Field("to") String to
    );

    @Multipart
    @POST("places/{id}/images")
    Call<String> postPlaceImage(
            @Path("id") int id,
            @Part MultipartBody.Part imageFile

    );







    //tests---------------------------------------------------------//

    @GET("messages")
    Call<List<Message>> getAllMessages();

    @POST("places/6/multi-rules") //not works
    Call<String> setMultipleRules(@Body List<String> rules);

}
