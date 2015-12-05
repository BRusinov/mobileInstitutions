package com.inst.mobileinstitutions.API;

import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class APIRESTAdapter {
    static <T> T createRetrofitService(final Class<T> APIClass, final String endPoint) {

        OkHttpClient httpClient = new OkHttpClient();

        httpClient.networkInterceptors().add(new AuthInterceptor());
        httpClient.setAuthenticator(new AuthAuthenticator());

        // for http debug
        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(APIClass);
    }
}
