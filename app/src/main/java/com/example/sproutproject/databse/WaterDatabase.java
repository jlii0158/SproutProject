package com.example.sproutproject.databse;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sproutproject.dao.WaterDAO;
import com.example.sproutproject.entity.Water;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Water.class}, version = 2, exportSchema = false)
public abstract class WaterDatabase extends RoomDatabase {
    public abstract WaterDAO waterDAO();
    private static WaterDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized WaterDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WaterDatabase.class, "WaterDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
