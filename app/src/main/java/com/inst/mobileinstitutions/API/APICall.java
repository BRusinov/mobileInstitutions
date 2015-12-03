package com.inst.mobileinstitutions.API;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class APICall {

    static final private String format = "json";
    static final private APIUrls service =  APIRESTAdapter.createRetrofitService(APIUrls.class, APIUrls.SERVICE_ENDPOINT);

    public static Observable getResource(String desiredResource){

        if(desiredResource == null){
            Log.w("pesho", "this is bad");
        }

        Map<String, String> query = new HashMap<String, String>() {{
            put("format", format);
        }};

        return service.getForms(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static APIUrls getService() {
        return service;
    }
}
