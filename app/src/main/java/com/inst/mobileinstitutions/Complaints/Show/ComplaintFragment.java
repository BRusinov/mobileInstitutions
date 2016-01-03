package com.inst.mobileinstitutions.Complaints.Show;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.Models.Complaint;
import com.inst.mobileinstitutions.API.Models.FileField;
import com.inst.mobileinstitutions.API.Models.Fill;
import com.inst.mobileinstitutions.R;

import java.io.File;
import java.util.List;

import rx.functions.Action1;

public class ComplaintFragment extends Fragment {

    private static final String ARG_COMPLAINT_ID = "complaint_id";
    private int PERMISSION_BEGGING_REQUEST_CONSTANT = 238;

    private TextView mComplaintTitle;
    private LinearLayout mComplaintHolder;
    private String mComplaintId;
    private LinearLayout mStatusHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComplaintId = (String) getArguments().getSerializable(ARG_COMPLAINT_ID);
    }

    public static ComplaintFragment newInstance(String complaintId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMPLAINT_ID, complaintId);
        ComplaintFragment fragment = new ComplaintFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_complaint, parent, false);
        mComplaintTitle = (TextView)v.findViewById(R.id.complaint_id_text_field);
        mComplaintHolder = (LinearLayout)v.findViewById(R.id.complaint_holder_layout);
        mStatusHolder = (LinearLayout)v.findViewById(R.id.complaint_status_bar_holder);
        APICall.getResource("complaint", Integer.valueOf(mComplaintId)).subscribe(new Action1<Complaint>() {
            @Override
            public void call(Complaint complaint) {
                printStatus(complaint.getStatusString());
                mComplaintTitle.setText(complaint.getId());
                populateComplaint(complaint.getFills(), complaint.getFiles());
            }
        });
        return v;
    }

    private void printStatus(String status){
        TextView statusText = new TextView(getActivity());
        statusText.setText(status);
        mStatusHolder.addView(statusText);
    }

    private void populateComplaint(List<Fill> fills, List<FileField> files) {
        displayFills(fills);
        displayFiles(files);
    }

    private void displayFills(List<Fill> fills){
        for (Fill fill : fills)
        {
            TextView fillView = new TextView(getActivity());
            fillView.setHint(fill.getField_name());
            fillView.setText(fill.getVal());
            mComplaintHolder.addView(fillView);
        }
    }

    private void displayFiles(List<FileField> files){
        for (FileField file : files){
            Button fileButton = new Button(getActivity());
            fileButton.setText(file.getField_name());
            final File writableFile = file.getFile();
            fileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_BEGGING_REQUEST_CONSTANT);
                    } else {
                        writeFileToSdCard(writableFile);
                    }
                }
            });
        }
    }

    private void writeFileToSdCard(File fileToWrite){
        // TODO write to file
    }


}