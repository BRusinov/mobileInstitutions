package com.inst.mobileinstitutions.Forms.CreateEdit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.Toast;

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
            setupCreateEditFormButton();
        }else {
            APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
                @Override
                public void call(Form form) {
                    mFormTitle.setText(form.getName());
                    mForm = form;
                    putFields();
                    setupNewFieldButton();
                    setupCreateEditFormButton();
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
        final Button fieldButton = new Button(getActivity());
        fieldButton.setText(field.getName());

        final LinearLayout fieldLayout = new LinearLayout(getActivity());
        int fieldId = field.getId() == null ? -1 : Integer.parseInt(field.getId());
        fieldLayout.setId(fieldId);
        fieldLayout.setOrientation(LinearLayout.VERTICAL);

        EditText fieldName = new EditText(getActivity());
        fieldName.setText(field.getName());

        CheckBox fieldRequired = new CheckBox(getActivity());
        fieldRequired.setText("required");


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

        Button newFieldOptionButton = new Button(getActivity());
        newFieldOptionButton.setText("нова опция");
        newFieldOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newOption = new EditText(getActivity());
                fieldOptionsHolder.addView(newOption);
            }
        });
        fieldOptionsLayout.addView(newFieldOptionButton);


        Spinner fieldType = new Spinner(getActivity());
        ArrayAdapter<CharSequence> fieldTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.field_types_array, android.R.layout.simple_spinner_item);
        fieldTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldType.setAdapter(fieldTypeAdapter);

        final Spinner autofillType = new Spinner(getActivity());
        final ArrayAdapter<CharSequence> autofillTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.form_autofill_types, android.R.layout.simple_spinner_item);
        autofillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autofillType.setAdapter(autofillTypeAdapter);

        fieldType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("dropdown") || item.equals("radio")) {
                    fieldOptionsLayout.setVisibility(View.VISIBLE);
                } else if (item.equals("textfield") || item.equals("email")) {
                    autofillType.setVisibility(View.VISIBLE);
                } else {
                    fieldOptionsLayout.setVisibility(View.GONE);
                    autofillType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fieldName.setTag("name");
        fieldLayout.addView(fieldName);

        fieldType.setTag("type");
        fieldLayout.addView(fieldType);

        autofillType.setTag("autofill");
        fieldLayout.addView(autofillType);

        fieldRequired.setTag("required");
        fieldLayout.addView(fieldRequired);

        fieldOptionsHolder.setTag("fieldOptions");
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

    private void setupCreateEditFormButton(){
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFormTitle.getText().toString().isEmpty())
                    mForm.setName(mFormTitle.getText().toString());
                else
                    Toast.makeText(getActivity(), "there's a form field out there without a name", Toast.LENGTH_LONG).show();
                for(int i=1; i<mFormHolder.getChildCount(); i+=2){
                    handleField((LinearLayout) mFormHolder.getChildAt(i));
                }

                if(mForm.getId() == null){
                    APICall.createForm(mForm);
                }else{
                    APICall.updateForm(mFormId, mForm);
                }
            }
        });
    }

    private void handleField(LinearLayout fieldHolder){
        String fieldId = Integer.toString(fieldHolder.getId());
        String fieldName = ((EditText) fieldHolder.findViewWithTag("name")).getText().toString();
        Boolean fieldRequired = ((CheckBox) fieldHolder.findViewWithTag("required")).isEnabled();
        String fieldType = ((Spinner) fieldHolder.findViewWithTag("type")).getSelectedItem().toString();
        int autofill = ((Spinner) fieldHolder.findViewWithTag("autofill")).getSelectedItemPosition() - 1;
        Field field = new Field(fieldName, fieldRequired, fieldType);
        field.setAutofill(autofill);
        LinearLayout fieldOptionsHolder = (LinearLayout) fieldHolder.findViewWithTag("fieldOptions");
        for(int i=0; i<fieldOptionsHolder.getChildCount(); i++){
            String newOption = ((EditText) fieldOptionsHolder.getChildAt(i)).getText().toString();
            field.addFieldOption(new FieldOption(newOption));
        }
        if(fieldId.equals("-1")){
            mForm.addField(field);
        }else{
            field.setId(fieldId);
            mForm.updateField(field);
        }
    }
}