package com.inst.mobileinstitutions.Forms.List;

import android.support.v4.app.Fragment;

import com.inst.mobileinstitutions.SingleFragmentActivity;

public class FormListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new FormListFragment();
    }
}
