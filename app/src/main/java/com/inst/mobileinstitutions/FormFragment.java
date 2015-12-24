package com.inst.mobileinstitutions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Form;

import rx.functions.Action1;


public class FormFragment extends android.support.v4.app.Fragment {

    private static final String ARG_FORM_ID = "form_id";

    private String mFormId;
    private TextView mFormField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFormId = (String) getArguments().getSerializable(ARG_FORM_ID);
    }

    public static FormFragment newInstance(String formId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORM_ID, formId);
        FormFragment fragment = new FormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, parent, false);
        mFormField = (TextView)v.findViewById(R.id.form_id_text_field);
        APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
            @Override
            public void call(Form form) {
                mFormField.setText(form.getName());
            }
        });
        return v;
    }



}