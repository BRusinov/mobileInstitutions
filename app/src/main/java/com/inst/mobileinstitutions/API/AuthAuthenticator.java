package com.inst.mobileinstitutions.API;

import android.os.SystemClock;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

public class AuthAuthenticator implements Authenticator {
    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        boolean failedAuthentication = response.request().url().toString().endsWith("/o/token/");
        if (responseCount(response) >= 3 || failedAuthentication) {
            return null;
        }

        String newAccessToken = APICredentials.resetAccessToken();

        return response.request().newBuilder()
                .addHeader("Authorization", newAccessToken)
                .build();
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
