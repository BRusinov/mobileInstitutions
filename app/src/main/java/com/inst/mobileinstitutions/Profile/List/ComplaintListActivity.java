package com.inst.mobileinstitutions.Profile.List;

import android.support.v4.app.Fragment;

import com.inst.mobileinstitutions.SingleFragmentActivity;

public class ComplaintListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new ComplaintListFragment();
    }
}
