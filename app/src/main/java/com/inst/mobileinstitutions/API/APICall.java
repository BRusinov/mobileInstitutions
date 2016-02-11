package com.inst.mobileinstitutions.API;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.API.Models.User;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class APICall {

    static final private APIUrls service =  APIRESTAdapter.createRetrofitService(APIUrls.class, APIUrls.SERVICE_ENDPOINT);
    static final private String format = "json";

    static final private String[] pluralResources = {"forms", "complaints", "users", "fields"};
    static final private String[] singularResources = {"form", "complaint", "user", "field"};

    private static boolean DoesResourceExist(String desiredResource, int objectId){
        return (objectId>0 ? Arrays.asList(singularResources) : Arrays.asList(pluralResources))
                .contains(desiredResource);
    }

    private static String transformToUrlMethod(String desiredResource){
        String capitalizedResource = desiredResource.substring(0, 1).toUpperCase() + desiredResource.substring(1);
        return "get" + capitalizedResource;
    }

    public static Observable getResource(String desiredResource) {
        return getResource(desiredResource, -1);
    }

    public static Observable getResource(String desiredResource, int objectId) {
        if(DoesResourceExist(desiredResource, objectId))
            try{
                return returnResource(transformToUrlMethod(desiredResource), objectId);
            } catch(Exception ex) { Log.w("reflection exceptions", ex); }
        return null;
    }

    private static Observable returnResource(String urlMethodName, int objectId) throws Exception {
        return (objectId>0 ?
                ((Observable) service.getClass()
                        .getMethod(urlMethodName, int.class, String.class)
                        .invoke(service, objectId, format))
                :
                ((Observable) service.getClass()
                        .getMethod(urlMethodName, String.class)
                        .invoke(service, format)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.w("error", throwable);
                    }
                });

    }

    public static APIUrls getService() {
        return service;
    }

    public static Observable<User> getUserByEmail(String email){
        return service.getUsersByEmail(email, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void updateUser(String id, Map<String, String> userInfo){
        service.updateUser(id, userInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        Log.w("userProfile", "user updated");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("userProfile", e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    public static Observable<JsonObject> submitForm(String formId, String email, Map<String, String> fields, Map<String, RequestBody> files){
        fields.put("form_id", formId);
        fields.put("email", null);
        return service.submitForm(Integer.parseInt(formId), fields, files)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void updateComplaint(int status, String complaintId){
        /*service.updateComplaint(complaintId, status)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());*/
    }

    public static void createForm(Form createdForm){
        service.createForm(createdForm)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        Log.w("jsonOutput", "complet");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("JsonError", e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.w("jsonOutput", jsonObject.toString());
                    }
                });
    }

    public static void updateForm(String id, Form updatedForm){
        service.updateForm(Integer.parseInt(id), updatedForm)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        Log.w("jsonOutput", "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("JsonError", e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.w("jsonOutput", jsonObject.toString());
                    }
                });
    }

    public static void signIn(String username, String password){
        APICredentials.setUsername(username);
        APICredentials.setPassword(password);
        APICredentials.cancelAccessToken();
    }

    public static void signOut(){
        signIn("test", "password");
        APICredentials.setLoggedUser(null);
    }

    public static void signUp(String email, String password){
        service.register(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Log.w("complete", "success");
                    }
                });
    }
}


//Observable observable = ((Observable) service.getClass().getMethod("getForm", int.class, String.class).invoke(service, 1, format));