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
}
