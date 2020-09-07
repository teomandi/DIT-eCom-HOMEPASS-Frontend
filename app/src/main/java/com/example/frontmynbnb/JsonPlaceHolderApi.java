package com.example.frontmynbnb;


import com.example.frontmynbnb.models.Availability;
import com.example.frontmynbnb.models.Benefit;
import com.example.frontmynbnb.models.Login;
import com.example.frontmynbnb.models.Message;
import com.example.frontmynbnb.models.Place;
import com.example.frontmynbnb.models.Rating;
import com.example.frontmynbnb.models.Rule;
import com.example.frontmynbnb.models.User;

import java.util.List;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("places")
    Call<List<Place>> getAllPlaces(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize
    );

    @GET("places/{id}")
    Call<Place> getPlaceById(@Path("id") int id);

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
    Call<Void> postPlaceImage(
            @Path("id") int id,
            @Part List<MultipartBody.Part> images
    );

    @Multipart
    @PUT("places/{id}/images")
    Call<Void> putPlaceImage(
            @Path("id") int id,
            @Part List<MultipartBody.Part> images
    );

    @PUT("benefits/{id}")
    Call<Benefit> putBenefit(
            @Path("id") int id,
            @Body Benefit b
    );

    @DELETE("benefits/{id}")
    Call<Void> deleteBenefit(@Path("id") int id);

    @PUT("rules/{id}")
    Call<Rule> putRule(
            @Path("id") int id,
            @Body Rule r
    );

    @DELETE("rules/{id}")
    Call<Void> deleteRule(@Path("id") int id);

    @PUT("availabilities/{id}")
    Call<Availability> putAvailability(
            @Path("id") int id,
            @Body Availability b
    );

    @DELETE("availabilities/{id}")
    Call<Void> deleteAvailability(@Path("id") int id);


    @Multipart
    @PUT("users/{uid}/places/{pid}")
    Call<Place> putUsersPlace(
            @Path("uid") int uid,
            @Path("pid") int pid,
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

    @DELETE("users/{uid}/places/{pid}")
    Call<Void> deletePlace(
            @Path("uid") int uid,
            @Path("pid") int pid
    );

    @DELETE("images/{id}")
    Call<Void> deleteImage(@Path("id") int id);

    @GET("search")
    Call<List<Place>> searchPlaces(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize,
            @Query("type") String type,
            @Query("from") String from,
            @Query("to") String to,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("num") int num
    );

    @GET("places/{id}/users")
    Call<User> getPlaceOwner(@Path("id") int id);

    @GET("places/{id}/benefits")
    Call<Set<Rating>> getRatingsByPlace(@Path("id") int id);

    @FormUrlEncoded
    @POST("users/{uid}/places/{pid}/ratings")
    Call<Rating> postRatingForPlace(
            @Path("uid") int uid,
            @Path("pid") int pid,
            @Field("degree") float degree,
            @Field("comment") String comment
    );

    @GET("messages/host/{uid}")
    Call<List<Message>> getMessagesAsHost(@Path("uid") int id);

    @GET("messages/{uid}")
    Call<List<Message>> getMessagesAsGuest(@Path("uid") int id);

    @GET("messages/host/{huid}/{uid}/chat")
    Call<List<Message>> getChatAsHost(@Path("huid") int huid, @Path("uid") int uid);

    @GET("messages/{u1id}/{u2id}/chat")//in the u1id is the current user
    Call<List<Message>> getChatNotAsHost(@Path("u1id") int u1id, @Path("u2id") int u2id);

    @POST("messages/{sid}/{rid}/{hid}")
    Call<Message> postMessage(
            @Path("sid") int sid,
            @Path("rid") int rid,
            @Path("hid") int hid,
            @Query("text") String text
    );

    @GET("reservations/places/{pid}/users/{uid}/canrate")
    Call<Boolean> checkIfUserCanRate(
            @Path("pid") int pid,
            @Path("uid") int uid
    );

    @FormUrlEncoded
    @POST("/reservations/places/{pid}/users/{uid}")
    Call<Boolean> createReservation(
            @Path("pid") int pid,
            @Path("uid") int uid,
            @Field("start") String start,
            @Field("end") String to
    );


}
