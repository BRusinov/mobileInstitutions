package com.inst.mobileinstitutions.Forms.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Form;
import com.inst.mobileinstitutions.Forms.CreateEdit.CreateEditFormActivity;
import com.inst.mobileinstitutions.Forms.Show.FormActivity;
import com.inst.mobileinstitutions.R;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class FormListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mFormRecyclerView;
    private FormAdapter mAdapter;
    private Button mCreateFormButton;

    private void updateUI(Observable<List<Form>> formsObserver) {
        formsObserver.subscribe(new Action1<List<Form>>() {
            @Override
            public void call(List<Form> forms) {
                if (mAdapter == null) {
                    mAdapter = new FormAdapter(forms);
                    mFormRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Observable<List<Form>> forms = APICall.getResource("forms");
        View view = inflater.inflate(R.layout.fragment_form_list, container, false);
        mFormRecyclerView = (RecyclerView) view.findViewById(R.id.form_recycler_view);
        mFormRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(/*institution*/ true){
            mCreateFormButton = (Button) view.findViewById(R.id.form_create_button);
            mCreateFormButton.setVisibility(View.VISIBLE);
            mCreateFormButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(CreateEditFormActivity.newIntent(getActivity(), "newForm"));
                }
            });
        }
        updateUI(forms);
        return view;
    }

    private class FormHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mInstitutionName;
        private String formId;

        public void bindForm(Form form){
            mTitleTextView.setText(form.getName());
            mInstitutionName.setText(form.getInst_name());
            formId = form.getId();
        }

        public FormHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_form_title_view);
            mInstitutionName = (TextView) itemView.findViewById(R.id.list_item_form_institution_view);
        }

        @Override
        public void onClick(View v){
            Intent intent;
            if(false) {
                intent = CreateEditFormActivity.newIntent(getActivity(), formId);
            }else {
                intent = FormActivity.newIntent(getActivity(), formId);
            }
            startActivity(intent);
        }
    }

    private class FormAdapter extends RecyclerView.Adapter<FormHolder>{
        private List<Form> mForms;

        public FormAdapter(List<Form> forms){
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
            Form form = mForms.get(position);
            holder.bindForm(form);
        }

        @Override
        public int getItemCount() {
            return mForms.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();updateUI(APICall.getResource("forms"));
    }
}
