package com.inst.mobileinstitutions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class FormListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mFormRecyclerView;
    private FormAdapter mAdapter;

    private void updateUI() {
        String [] forms = {"MVR", "LI", "PNG"};
        if(mAdapter == null) {
            mAdapter = new FormAdapter(forms);
            mFormRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_form_list, container, false);
        mFormRecyclerView = (RecyclerView) view.findViewById(R.id.form_recycler_view);
        mFormRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private class FormHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private String mInstitutionName;

        public void bindCrime(String mInstitutionName){
            mTitleTextView.setText(mInstitutionName);
        }

        public FormHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_form_title_view);
        }

        @Override
        public void onClick(View v){
            UUID  id = UUID.randomUUID();
            Intent intent = FormActivity.newIntent(getActivity(), id);
            startActivity(intent);
        }
    }

    private class FormAdapter extends RecyclerView.Adapter<FormHolder>{
        private String [] mForms;

        public FormAdapter(String [] forms){
            mForms = forms;
        }

        @Override
        public FormHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_form, parent, false);
            return new FormHolder(view);
        }

        @Override
        public void onBindViewHolder(FormHolder holder, int position){
            String form = mForms[position];
            holder.bindCrime(form);
        }

        @Override
        public int getItemCount() {
            return mForms.length;
        }
    }

    @Override
    public void onResume() {
        super.onResume();updateUI();
    }
}
