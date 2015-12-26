package com.inst.mobileinstitutions.API.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Form {
    private String id;
    private String name;
    private String inst_name;
    private String created_at;
    private String updated_at;
    private String main_field;
    @SerializedName("field_set")
    private List<Field> fields;

    public String print(){
        return String.format("%s, %s, %s, %s", id, name, inst_name, main_field);
    }

    public String getName() {
        return name;
    }

    public String getInst_name() {
        return inst_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getMain_field() {
        return main_field;
    }

    public String getId() {
        return id;
    }

    public List<Field> getFields() {
        return fields;
    }
}
