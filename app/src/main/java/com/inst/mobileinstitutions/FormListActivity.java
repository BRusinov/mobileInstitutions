package com.inst.mobileinstitutions;

import android.support.v4.app.Fragment;

public class FormListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new FormListFragment();
    }
}
