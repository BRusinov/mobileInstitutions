package com.inst.mobileinstitutions.API;

import java.util.List;

public class Forms {
    List<Form> forms;

    public Forms(List<Form> apiForms){
        forms = apiForms;
    }

    public String print(){
        String print = "";
        for(Form form : forms){
            print += (form.print() + "\n");
        }
        return print;
    }
}
