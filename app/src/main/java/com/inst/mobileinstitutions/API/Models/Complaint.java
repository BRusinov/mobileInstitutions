package com.inst.mobileinstitutions.API.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Complaint{
    private String id;
    private int status;
    private String created_at;
    private String updated_at;
    private String hash_value;
    @SerializedName("fill_set")
    private List<Fill> fill_set;
    @SerializedName("file_set")
    private List<FileField> file_set;

    String[] statuses = {"Pending", "Rejected", "Resolved"};


    public String print() {
        return String.format("%s, %s, %s", id, created_at, hash_value);
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getHash_value() {
        return hash_value;
    }

    public List<Fill> getFills() {
        return fill_set;
    }

    public List<FileField> getFiles() {
        return file_set;
    }

    public String getStatusString(){
        return statuses[status];
    }
}
