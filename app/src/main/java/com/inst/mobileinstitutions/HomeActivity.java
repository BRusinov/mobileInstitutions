package com.inst.mobileinstitutions;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;
import com.squareup.okhttp.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*APICall.getResource("forms").subscribe(new Subscriber<List<Form>>() {
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
                for(Form form:forms) {
                    Log.w("homeFormsIteration", form.print());
                }

            }
        });*/

        //APICall.signUp("vengefulfly@abv.com", "withapassword");
        //APICall.signIn("dimitar.trz@gmail.com", "pesho123");

        /*ContentResolver cR = HomeActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        Uri myUri = Uri.fromFile(new File("/storage/1700-131C/Pictures/mypic.jpg"));

        String extension = MimeTypeMap.getFileExtensionFromUrl("/storage/1700-131C/Pictures/mypic.jpg");
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        try {
            String type1 = cR.getType(myUri);
            String type2 = mime.getExtensionFromMimeType(type1);
            MediaType type3 = MediaType.parse(type2);
        } catch (Exception ex) {Log.w("ex", ex);}*/

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
