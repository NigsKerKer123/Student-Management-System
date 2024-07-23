package com.example.sms.college;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

public class CollegeViewHolder extends RecyclerView.ViewHolder {
    TextView number;
    TextView collegeName;
    TextView collegeId;
    TextView actionButton;
    ConstraintLayout mainLayout;

    public CollegeViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.number);
        collegeName = itemView.findViewById(R.id.editRecordEditText);
        collegeId = itemView.findViewById(R.id.recordID);
        actionButton = itemView.findViewById(R.id.actionButton);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}
