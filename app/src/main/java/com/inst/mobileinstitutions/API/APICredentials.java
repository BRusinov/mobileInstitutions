package com.inst.mobileinstitutions.API;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

public class APICredentials {
    static final private String grant_type = "password";
    static final private String password = "password";
    static final private String username = "test";
    static final private String client_id = "SDu7Taw00Bj1xjFMn5gG4PqDLdhEMqBfvw0nJxRF";
    static final private String client_secret = "CmnO3ncjo6ZB9sAmwcRJl2U6MLI7ZG58WAfjyEhJ39VQPYcOMndZWRWsvi39Fm8Hs6veSOl0SLUa5okDx355wZolhRE2VW9gACDizx0IM8X3QQs5N2gu2hVmBr7aPg0r";

    static private String accessTokenType ="Bearer";
    static private String accessToken = "";

    public static String getAccessToken() {
        return accessTokenType + " " + accessToken;
    }

    public static String resetAccessToken() throws IOException{
        APIUrls service = APICall.getService();
        JsonObject response = service.getRefresh(grant_type, username, password, client_id, client_secret).execute().body();
        accessToken = response.get("access_token").getAsString();
        return getAccessToken();
    }
}
