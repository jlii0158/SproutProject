package com.example.sproutproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.sproutproject.entity.Water;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WaterDAO {

    @Query("SELECT * FROM `Water`")
    LiveData<List<Water>> getAll();
    @Query("SELECT * FROM `Water` WHERE water_plan_name = :planName LIMIT 1")
    Water findByName(String planName);
    @Insert
    void insertAll(Water... waters);
    @Insert
    long insert(Water water);
    @Delete
    void delete(Water water);
    @Update(onConflict = REPLACE)
    void updateWaterRecords(Water... waters);
    @Query("DELETE FROM `Water`")
    void deleteAll();
    @Query("DELETE FROM `Water` WHERE water_plan_name=:planName")
    void deleteByName(String planName);
}
