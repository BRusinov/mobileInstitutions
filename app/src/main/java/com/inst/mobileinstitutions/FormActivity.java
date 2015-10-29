package com.inst.mobileinstitutions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;

public class FormActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.inst.android.mobileInstitutions.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, FormActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID formId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return FormFragment.newInstance(formId);
    }
}
