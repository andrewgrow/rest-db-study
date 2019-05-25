package com.example.restdbstudy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.restdbstudy.R;
import com.example.restdbstudy.adapters.DaysAdapter;
import com.example.restdbstudy.adapters.ScheduleAdapter;
import com.example.restdbstudy.models.Day;
import com.example.restdbstudy.retrofit.Rest;

import java.util.List;

/**
 * Основной экран.
 * Отображает на экране список расписаний.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName(); // тег для логов
    private RecyclerView recyclerView; // основной список. Может отображать неделю, может только день.
    private DaysAdapter daysAdapter; // адаптер с днями
    private boolean isDayScheduleShowing; // флаг, который показывает, показывается ли сейчас расписание конкретного дня


    /**
     * Получает список расписания для конкретного дня
     */
    public interface ScheduleCallback {
        void onCall(List<Day> dayList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // добавляем вёрстку экрана

        // находим все элементы
        recyclerView = findViewById(R.id.recyclerView);
        // для списка устанавливается вертикальный менеджер
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                RecyclerView.VERTICAL, false));

        // получаем список раписаний на все дни
        Rest.getAllDays(callback);
    }


    /**
     * Коллбек в который придёт расписание для дня.
     */
    private ScheduleCallback callback = new ScheduleCallback() {
        @Override
        public void onCall(List<Day> dayList) {
            // если адаптер для дня пустой, то создаём его
            if (daysAdapter == null) {
                daysAdapter = new DaysAdapter();
            }

            // обновляем элементы в адаптере
            daysAdapter.setNewItems(dayList);
            // устанавливаем слушатель нажатий
            daysAdapter.setClickListener(adapterItemClickListener);
            // устанавливаем списку адаптер
            recyclerView.setAdapter(daysAdapter);
        }
    };

    /**
     * Коллбек для определения клика по элементу адаптера.
     */
    private DaysAdapter.OnClickListener adapterItemClickListener = new DaysAdapter.OnClickListener() {
        @Override
        public void onItemClicked(int position) {
            Day day = daysAdapter.getItem(position); // получаем день из элемента
            List<String> schedules = day.getListSchedule(); // достаём расписание дня
            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(); // создаём адаптер для конкретного дня
            scheduleAdapter.setNewItems(schedules); // вносим в адаптер элементы
            recyclerView.setAdapter(scheduleAdapter); // заменяем у списка адаптер дней на адаптер расписания конкретного дня
            isDayScheduleShowing = true; // меняем флаг
            enableBackButton(true, day.getNameOfDay()); // меняем текст у кнопки назад + показываем кнопку
        }
    };


    /**
     * Меняем текст у кнопки назад + показываем кнопку
     * @param isEnabled true если нужно показать кнопку, false если нужно спрятать
     * @param title заголовок который отображается возле кнопки назад
     */
    private void enableBackButton(boolean isEnabled, String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
        getSupportActionBar().setDisplayShowHomeEnabled(isEnabled);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        // если флаг говорит о том, то сейчас показывается расписание конкретного дня,
        // а не общее, то мы просто возвращаем на место адаптер дней
        // и меняем данные у кнопки назад
        if (isDayScheduleShowing) {
            recyclerView.setAdapter(daysAdapter);
            enableBackButton(false, getString(R.string.app_name));
            return;
        }
        // иначе выполняем стандартное действие кнопки назад
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // переопределённый метод для кнопки навигации это вызов метода кнопки назад
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
