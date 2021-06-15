package com.example.moneymanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moneymanager.dao.ManagerDao;
import com.example.moneymanager.dao.RecordsDao;
import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;

@Database(entities = {Manager.class, Records.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "MoneyManagerDatabase").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ManagerDao managerDao();
    public abstract RecordsDao recordsDao();
}
