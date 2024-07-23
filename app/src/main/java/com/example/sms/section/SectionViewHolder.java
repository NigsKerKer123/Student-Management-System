package com.example.sms.section;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

public class SectionViewHolder extends RecyclerView.ViewHolder{
    TextView number;
    TextView sectionName;
    TextView sectionId;
    TextView actionButton;
    ConstraintLayout mainLayout;

    public SectionViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.number);
        sectionName = itemView.findViewById(R.id.editRecordEditText);
        sectionId = itemView.findViewById(R.id.recordID);
        actionButton = itemView.findViewById(R.id.actionButton);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}
