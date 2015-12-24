package com.inst.mobileinstitutions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class FormActivity extends SingleFragmentActivity {

    private static final String EXTRA_FORM_ID = "com.inst.android.mobileInstitutions.form_id";

    @Override
    protected Fragment createFragment(){
        String formId = (String) getIntent().getSerializableExtra(EXTRA_FORM_ID);
        return FormFragment.newInstance(formId);
    }

    public static Intent newIntent(Context packageContext, String formId) {
        Intent intent = new Intent(packageContext, FormActivity.class);
        intent.putExtra(EXTRA_FORM_ID, formId);
        return intent;
    }
}
