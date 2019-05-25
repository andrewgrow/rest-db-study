package com.example.restdbstudy.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restdbstudy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер со списком расписания для конкретного дня
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<String> items = new ArrayList<>(); // список предметов в расписании

    /**
     * Получаем предмет по его номеру в списке
     * @param position порядковый номер элемента
     * @return предмет расписания
     */
    public String getItem(int position) {
        return items.get(position);
    }

    /**
     * Устанавливаем список предметов
     * @param items список предметов
     */
    public void setNewItems(List<String> items) {
        final int oldSize = items.size(); // запоминаем старый размер
        this.items = items; // меняем старый список на новый
        notifyItemRangeInserted(oldSize, items.size()); // оповещаем адаптер о том, что старый размер поменялся на новый
    }

    /**
     * Создаём ходлер для элементов
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        // создаём вью
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup,
                false);
        // создаём и возвращаем готовый холдер
        return new ViewHolder(view);
    }

    /**
     * Устанавливаем на вёрстке значения элементов в зависимости от позиции
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // получаем предмет из списка по его позиции
        String lesson = getItem(position);
        // записываем холдеру значение предмета
        viewHolder.tvTitle.setText(lesson);
    }

    /**
     * @return количество предметов в списке
     */
    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Статический класс с вёрсткой элемента
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
