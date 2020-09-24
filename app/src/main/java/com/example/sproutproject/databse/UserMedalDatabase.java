package com.example.sproutproject.databse;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sproutproject.dao.UserMedalDAO;
import com.example.sproutproject.entity.GetMedal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GetMedal.class}, version = 2, exportSchema = false)
public abstract class UserMedalDatabase extends RoomDatabase {
    public abstract UserMedalDAO userMedalDAO();
    private static UserMedalDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized UserMedalDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    UserMedalDatabase.class, "UserMedalDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
