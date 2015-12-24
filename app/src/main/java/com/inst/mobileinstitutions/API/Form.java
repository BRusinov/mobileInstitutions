package com.inst.mobileinstitutions.API;

import java.util.Date;

public class Form {
    private String id;
    private String name;
    private String inst_name;
    private String created_at;
    private String updated_at;
    private String main_field;

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
}
