package com.inst.mobileinstitutions.API.Models;

public class FieldOption {
    String option;

    public FieldOption(String option){
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    @Override
    public String toString(){
        return option;
    }
}
