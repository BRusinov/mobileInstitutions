package com.inst.mobileinstitutions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.Complaints.List.ComplaintListActivity;
import com.inst.mobileinstitutions.Forms.List.FormListActivity;

public class BaseMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        Boolean noUser = (APICredentials.getLoggedUser() == null);
        menu.setGroupVisible(R.id.unlogged_group, noUser);
        menu.setGroupVisible(R.id.logged_group, !noUser);
        if(!noUser && APICredentials.getLoggedUser().isInstitution())
            menu.findItem(R.id.profile_menu_item).setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.login_menu_item:
                startActivity(new Intent(BaseMenuActivity.this, LoginActivity.class));
                return true;
            case R.id.register_menu_item:
                startActivity(new Intent(BaseMenuActivity.this, RegisterActivity.class));
                return true;

            case R.id.complain_menu_item:
                startActivity(new Intent(BaseMenuActivity.this, FormListActivity.class));
                return true;
            case R.id.sent_complaints_menu_item:
                startActivity(new Intent(BaseMenuActivity.this, ComplaintListActivity.class));
                return true;

            case R.id.profile_menu_item:
                startActivity(new Intent(BaseMenuActivity.this, ProfileActivity.class));
                return true;
            case R.id.logout_menu_item:
                ProfileActivity logout = new ProfileActivity();
                logout.Logout(this);
                return true;
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
                        APICall.signOut();
                        LoginManager.getInstance().logOut();
                        context.startActivity(new Intent(context, HomeActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

