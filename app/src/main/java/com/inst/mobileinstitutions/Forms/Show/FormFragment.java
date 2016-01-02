package com.inst.mobileinstitutions.Forms.Show;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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

import com.google.gson.JsonObject;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Field;
import com.inst.mobileinstitutions.API.Models.FieldOption;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;
import com.inst.mobileinstitutions.LoginActivity;
import com.inst.mobileinstitutions.R;
import com.inst.mobileinstitutions.SingleFragmentActivity;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FormFragment extends android.support.v4.app.Fragment {

    private static final String ARG_FORM_ID = "form_id";

    private TextView mFormTitle;
    private LinearLayout mFormHolder;
    private String mFormId;
    private Button mSubmitButton;

    private List<String> fileUris = new ArrayList<>();
    private List<String> fieldHtmlNames = new ArrayList<>();
    private List<String> fileNames = new ArrayList<>();

    private final int FILE_CHOOSER_REQUEST_CODE = 42;
    private final int PERMISSION_BEGGING_REQUEST_CONSTANT = 234;

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
        mSubmitButton = (Button)v.findViewById(R.id.form_submit_button);
        APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
            @Override
            public void call(Form form) {
                mFormTitle.setText(form.getName());
                populateForm(form.getFields());
                setupSubmitButton(form.getId(), "user0@gmail.com");
            }
        });
        return v;
    }

    private void populateForm(List<Field> fields) {
        for (Field field : fields) {
            switch(field.getTypeName()){
                case "textfield":
                    EditText text = new EditText(getActivity());
                    text.setHint(field.getDescription());
                    mFormHolder.addView(text);
                    break;

                case "textarea":
                    EditText textarea = new EditText(getActivity());
                    textarea.setHint(field.getDescription());
                    mFormHolder.addView(textarea);
                    break;

                case "dropdown":
                    Spinner dropdown = new Spinner(getActivity());
                    ArrayAdapter<FieldOption> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_list_item, field.getFieldOptions());
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerArrayAdapter.add(new FieldOption(field.getDescription()));
                    dropdown.setAdapter(spinnerArrayAdapter);
                    dropdown.setSelection(spinnerArrayAdapter.getCount()-1);
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
                    email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    email.setHint(field.getDescription());
                    mFormHolder.addView(email);
                    break;

                case "file":
                    Button fileButton = new Button(getActivity());
                    mFormHolder.addView(fileButton);
                    fileButton.setText(field.getName());
                    fileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_BEGGING_REQUEST_CONSTANT);
                            } else {
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
            if(!field.getTypeName().equals("file"))
                fieldHtmlNames.add(field.getHtmlName());
            else
                fileNames.add(field.getHtmlName());
        }
    }

    private void showChooser(int request_code) {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target, "pesho");
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            final Uri uri = data.getData();
            String path = FileUtils.getPath(getActivity(), uri);
            fileUris.add(path);
        } catch (Exception ex) {
            Log.w("aFileChooserException", ex);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setupSubmitButton(final String formId, final String email){
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> fields = new HashMap<>();
                Map<String, RequestBody> requestFiles = new HashMap<>();
                int filesSoFar = 0;
                for (int i = 0; i < mFormHolder.getChildCount(); i++) {
                    View view = mFormHolder.getChildAt(i);
                    if (view instanceof EditText) {
                        EditText textInput = (EditText) view;
                        fields.put(fieldHtmlNames.get(i-filesSoFar), textInput.getText().toString());
                    }
                    else if (view instanceof Spinner) {
                        Spinner dropdown = (Spinner) view;
                        fields.put(fieldHtmlNames.get(i-filesSoFar), dropdown.getSelectedItem().toString());
                    }
                    else if (view instanceof RadioGroup){
                        RadioGroup radios = (RadioGroup) view;
                        RadioButton rb = (RadioButton)radios.getChildAt(radios.indexOfChild(v.findViewById(radios.getCheckedRadioButtonId())));
                        fields.put(fieldHtmlNames.get(i-filesSoFar), rb.getText().toString());
                    }
                    else if (view instanceof CheckBox){
                        CheckBox checkBox = (CheckBox) view;
                        String boolResult = checkBox.isEnabled() ? "true" : "false";
                        fields.put(fieldHtmlNames.get(i-filesSoFar), boolResult);
                    }
                    else if (view instanceof Button)
                        filesSoFar++;
                }

                for (int i = 0; i < fileUris.size(); i++) {
                    String path = fileUris.get(i);
                    File file = new File(path);
                    String extension = MimeTypeMap.getFileExtensionFromUrl(path);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    MediaType type = MediaType.parse(mimeType);
                    requestFiles.put(fileNames.get(i), RequestBody.create(type, file));
                }
                APICall.submitForm(formId, email, fields, requestFiles).subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        startActivity(new Intent(getActivity(), FormListActivity.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("postEx", e);
                    }

                    @Override
                    public void onNext(JsonObject response) {
                        Log.w("jsonout", response.toString());
                    }
                });
            }
        });
    }
}