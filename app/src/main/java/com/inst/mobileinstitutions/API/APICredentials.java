package com.inst.mobileinstitutions.API;

import android.util.Log;

import com.google.gson.JsonObject;
import com.inst.mobileinstitutions.API.Models.User;

import java.io.IOException;

public class APICredentials {
    static final private String grant_type = "password";
    static private String password = "password";
    static private String username = "test";
    static final private String client_id = "SDu7Taw00Bj1xjFMn5gG4PqDLdhEMqBfvw0nJxRF";
    static final private String client_secret = "CmnO3ncjo6ZB9sAmwcRJl2U6MLI7ZG58WAfjyEhJ39VQPYcOMndZWRWsvi39Fm8Hs6veSOl0SLUa5okDx355wZolhRE2VW9gACDizx0IM8X3QQs5N2gu2hVmBr7aPg0r";

    static private String accessTokenType ="Bearer";
    static private String accessToken = "";

    static private User loggedUser = null;

    public static void setPassword(String newPassword) {
        password = newPassword;
    }

    public static void setUsername(String newUsername) {
        username = newUsername;
    }

    public static String getUsername(){
        return username;
    }

    public static String getPassword(){
        return password;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        APICredentials.loggedUser = loggedUser;
    }

    public static String getAccessToken() {
        return accessTokenType + " " + accessToken;
    }

    public static void cancelAccessToken(){
        accessToken = "";
    }

    public static String resetAccessToken() throws IOException{
        APIUrls service = APICall.getService();
        JsonObject response = service.getRefresh(grant_type, username, password, client_id, client_secret).execute().body();
        if(response != null)
            accessToken = response.get("access_token").getAsString();
        return getAccessToken();
    }
}
