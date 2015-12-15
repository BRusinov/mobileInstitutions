package com.inst.mobileinstitutions;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.appevents.AppEventsLogger;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Form;
import com.inst.mobileinstitutions.API.Forms;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import android.content.pm.Signature;

import rx.Subscriber;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        APICall.getResource("forms").subscribe(new Subscriber<List<Form>>() {
            @Override
            public void onCompleted() {
                Log.w("homeDone", "doneIterating");
            }

            @Override
            public void onError(Throwable e) {
                Log.w("homeError", e);
            }

            @Override
            public void onNext(List<Form> forms) {
                for (Form form : forms) {
                    Log.w("homeFormsIteration", form.print());
                }
            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.login:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
            case R.id.register:
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                return true;
            case R.id.forms:
                startActivity(new Intent(HomeActivity.this, FormListActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
