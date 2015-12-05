package com.inst.mobileinstitutions.API;

import android.util.Log;

import java.util.Arrays;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class APICall {

    static final private APIUrls service =  APIRESTAdapter.createRetrofitService(APIUrls.class, APIUrls.SERVICE_ENDPOINT);
    static final private String format = "json";

    static final private String[] pluralResources = {"forms", "complaints", "users"};
    static final private String[] singularResources = {"form", "complaint", "user"};

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
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static APIUrls getService() {
        return service;
    }
}


//Observable observable = ((Observable) service.getClass().getMethod("getForm", int.class, String.class).invoke(service, 1, format));