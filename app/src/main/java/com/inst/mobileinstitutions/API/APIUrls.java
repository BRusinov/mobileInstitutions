package com.inst.mobileinstitutions.API;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

public interface APIUrls {
    String SERVICE_ENDPOINT = "https://agile-garden-2320.herokuapp.com/";

    @GET("api/forms/")
    Observable<List<Form>> getForms(@Query("format") String format);

    @GET("api/forms/{id}")
    Observable<Form> getForm(@Path("id") int id, @Query("format") String format);

    @GET("api/complaints/")
    Observable<List<Complaint>> getComplaints(@Query("format") String format);

    @GET("api/complaints/{id}")
    Observable<Complaint> getComplaint(@Path("id") int id, @Query("format") String format);

    @GET("api/users/")
    Observable<List<User>> getUsers(@Query("format") String format);

    @GET("api/users/{id}")
    Observable<User> getUser(@Path("id") int id, @Query("format") String format);

    @GET("api/fields/")
    Observable<List<Field>> getFields(@Query("format") String format);

    @GET("api/fields/{id}")
    Observable<Field> getField(@Path("id") int id, @Query("format") String format);


    //WIP
    @POST("/accounts/login")
    @FormUrlEncoded
    Call<JsonObject> login(@Field("email") String grant_type, @Field("password") String username);

    @POST("/accounts/signup")
    @FormUrlEncoded
    Call<JsonObject> register(@Field("email") String grant_type, @Field("pword") String username);
    //WIP

    @POST("/o/token/")
    @FormUrlEncoded
    Call<JsonObject> getRefresh(@Field("grant_type") String grant_type, @Field("username") String username,
                                @Field("password") String password, @Field("client_id") String client_id,
                                @Field("client_secret") String client_secret);


}
