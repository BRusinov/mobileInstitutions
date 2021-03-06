package com.inst.mobileinstitutions.Forms.CreateEdit;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.inst.mobileinstitutions.SingleFragmentActivity;

public class CreateEditFormActivity extends SingleFragmentActivity {

    private static final String EXTRA_FORM_ID = "com.inst.android.mobileInstitutions.form_id";

    @Override
    protected Fragment createFragment(){
        String formId = (String) getIntent().getSerializableExtra(EXTRA_FORM_ID);
        return CreateEditFormFragment.newInstance(formId);
    }

    public static Intent newIntent(Context packageContext, String formId) {
        Intent intent = new Intent(packageContext, CreateEditFormActivity.class);
        intent.putExtra(EXTRA_FORM_ID, formId);
        return intent;
    }
}
