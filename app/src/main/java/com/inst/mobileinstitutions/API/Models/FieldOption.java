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

    @Override
    public boolean equals(Object object) {
        return object instanceof FieldOption && option.equals(((FieldOption)object).getOption());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (int i = 0; i < option.length(); i++) {
            hash = hash*31 + option.charAt(i);
        }
        return hash;
    }
}
