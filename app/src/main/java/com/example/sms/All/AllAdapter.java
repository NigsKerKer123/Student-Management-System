package com.example.sms.All;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.student.ItemStudent;
import com.example.sms.student.StudentItemListener;
import com.example.sms.student.StudentViewHolder;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllViewHolder>{
    Context context;
    ArrayList<ItemAll> items;
    AllItemListener listener;

    public AllAdapter(AllItemListener listener, ArrayList<ItemAll> items, Context context) {
        this.listener = listener;
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {
        ItemAll itemAll = items.get(position);
        holder.name.setText(itemAll.getName());
        holder.number.setText(itemAll.getNumber());
        holder.id.setText(itemAll.getId());

        holder.mainLayout.setOnClickListener(v -> listener.onItemClicked(itemAll));
        //holder.actionButton.setOnClickListener(v -> listener.actionButton(itemAll));

        holder.actionButton.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
