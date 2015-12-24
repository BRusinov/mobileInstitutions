package com.inst.mobileinstitutions.API;

public class Field {
    private String id;
    private String name;
    private String type;
    private String html_name;
    private String description;
    private String autofill;
    private String required;
    private String validation_type;
    private String maximum;
    private String minimum;
    private String choose_one;

    public String print(){
        return String.format("%s, %s, %s, %s", id, name, description, type);
    }
}
