package com.inst.mobileinstitutions.Forms.Show;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Models.Field;
import com.inst.mobileinstitutions.API.Models.FieldOption;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.API.Models.User;
import com.inst.mobileinstitutions.Complaints.List.ComplaintListActivity;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;
import com.inst.mobileinstitutions.HomeActivity;
import com.inst.mobileinstitutions.LoginActivity;
import com.inst.mobileinstitutions.R;
import com.inst.mobileinstitutions.SingleFragmentActivity;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import android.location.LocationListener;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FormFragment extends android.support.v4.app.Fragment{

    private static final String ARG_FORM_ID = "form_id";
    private static final int REQUEST_CAMERA=0;

    private TextView mFormTitle;
    private LinearLayout mFormHolder;
    private String mFormId;
    private Button mSubmitButton;
    private Button mCameraButton;
    private ImageView mImage;
    private Button mUserLocation;
    private TextView mUserAddress;
    private EditText address;


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
        mUserLocation= (Button) v.findViewById(R.id.location);
        mUserAddress=(TextView) v.findViewById(R.id.address);
        mCameraButton=(Button) v.findViewById(R.id.take_photo);
        mImage= (ImageView) v.findViewById(R.id.photo_image);

        mSubmitButton = (Button)v.findViewById(R.id.form_submit_button);
        APICall.getResource("form", Integer.valueOf(mFormId)).subscribe(new Action1<Form>() {
            @Override
            public void call(Form form) {
                mFormTitle.setText(form.getName());
                populateForm(form.getFields());
                setupSubmitButton(form.getId(), APICredentials.getLoggedUser() != null ? APICredentials.getUsername() : null);
            }
        });
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        mUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLocationAddress();
            }
        });
        return v;
    }

    private void getUserLocationAddress(){
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Toast.makeText(getActivity().getApplicationContext(),"GPS на телефона е изключен!", Toast.LENGTH_LONG).show();
        List<String> providers = locationManager.getProviders(criteria,true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
                onLocationChanged(l);
                locationManager.requestLocationUpdates(provider, 20000, 10, locationListener);
            }
        }
    }
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geocoder= new Geocoder(getActivity().getApplicationContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude, 1);
            if(addresses != null) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                mUserAddress.setText("Вие се намирате на следният адрес:\n " +strAddress.toString());
                if(address!=null)
                address.setText(strAddress.toString());
            }

            else
                mUserLocation.setText("Не е намерена локация!");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),"Няма връзка с интернет!", Toast.LENGTH_LONG).show();
        }
    }





    private void takePicture(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void populateForm(List<Field> fields) {
        for (Field field : fields) {
            switch(field.getTypeName()){
                case "textfield":
                    EditText text = new EditText(getActivity());
                    text.setHint(field.getName());
                    mFormHolder.addView(text);
                    setAutofill(text, field);
                    break;

                case "textarea":
                    EditText textarea = new EditText(getActivity());
                    textarea.setHint(field.getName());
                    mFormHolder.addView(textarea);
                    break;

                case "dropdown":
                    Spinner dropdown = new Spinner(getActivity());
                    ArrayAdapter<FieldOption> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_list_item, field.getFieldOptions());
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerArrayAdapter.add(new FieldOption(field.getName()));
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
                    email.setHint(field.getName());
                    mFormHolder.addView(email);
                    setAutofill(email, field);
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

    private void setAutofill(EditText textfield, Field field){
        User currentUser = APICredentials.getLoggedUser();
        if(field.getAutofill()==5)
            address=textfield;
        if(currentUser == null)
            return;
        switch (field.getAutofill()){
            case 0:
                textfield.setText(currentUser.getFirst_name());
                break;
            case 1:
                textfield.setText(currentUser.getLast_name());
                break;
            case 2:
                textfield.setText(currentUser.getCity());
                break;
            case 3:
                textfield.setText(currentUser.getPhone());
                break;
            case 4:
                textfield.setText(currentUser.getEmail());
                break;
            case 5:
                textfield.setText(currentUser.getAddress());
                break;
        }
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
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                mImage.setImageBitmap(image);
            }
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
                        int chosen_id = radios.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) radios.getChildAt(chosen_id);
                        if (chosen_id != -1)
                            fields.put(fieldHtmlNames.get(i-filesSoFar), rb.getText().toString());
                        else
                            fields.put(fieldHtmlNames.get(i-filesSoFar), "");
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
                        startActivity(new Intent(getActivity(), ComplaintListActivity.class));
                        Toast.makeText(getActivity().getApplicationContext(), "Form submitted successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("form submit exception", e);
                        startActivity(new Intent(getActivity(), ComplaintListActivity.class));
                        Toast.makeText(getActivity().getApplicationContext(), "Error. Form might not have\nbeen submtted successfully", Toast.LENGTH_LONG).show();
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