package com.example.sms.section;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.course.CourseItemListener;

import java.util.ArrayList;

public class SectionAdapter extends RecyclerView.Adapter<SectionViewHolder>{
    Context context;
    ArrayList<ItemSection> items;
    SectionItemListener listener;

    public SectionAdapter(Context context, ArrayList<ItemSection> items, SectionItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        ItemSection itemSection = items.get(position);
        holder.sectionName.setText(itemSection.getSectionName());
        holder.number.setText(itemSection.getNumber());
        holder.sectionId.setText(itemSection.getSectionId());

        holder.mainLayout.setOnClickListener(v -> listener.onItemClicked(itemSection));
        holder.actionButton.setOnClickListener(v -> listener.actionButton(itemSection));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
