package com.inst.mobileinstitutions.API;

import java.util.Date;

public class User {
    private String id;
    private String first_name;
    private String last_name;
    private String city;
    private String phone;
    private String email;
    private String address;

    public String print(){
        return String.format("%s, %s, %s, %s, %s, %s, %s", id, first_name, last_name, city, phone, email, address);
    }
}
