package com.example.sms.course;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

public class CourseViewHolder extends RecyclerView.ViewHolder{
    TextView number;
    TextView courseName;
    TextView courseId;
    TextView actionButton;
    ConstraintLayout mainLayout;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.number);
        courseName = itemView.findViewById(R.id.editRecordEditText);
        courseId = itemView.findViewById(R.id.collegeId);
        actionButton = itemView.findViewById(R.id.actionButton);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}
