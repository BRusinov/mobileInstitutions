package com.inst.mobileinstitutions.Forms.Show;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

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
                    Button fileButton = new Button(getActivity());
                    mFormHolder.addView(fileButton);
                    fileButton.setText(field.getName());
                    showChooser(FILE_CHOOSER_REQUEST_CODE);
                    fileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                Log.w("permissionsss", "just kinda curious. Whats the feelin' to be wrong again");
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_BEGGING_REQUEST_CONSTANT);
                            } else {
                                Log.w("permisssionsss", "this didnt work a minute ago. Still doesnt but for some reason does....");
                                showChooser(FILE_CHOOSER_REQUEST_CODE);
                            }
                        }
                    });
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

    private final int FILE_CHOOSER_REQUEST_CODE = 42;
    private final int PERMISSION_BEGGING_REQUEST_CONSTANT = 234;

    private void showChooser(int request_code) {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target, "pesho");
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.w("permissionssss", "'least we reach here");
        switch (requestCode) {
            case PERMISSION_BEGGING_REQUEST_CONSTANT: {
                Log.w("permissionsss", "last stop....");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("permissionsss", "ok i dont get it... if we do get here then whats wrong. Is there no God?!?1!");
                    showChooser(FILE_CHOOSER_REQUEST_CODE);
                }
                break;
            }

            default:
                Log.w("permissionsss", "then.... whats the request code?: " + requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("permissionsssss", Integer.toString(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)));
        try {
            final Uri uri = data.getData();
            final String path = FileUtils.getPath(getActivity(), uri);
        } catch (Exception ex) {
            Log.w("pesho", ex);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}