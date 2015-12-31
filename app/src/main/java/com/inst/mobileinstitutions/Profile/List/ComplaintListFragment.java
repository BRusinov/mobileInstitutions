package com.inst.mobileinstitutions.Profile.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Complaint;
import com.inst.mobileinstitutions.Profile.Show.ComplaintActivity;
import com.inst.mobileinstitutions.R;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class ComplaintListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mComplaintRecyclerView;
    private ComplaintAdapter mAdapter;

    private void updateUI(Observable<List<Complaint>> complaintsObserver) {
        complaintsObserver.subscribe(new Action1<List<Complaint>>() {
            @Override
            public void call(List<Complaint> complaints) {
                if (mAdapter == null) {
                    mAdapter = new ComplaintAdapter(complaints);
                    mComplaintRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Observable<List<Complaint>> complaints = APICall.getResource("complaints");
        View view = inflater.inflate(R.layout.fragment_complaint_list, container, false);
        mComplaintRecyclerView = (RecyclerView) view.findViewById(R.id.complaint_recycler_view);
        mComplaintRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI(complaints);
        return view;
    }

    private class ComplaintHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private String complaintId;

        public void bindComplaint(Complaint complaint){
            complaintId = complaint.getId();
            mTitleTextView.setText(complaint.getId());
        }

        public ComplaintHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_complaint_title_view);
        }

        @Override
        public void onClick(View v){
            Intent intent = ComplaintActivity.newIntent(getActivity(), complaintId);
            startActivity(intent);
        }
    }

    private class ComplaintAdapter extends RecyclerView.Adapter<ComplaintHolder>{
        private List<Complaint> mComplaints;

        public ComplaintAdapter(List<Complaint> complaints){
            mComplaints = complaints;
        }

        @Override
        public ComplaintHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_complaint, parent, false);
            return new ComplaintHolder(view);
        }

        @Override
        public void onBindViewHolder(ComplaintHolder holder, int position){
            Complaint complaint = mComplaints.get(position);
            holder.bindComplaint(complaint);
        }

        @Override
        public int getItemCount() {
            return mComplaints.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();updateUI(APICall.getResource("complaints"));
    }
}
