package com.example.sms.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>{
    Context context;
    ArrayList<ItemStudent> items;
    StudentItemListener listener;

    public StudentAdapter(Context context, ArrayList<ItemStudent> items, StudentItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        ItemStudent itemStudent = items.get(position);
        holder.studentName.setText(itemStudent.getStudentName());
        holder.number.setText(itemStudent.getNumber());
        holder.studentId.setText(itemStudent.getStudentId());

        holder.mainLayout.setOnClickListener(v -> listener.onItemClicked(itemStudent));
        holder.actionButton.setOnClickListener(v -> listener.actionButton(itemStudent));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
