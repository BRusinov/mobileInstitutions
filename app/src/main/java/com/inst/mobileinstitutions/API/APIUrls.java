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
import retrofit.http.QueryMap;
import rx.Observable;

public interface APIUrls {
    String SERVICE_ENDPOINT = "https://agile-garden-2320.herokuapp.com/";

    @GET("api/forms/")
    Observable<List<Form>> getForms(@QueryMap Map<String, String> query);

    @GET("api/forms/{id}")
    Observable<Form> getForm(@Path("id") int id, @QueryMap Map<String, String> query);

    /*@GET("api/complaints")
    Observable<Complaints> getComplaints(@QueryMap Map<String, String> query);

    @GET("api/complaints/{id}")
    Observable<Complaint> getComplaint(@Path("id") String login, @QueryMap Map<String, String> query);

    @GET("api/users")
    Observable<Users> getUsers(@QueryMap Map<String, String> query);

    @GET("api/users/{id}")
    Observable<User> getUser(@Path("id") String login, @QueryMap Map<String, String> query);*/

    @POST("/o/token/")
    @FormUrlEncoded
    Call<JsonObject> getRefresh(@Field("grant_type") String grant_type, @Field("username") String username,
                                @Field("password") String password, @Field("client_id") String client_id,
                                @Field("client_secret") String client_secret);


}
