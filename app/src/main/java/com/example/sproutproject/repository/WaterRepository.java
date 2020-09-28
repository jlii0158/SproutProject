package com.example.sproutproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sproutproject.dao.WaterDAO;
import com.example.sproutproject.databse.WaterDatabase;
import com.example.sproutproject.entity.Water;

import java.util.List;

public class WaterRepository {
    private WaterDAO dao;
    private LiveData<List<Water>> allWater;
    private Water water;


    public WaterRepository(Application application){
        WaterDatabase db = WaterDatabase.getInstance(application);
        dao=db.waterDAO();
    }
    public LiveData<List<Water>> getAllWaters() {
        allWater=dao.getAll();
        return allWater;
    }
    public void insert(final Water water){
        WaterDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(water);
            }
        });
    }
    public void deleteAll(){
        WaterDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final Water water){
        WaterDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(water);
            }
        });
    }

    public void insertAll(final Water... waters){
        WaterDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(waters);
            }
        });
    }

}
