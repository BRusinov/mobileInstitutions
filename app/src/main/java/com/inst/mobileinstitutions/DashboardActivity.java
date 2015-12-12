package com.inst.mobileinstitutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    private TextView first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        first=(TextView) findViewById(R.id.first);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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
            case R.id.complaint:
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                return true;
            case R.id.sent_complaints:
                startActivity(new Intent(DashboardActivity.this, FormListActivity.class));
            case R.id.logout:
                ProfileActivity logout= new ProfileActivity();
                logout.Logout(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
