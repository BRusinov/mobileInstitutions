package com.inst.mobileinstitutions.Complaints.Edit;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.inst.mobileinstitutions.Complaints.Show.ComplaintFragment;
import com.inst.mobileinstitutions.SingleFragmentActivity;

public class ComplaintEditActivity extends SingleFragmentActivity {

    private static final String EXTRA_COMPLAINT_ID = "com.inst.android.mobileInstitutions.form_id";

    @Override
    protected Fragment createFragment(){
        String complaintId = (String) getIntent().getSerializableExtra(EXTRA_COMPLAINT_ID);
        return ComplaintEditFragment.newInstance(complaintId);
    }

    public static Intent newIntent(Context packageContext, String complaintId) {
        Intent intent = new Intent(packageContext, ComplaintEditActivity.class);
        intent.putExtra(EXTRA_COMPLAINT_ID, complaintId);
        return intent;
    }
}
