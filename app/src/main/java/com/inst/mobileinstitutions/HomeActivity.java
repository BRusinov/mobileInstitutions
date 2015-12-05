package com.inst.mobileinstitutions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Form;
import com.inst.mobileinstitutions.API.Forms;

import java.io.IOException;
import java.util.List;

import rx.Subscriber;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        APICall.getResource("forms").subscribe(new Subscriber<List<Form>>() {
            @Override
            public void onCompleted() {
                Log.w("homeDone", "done");
            }

            @Override
            public void onError(Throwable e) {
                Log.w("homeError", e);
            }

            @Override
            public void onNext(List<Form> forms) {
                for(Form form : forms) {
                    Log.w("homeFormsIteration", form.print());
                }
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
