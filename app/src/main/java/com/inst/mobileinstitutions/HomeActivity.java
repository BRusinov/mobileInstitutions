package com.inst.mobileinstitutions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
