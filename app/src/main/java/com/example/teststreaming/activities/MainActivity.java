package com.example.teststreaming.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.teststreaming.R;
import com.example.teststreaming.adapters.DaysAdapter;
import com.example.teststreaming.adapters.ScheduleAdapter;
import com.example.teststreaming.models.Day;
import com.example.teststreaming.retrofit.Rest;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private DaysAdapter daysAdapter;
    private boolean isScheduleShowing;

    public interface ScheduleCallback {
        void onCall(List<Day> dayList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                RecyclerView.VERTICAL, false));

        Rest.getAllDays(callback);
    }

    private ScheduleCallback callback = new ScheduleCallback() {
        @Override
        public void onCall(List<Day> dayList) {
            if (daysAdapter == null) {
                daysAdapter = new DaysAdapter();
            }
            daysAdapter.setNewItems(dayList);
            daysAdapter.setClickListener(adapterItemClickListener);
            recyclerView.setAdapter(daysAdapter);
        }
    };

    private DaysAdapter.OnClickListener adapterItemClickListener = new DaysAdapter.OnClickListener() {
        @Override
        public void onItemClicked(int position) {
            Day day = daysAdapter.getItem(position);
            List<String> schedules = day.getListSchedule();
            ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
            scheduleAdapter.setNewItems(schedules);
            recyclerView.setAdapter(scheduleAdapter);
            isScheduleShowing = true;
            enableBackButton(true, day.getNameOfDay());
        }
    };

    private void enableBackButton(boolean isEnabled, String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
        getSupportActionBar().setDisplayShowHomeEnabled(isEnabled);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (isScheduleShowing) {
            recyclerView.setAdapter(daysAdapter);
            enableBackButton(false, getString(R.string.app_name));
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
