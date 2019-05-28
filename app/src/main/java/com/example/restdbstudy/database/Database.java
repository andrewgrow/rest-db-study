package com.example.restdbstudy.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.example.restdbstudy.App;
import com.example.restdbstudy.activities.MainActivity;
import com.example.restdbstudy.models.Day;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Объект для работы с базой данных.
 */
public class Database {

    private static final String TAG = Database.class.getSimpleName();
    private static Database instance; // синглтон этого класса
    private final DatabaseManager databaseManager; // абстрактный класс, в котором описываются все Database Access Objects


    /**
     * @return синглтон
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }


    /**
     * В конструкторе мы берём контекст от апп и создаём databaseManager для работы с ДАО-объектами.
     *
     * Конструктор приватный, то есть создать его экземпляр больше никто не может.
     * Это нужно для реализации паттерна "Синглтон (Одиночка)".
     */
    private Database() {
        // см. работу с базой данных на уроке в стартандроид
        // https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/529-urok-5-room-osnovy.html
        Context context = App.getContext();
        databaseManager =  Room.databaseBuilder(context,
                DatabaseManager.class, "database")
                .build();
    }


    /**
     * Метод для получения списка всех дней из базы данных.
     *
     * @param callback на вход получает коллбек, которому мы сообщим о результатах запроса в базу.
     */
    @SuppressLint("CheckResult")
    public static void getAllDays(MainActivity.ScheduleCallback callback) {
        Log.d(TAG, "call getAllDays()");

        // сходим в базу данных и получим список всех дней

        getDao().getAllDays()
                .subscribeOn(Schedulers.io()) // после подписки необходимо работать в потоке IO
                .observeOn(AndroidSchedulers.mainThread()) // наблюдение за результатами производится в main потоке
                .subscribe(dayList -> {
                    // готовый результат сообщим коллбеку, если он не пустой
                    if (callback != null) {
                        callback.onCall(dayList);
                    }
                }, throwable -> {
                    // если произошла ошибка, то покажем это в логе
                    Log.e(TAG, throwable.getMessage());
                    throwable.printStackTrace();
                });
    }


    /**
     * Сохранение списка дней в базу данных.
     *
     * @param dayList на вход получаем список дней, которые надо сохранить.
     */
    @SuppressLint("CheckResult")
    public static void insertOrUpdateIfExists(List<Day> dayList) {
        Log.d(TAG, "callinsertOrUpdateIfExists()");

        if (dayList == null || dayList.size() == 0) {
            return;
        }

        // создаём поток
        // из списка достаём каждый элемент
        // и обрабатываем его

        Observable
                .fromIterable(dayList)
                .map(day -> {
                    String name = day.getNameOfDay();
                    Log.d(TAG, "сохраняем в базу данных день " + name);

                    // достанем день из базы данных
                    Day dayFromDatabase = getDao().getDayByName(name);

                    // если такого дня не существует, значит вставим новый (insert)
                    // иначе обновим (update)
                    if (dayFromDatabase == null) {
                        getDao().insert(day);
                    } else {
                        getDao().update(day);
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io()) // после подписки необходимо работать в потоке IO
                .observeOn(AndroidSchedulers.mainThread()) // наблюдение за результатами производится в мейн потоке
                .subscribe();
    }


    /**
     * @return метод возвращает Database Access Object для непосредственного выполнения описанных в нём методов
     */
    private static IDatabaseAccessObject getDao() {
        // Чтобы получить ДАО, нам нужно обратиться к менеджеру
        DatabaseManager manager = getInstance().databaseManager;
        // и попросить его вернуть ДАО
        return manager.getDAO();
    }
}
