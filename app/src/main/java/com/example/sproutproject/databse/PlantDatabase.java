package com.example.sproutproject.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sproutproject.dao.PlantDAO;
import com.example.sproutproject.entity.FavoritePlant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FavoritePlant.class}, version = 2, exportSchema = false)
public abstract class PlantDatabase extends RoomDatabase {
    public abstract PlantDAO plantDao();
    private static PlantDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized PlantDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PlantDatabase.class, "PlantDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
