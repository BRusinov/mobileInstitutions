package com.inst.mobileinstitutions.API;

import java.util.Date;

public class Complaint{
    private String id;
    private String status;
    private String created_at;
    private String updated_at;
    private String hash_value;

    public String print(){
        return String.format("%s, %s, %s, %s", id, status, created_at, hash_value);
    }
}
