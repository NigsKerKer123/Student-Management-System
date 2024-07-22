package com.example.sms.college;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;

import java.util.ArrayList;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeViewHolder>{
    Context context;
    ArrayList<ItemCollege> items;
    CollegeItemListener listener;

    public CollegeAdapter(Context context, ArrayList<ItemCollege> items, CollegeItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CollegeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollegeViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeViewHolder holder, int position) {
        ItemCollege itemCollege = items.get(position);
        holder.collegeName.setText(itemCollege.getCollegeName());
        holder.number.setText(itemCollege.getNumber());
        holder.collegeId.setText(itemCollege.getCollegeId());

        // Set click listener on the cardView
        holder.mainLayout.setOnClickListener(v -> listener.onItemClicked(itemCollege));
        holder.actionButton.setOnClickListener(v -> listener.actionButton(itemCollege));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
