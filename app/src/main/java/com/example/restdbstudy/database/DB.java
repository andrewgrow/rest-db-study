package com.example.restdbstudy.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.example.restdbstudy.App;
import com.example.restdbstudy.models.Day;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DB {

    private static final String TAG = DB.class.getSimpleName();
    private static DB instance;
    private final Database db;

    public static DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public DB() {
        // см. работу с базой данных на уроке в стартандроид
        // https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/529-urok-5-room-osnovy.html
        Context context = App.getContext();
        db =  Room.databaseBuilder(context,
                Database.class, "database")
                .build();
    }

    public static Flowable<List<Day>> getAllDays() {
        Log.d(TAG, "call getAllDays()");
        return getInstance().db.getDAO().getAllDays();
    }

    /**
     * Сохранение списка дней в базу данных.
     * @param dayList
     */
    @SuppressLint("CheckResult")
    public static void insertOrUpdateIfExists(List<Day> dayList) {
        Log.d(TAG, "callinsertOrUpdateIfExists()");
        Observable.fromCallable(new CallableUpdate(dayList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "callinsertOrUpdateIfExists() - success!!!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    private static class CallableUpdate implements Callable<Boolean> {

        private final List<Day> dayList;

        public CallableUpdate(List<Day> dayList) {
            this.dayList = dayList;
        }

        @Override
        public Boolean call() throws Exception {
            if (dayList == null || dayList.size() == 0) {
                return false;
            }

            // получаем объект базы данных
            Database db = getInstance().db;
            // запрашиваем у базы нужный ДАО
            IDatabaseAccessObject dao = db.getDAO();
            // перебираем все дни и вставляем каждый в базу
            for (Day day : dayList) {
                String name = day.getNameOfDay();
                Day dbDay = dao.getDayByName(name);
                if (dbDay == null) {
                    dao.insert(day);
                } else {
                    dao.update(day);
                }
            }

            return true;
        }
    }
}
