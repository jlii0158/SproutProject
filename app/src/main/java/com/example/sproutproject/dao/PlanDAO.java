package com.example.sproutproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sproutproject.entity.FavoritePlant;
import com.example.sproutproject.entity.Plan;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlanDAO {

    @Query("SELECT * FROM `Plan`")
    LiveData<List<Plan>> getAll();
    @Query("SELECT * FROM `Plan` WHERE planId = :planId LIMIT 1")
    Plan findByID(int planId);
    @Insert
    void insertAll(Plan... plans);
    @Insert
    long insert(Plan plan);
    @Delete
    void delete(Plan plan);
    @Update(onConflict = REPLACE)
    void updatePlans(Plan... plans);
    @Query("DELETE FROM `Plan`")
    void deleteAll();
    @Query("DELETE FROM `plan` WHERE planId=:planId")
    void deleteById(int planId);
    @Query("SELECT * FROM `Plan`")
    List<Plan> findAllPlan();
}
