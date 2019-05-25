package com.example.teststreaming.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teststreaming.R;
import com.example.teststreaming.models.Day;

import java.util.ArrayList;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private List<Day> items = new ArrayList<>();
    private OnClickListener clickListener;

    public Day getItem(int position) {
        return items.get(position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setNewItems(List<Day> items) {
        final int oldSize = items.size();
        this.items = items;
        notifyItemRangeInserted(oldSize, items.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int typeView) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup,
                false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Day day = items.get(position);
        viewHolder.tvTitle.setText(day.getNameOfDay());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView, OnClickListener clickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(clickedView -> {
                if (clickListener != null) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
