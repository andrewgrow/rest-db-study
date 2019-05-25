package com.example.restdbstudy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restdbstudy.R;
import com.example.restdbstudy.models.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер со списком дней
 */
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private List<Day> items = new ArrayList<>(); // список дней
    private OnClickListener clickListener; // глобальный слушательно нажатий (устанавливается через активность)


    /**
     * Получение элемента адаптера по его позиции в адаптере
     * @param position порядковый номер элемента в списке
     * @return день из списка
     */
    public Day getItem(int position) {
        return items.get(position);
    }

    /**
     * Интерфейс глобального слушателя нажатий
     */
    public interface OnClickListener {
        void onItemClicked(int position);
    }

    /**
     * Устанавливаем глобальный слушатель нажатий
     * @param clickListener объект, который реализует интерфейс слушателя
     */
    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Устанавливаем в список новые элементы
     * @param items список элементов
     */
    public void setNewItems(List<Day> items) {
        final int oldSize = items.size(); // запоминаем старый размер
        this.items = items; // сохраняем новый список
        notifyItemRangeInserted(oldSize, items.size()); // оповещаем адаптер, что размер изменился со старого на новый
    }

    /**
     * Создание холдера для элемента
     * @param viewGroup родительское вью
     * @param typeView тип вью
     * @return готовый вью холдер для элемента
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int typeView) {
        Context context = viewGroup.getContext(); // получаем контекст из родительской вью
        LayoutInflater inflater = LayoutInflater.from(context); // получаем инфлейтер из контекста
        int layout = R.layout.item_schedule; // файл вёрстки
        // инфлейтим (получаем) вью из файла вёрстки
        View view = inflater.inflate(layout, viewGroup, false);
        // создаём холдер из нашей вью. На вход также передаём глобальный слушатель нажатий
        ViewHolder result = new ViewHolder(view, clickListener);
        // возвращаем готовый холдер
        return result;
    }

    /**
     * Присоединение холдера к отображаемому элементу
     * @param viewHolder созданный ранее готовый холдер
     * @param position порядковый номер элемента к которому присоединяется этот холдер
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Day day = getItem(position); // получаем день по номеру позиции
        viewHolder.tvTitle.setText(day.getNameOfDay()); // устанавливаем элементу текст, полученный из дня
    }

    /**
     * @return количество элементов в списке
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Статический класс который хранит в себе данные по элементам вёрстки
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle; // текстовое поле заголовка
        public ViewHolder(@NonNull View itemView, OnClickListener clickListener) {
            super(itemView);
            // ищем элементы
            tvTitle = itemView.findViewById(R.id.tvTitle);
            // устанавливаем элементам слушатель нажатий, который будет вызывать при нажатии
            // глобальный слушатель
            itemView.setOnClickListener(clickedView -> {
                if (clickListener != null) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
