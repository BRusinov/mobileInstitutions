package com.inst.mobileinstitutions.API.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {
    private String id;
    private String name;
    private int type;
    private String html_name;
    private String description;
    private String autofill;
    private String required;
    private String validation_type;
    private String maximum;
    private String minimum;
    private String choose_one;
    @SerializedName("fieldoption_set")
    private List<FieldOption> fieldOptions;

    static final String[] FIELD_TYPES = {
        null, "textfield",  "textarea", "dropdown", "radio", "email", "file", "checkbox", "hidden"
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHtmlName() {
        return html_name;
    }

    public String getAutofill() {
        return autofill;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return FIELD_TYPES[type];
    }

    public String getDescription() {
        return description;
    }

    public String getValidationType() {
        return validation_type;
    }

    public String getMaximum() {
        return maximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public String getChooseOne() {
        return choose_one;
    }

    public List<FieldOption> getFieldOptions() {
        return fieldOptions;
    }

    public String getRequired() {
        return required;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String print(){
        return String.format("%s, %s, %s, %s", id, name, description, type);
    }

    public Field(){
        fieldOptions = new ArrayList<>();
    }

    public Field(String name, Boolean required, String type){
        this.name = name;
        this.required = required.toString();
        this.type = Arrays.asList(FIELD_TYPES).indexOf(type);
        fieldOptions = new ArrayList<>();
    }

    public void addFieldOption(FieldOption newFieldOption){
        if(!fieldOptions.contains(newFieldOption)){
            fieldOptions.add(newFieldOption);
        }
    }

    public void updateField(Field updatedField){
        name = updatedField.getName();
        type = updatedField.getType();
        required = updatedField.getRequired();
        fieldOptions = updatedField.getFieldOptions();
    }
}
