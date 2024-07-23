package com.example.sms.All;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

public class AllViewHolder extends RecyclerView.ViewHolder{
    TextView number;
    TextView name;
    TextView id;
    TextView actionButton;
    ConstraintLayout mainLayout;

    public AllViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.number);
        name = itemView.findViewById(R.id.editRecordEditText);
        id = itemView.findViewById(R.id.recordID);
        actionButton = itemView.findViewById(R.id.actionButton);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}