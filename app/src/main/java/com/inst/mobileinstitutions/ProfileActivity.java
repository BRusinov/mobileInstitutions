package com.inst.mobileinstitutions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Models.User;
import com.inst.mobileinstitutions.Complaints.List.ComplaintListActivity;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class ProfileActivity extends BaseMenuActivity {

    private User user = APICredentials.getLoggedUser();

    private AutoCompleteTextView mFirstNameTextView;
    private AutoCompleteTextView mLastNameTextView;
    private AutoCompleteTextView mEmailTextView;

    private AutoCompleteTextView mCityTextView;
    private AutoCompleteTextView mPhoneTextView;
    private AutoCompleteTextView mAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setFields();
        setUserFields();

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword();
            }
        });
    }

    private void confirmPassword(){
        final EditText password = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Потвърдете паролата, за да запазите промените")
                .setMessage("Парола:")
                .setView(password)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // need to implement password verification
                        if (APICredentials.getPassword().equals(password.getText().toString())) {
                            saveChanges();
                            startActivity(new Intent(ProfileActivity.this, ComplaintListActivity.class));
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setFields(){
        mFirstNameTextView = (AutoCompleteTextView)findViewById(R.id.profile_name_text_view);
        mLastNameTextView = (AutoCompleteTextView)findViewById(R.id.profile_surname_text_view);
        mEmailTextView = (AutoCompleteTextView)findViewById(R.id.profile_email_text_view);

        mCityTextView = (AutoCompleteTextView)findViewById(R.id.profile_city_text_view);
        mPhoneTextView = (AutoCompleteTextView)findViewById(R.id.profile_phone_text_view);
        mAddressTextView = (AutoCompleteTextView)findViewById(R.id.profile_address_text_view);
    }

    private void setUserFields(){
        mFirstNameTextView.setText(user.getFirst_name());
        mLastNameTextView.setText(user.getLast_name());
        mEmailTextView.setText(user.getEmail());

        mCityTextView.setText(user.getCity());
        mPhoneTextView.setText(user.getPhone());
        mAddressTextView.setText(user.getAddress());
    }

    private void saveChanges(){
        Map<String, String > newUserInfo = new HashMap<String, String>();

        newUserInfo.put("first_name", mFirstNameTextView.getText().toString());
        newUserInfo.put("last_name", mLastNameTextView.getText().toString());
        newUserInfo.put("email", mEmailTextView.getText().toString());
        newUserInfo.put("city", mCityTextView.getText().toString());
        newUserInfo.put("phone", mPhoneTextView.getText().toString());
        newUserInfo.put("address", mAddressTextView.getText().toString());

        newUserInfo.put("old_pass", APICredentials.getPassword());

        APICall.updateUser(user.getId(), newUserInfo);
    }

}
