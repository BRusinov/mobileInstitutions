package com.inst.mobileinstitutions.Forms.Show;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Field;
import com.inst.mobileinstitutions.API.Models.FieldOption;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.R;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;


public class FormFragment extends android.support.v4.app.Fragment {

    private static final String ARG_FORM_ID = "form_id";

    private TextView mFormTitle;
    private LinearLayout mFormHolder;
    private String mFormId;
    private Form mForm;

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
        mFormTitle = (TextView)v.findViewById(R.id.form_id_text_field);
        mFormHolder = (LinearLayout)v.findViewById(R.id.form_holder_layout);
        APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
            @Override
            public void call(Form form) {
                mFormTitle.setText(form.getName());
                populateForm(form.getFields());
                Log.w("debugform", "'ere");
            }
        });
        return v;
    }

    private void populateForm(List<Field> fields) {
        for (Field field : fields) {
            switch(field.getTypeName()){
                case "textfield":
                    EditText text = new EditText(getActivity());
                    mFormHolder.addView(text);
                    break;

                case "textarea":
                    EditText textarea = new EditText(getActivity());
                    mFormHolder.addView(textarea);
                    break;

                case "dropdown":
                    Spinner dropdown = new Spinner(getActivity());
                    ArrayAdapter<FieldOption> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_list_item, field.getFieldOptions());
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(spinnerArrayAdapter);
                    mFormHolder.addView(dropdown);
                    break;

                case "radio":
                    RadioGroup radioGroup = new RadioGroup(getActivity());
                    for(FieldOption option : field.getFieldOptions()){
                        RadioButton newButton = new RadioButton(getActivity());
                        newButton.setText(option.getOption());
                        radioGroup.addView(newButton);
                    }
                    mFormHolder.addView(radioGroup);
                    break;

                case "email":
                    EditText email = new EditText(getActivity());
                    email.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
                    mFormHolder.addView(email);
                    break;

                case "file":
                    EditText file = new EditText(getActivity());
                    mFormHolder.addView(file);
                    break;

                case "checkbox":
                    CheckBox checkBox = new CheckBox(getActivity());
                    mFormHolder.addView(checkBox);
                    break;

                case "hidden":
                    EditText hidden = new EditText(getActivity());
                    mFormHolder.addView(hidden);
                    break;
            }
        }
    }
}