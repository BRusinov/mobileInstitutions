package com.inst.mobileinstitutions.API;

import com.google.gson.JsonObject;
import com.inst.mobileinstitutions.API.Models.Complaint;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.API.Models.User;
import com.squareup.okhttp.RequestBody;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface APIUrls {
    String SERVICE_ENDPOINT = "http://podaisignal.herokuapp.com/";

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

    @POST("/api/form_submit/{form_id}/")
    @Multipart
    Observable<JsonObject> submitForm(@Path("form_id") int form_id,
                                      @PartMap Map<String, String> fields,
                                      @PartMap Map<String, RequestBody> files);

    @POST("api/forms/")
    Observable<JsonObject> createForm(@Body Form form_body);

    @PUT("api/forms/{form_id}/")
    Observable<JsonObject> updateForm(@Path("form_id") int form_id, @Body Form form_body);

    /*
    @POST("/accounts/login")
    @FormUrlEncoded
    Observable<JsonObject> login(@Field("email") String email, @Field("password") String password);*/

    //WIP
    @POST("/api/sign_up")
    @FormUrlEncoded
    Observable<JsonObject> register(@Field("email") String email, @Field("password") String password);

    @POST("/o/token/")
    @FormUrlEncoded
    Call<JsonObject> getRefresh(@Field("grant_type") String grant_type, @Field("username") String username,
                                @Field("password") String password, @Field("client_id") String client_id,
                                @Field("client_secret") String client_secret);


}
