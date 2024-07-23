package com.example.sms.student;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

public class StudentViewHolder extends RecyclerView.ViewHolder{
    TextView number;
    TextView studentName;
    TextView studentId;
    TextView actionButton;
    ConstraintLayout mainLayout;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.number);
        studentName = itemView.findViewById(R.id.editRecordEditText);
        studentId = itemView.findViewById(R.id.recordID);
        actionButton = itemView.findViewById(R.id.actionButton);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}
