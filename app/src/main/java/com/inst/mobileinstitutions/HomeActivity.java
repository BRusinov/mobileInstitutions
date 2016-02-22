package com.inst.mobileinstitutions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.appevents.AppEventsLogger;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Models.Complaint;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;
import com.inst.mobileinstitutions.Complaints.List.ComplaintListActivity;

import rx.Subscriber;
import rx.functions.Action1;


public class HomeActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*APICall.getResource("forms").subscribe(sendit Subscriber<List<Form>>() {
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
        //APICall.signIn("test0@gmail.com", "password");

        /*ContentResolver cR = HomeActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        Uri myUri = Uri.fromFile(sendit File("/storage/1700-131C/Pictures/mypic.jpg"));

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

}
