package com.inst.mobileinstitutions.Forms.CreateEdit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Field;
import com.inst.mobileinstitutions.API.Models.FieldOption;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.R;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class CreateEditFormFragment extends Fragment {

    private static final String ARG_FORM_ID = "form_id";

    private EditText mFormTitle;
    private LinearLayout mFormHolder;
    private String mFormId;
    private Button mSubmitButton;
    private Button mNewFieldButton;
    private Form mForm;
    private Boolean newForm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFormId = (String) getArguments().getSerializable(ARG_FORM_ID);
    }

    public static CreateEditFormFragment newInstance(String formId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORM_ID, formId);
        CreateEditFormFragment fragment = new CreateEditFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_edit_form, parent, false);
        mFormTitle = (EditText)v.findViewById(R.id.form_title_edit_text);
        mFormHolder = (LinearLayout)v.findViewById(R.id.form_holder_layout);
        mSubmitButton = (Button)v.findViewById(R.id.form_submit_button);
        mNewFieldButton = (Button)v.findViewById(R.id.form_add_field_button);
        newForm = (mFormId.equals("newForm"));
        if(newForm){
            mSubmitButton.setText("Създай");
            mForm = new Form();
            setupNewFieldButton();
        }else {
            APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
                @Override
                public void call(Form form) {
                    mFormTitle.setText(form.getName());
                    mForm = form;
                    putFields();
                    setupNewFieldButton();
                }
            });
            mSubmitButton.setText("Обнови");
        }
        return v;
    }

    private void putFields(){
        for(Field field : mForm.getFields()){
            setFieldEdit(field);
        }
    }

    private void setFieldEdit(Field field){
        final LinearLayout fieldLayout = new LinearLayout(getActivity());
        fieldLayout.setOrientation(LinearLayout.VERTICAL);

        final Button fieldButton = new Button(getActivity());
        fieldButton.setText(field.getName());

        EditText fieldName = new EditText(getActivity());
        fieldName.setText(field.getName());

        CheckBox required = new CheckBox(getActivity());
        required.setText("required");


        final LinearLayout fieldOptionsLayout = new LinearLayout(getActivity());
        fieldOptionsLayout.setVisibility(View.GONE);
        fieldOptionsLayout.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout fieldOptionsHolder = new LinearLayout(getActivity());
        fieldOptionsHolder.setOrientation(LinearLayout.VERTICAL);
        fieldOptionsLayout.addView(fieldOptionsHolder);

        for(FieldOption fieldOption : field.getFieldOptions()){
            EditText option = new EditText(getActivity());
            option.setText(fieldOption.getOption());
            fieldOptionsHolder.addView(option);
        }

        Button newFieldOption = new Button(getActivity());
        newFieldOption.setText("нова опция");
        newFieldOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newOption = new EditText(getActivity());
                fieldOptionsHolder.addView(newOption);
            }
        });
        fieldOptionsLayout.addView(newFieldOption);


        Spinner fieldType = new Spinner(getActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.field_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldType.setAdapter(adapter);
        fieldType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("dropdown") || item.equals("radio")) {
                    fieldOptionsLayout.setVisibility(View.VISIBLE);
                }else{
                    fieldOptionsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fieldLayout.addView(fieldName);
        fieldLayout.addView(fieldType);
        fieldLayout.addView(required);
        fieldLayout.addView(fieldOptionsLayout);

        fieldLayout.setVisibility(View.GONE);
        mFormHolder.addView(fieldButton);
        mFormHolder.addView(fieldLayout);

        fieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newVisibility = fieldLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
                fieldLayout.setVisibility(newVisibility);
            }
        });
    }

    private void setupNewFieldButton(){
        mNewFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFieldEdit(new Field());
            }
        });
    }
}