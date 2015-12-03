package com.inst.mobileinstitutions.API;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request authorisedRequest = originalRequest.newBuilder()
                .header("Authorization", APICredentials.getAccessToken())
                .build();
        return chain.proceed(authorisedRequest);
    }
}
