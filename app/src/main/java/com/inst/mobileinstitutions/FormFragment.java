package com.inst.mobileinstitutions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;


public class FormFragment extends android.support.v4.app.Fragment {

    private static final String ARG_FORM_ID = "form_id";

    private UUID mFormId;
    private TextView mFormField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFormId = (UUID) getArguments().getSerializable(ARG_FORM_ID);
    }

    public static FormFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORM_ID, crimeId);
        FormFragment fragment = new FormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_form, parent, false);
        mFormField = (TextView)v.findViewById(R.id.form_id_text_field);
        mFormField.setText("fsdf");
        return v;
    }



}