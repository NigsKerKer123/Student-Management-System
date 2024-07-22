package com.example.sms.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.college.ItemCollege;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    Context context;
    ArrayList<ItemCourse> items;
    CourseItemListener listener;

    public CourseAdapter(Context context, ArrayList<ItemCourse> items, CourseItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        ItemCourse itemCourse = items.get(position);
        holder.courseName.setText(itemCourse.getCourseName());
        holder.number.setText(itemCourse.getNumber());
        holder.courseId.setText(itemCourse.getCourseId());

        // Set click listener on the cardView
        holder.mainLayout.setOnClickListener(v -> listener.onItemClicked(itemCourse));
        holder.actionButton.setOnClickListener(v -> listener.actionButton(itemCourse));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
