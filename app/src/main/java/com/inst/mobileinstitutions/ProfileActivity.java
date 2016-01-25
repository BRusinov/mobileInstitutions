package com.inst.mobileinstitutions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inst.mobileinstitutions.Complaints.List.ComplaintListActivity;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Forgotten();
            }
        });
    }

    private void Forgotten(){
        final EditText password= new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Потвърдете паролата, за да запазите промените")
                .setMessage("Парола:")
                .setView(password)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // need to implement password verification
                        startActivity(new Intent(ProfileActivity.this, ComplaintListActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent= new Intent(this, ProfileActivity.class);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.complaint:
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                return true;
            case R.id.sent_complaints:
                startActivity(new Intent(ProfileActivity.this, FormListActivity.class));
            case R.id.logout:
                Logout(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void Logout(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("Съобщение?")
                .setMessage("Сигурни ли сте, че искате да излезете от профила си?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, HomeActivity.class));
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

}
