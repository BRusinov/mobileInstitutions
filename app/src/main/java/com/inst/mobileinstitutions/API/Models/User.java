package com.inst.mobileinstitutions.API.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    private String id;
    private String django_user_id;
    private int permission_level;
    private String first_name;
    private String last_name;
    private String city;
    private String phone;
    private String email;
    private String address;

    public String print(){
        return String.format("%s, %s, %s, %s, %s, %s, %s", id, first_name, last_name, city, phone, email, address);
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public int getPermission_level() {
        return permission_level;
    }

    public boolean isInstitution(){
        return (getPermission_level() > 1);
    }
}
