package com.inst.mobileinstitutions.Complaints.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.inst.mobileinstitutions.API.APICall;
import com.inst.mobileinstitutions.API.APICredentials;
import com.inst.mobileinstitutions.API.Models.Complaint;
import com.inst.mobileinstitutions.Complaints.Edit.ComplaintEditActivity;
import com.inst.mobileinstitutions.Complaints.Show.ComplaintActivity;
import com.inst.mobileinstitutions.R;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class ComplaintListFragment extends android.support.v4.app.Fragment {
    private TableLayout mComplaintsTable;
    private Observable<List<Complaint>> mObservableComplaints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_complaint_list, container, false);
        mObservableComplaints = APICall.getResource("complaints");
        mComplaintsTable = (TableLayout) view.findViewById(R.id.complaint_list_table);
        populateTable();
        return view;
    }

    private void populateTable(){
        addRow("", "Дата", "Статус");
        mObservableComplaints.subscribe(new Action1<List<Complaint>>() {
            @Override
            public void call(List<Complaint> complaints) {
                for (Complaint complaint : complaints) {
                    addRow(complaint.getId(), complaint.getCreated_at(), complaint.getStatusString());
                }
            }
        });
    }

    private void addRow(final String cid, String cdate, String cstatus){
        TableRow complaintTableRow = new TableRow(getActivity());
        addToTableRow(complaintTableRow, cid);
        addToTableRow(complaintTableRow, cdate);
        addToTableRow(complaintTableRow, cstatus);
        complaintTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (APICredentials.getLoggedUser() != null && APICredentials.getLoggedUser().isInstitution() && !cid.isEmpty()) {
                    intent = ComplaintEditActivity.newIntent(getActivity(), cid);
                } else {
                    intent = ComplaintActivity.newIntent(getActivity(), cid);
                }
                startActivity(intent);
            }
        });
        mComplaintsTable.addView(complaintTableRow);
    }

    private void addToTableRow(TableRow complaintTableRow, String data){
        TextView rowData = new TextView(getActivity());
        rowData.setText(data);
        complaintTableRow.addView(rowData);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
