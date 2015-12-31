package com.inst.mobileinstitutions.Profile.Show;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.inst.mobileinstitutions.SingleFragmentActivity;

public class ComplaintActivity extends SingleFragmentActivity {

    private static final String EXTRA_COMPLAINT_ID = "com.inst.android.mobileInstitutions.form_id";

    @Override
    protected Fragment createFragment(){
        String formId = (String) getIntent().getSerializableExtra(EXTRA_COMPLAINT_ID);
        return ComplaintFragment.newInstance(formId);
    }

    public static Intent newIntent(Context packageContext, String formId) {
        Intent intent = new Intent(packageContext, ComplaintActivity.class);
        intent.putExtra(EXTRA_COMPLAINT_ID, formId);
        return intent;
    }
}
