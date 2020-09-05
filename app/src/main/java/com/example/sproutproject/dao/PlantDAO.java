package com.example.sproutproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sproutproject.entity.FavoritePlant;
import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface PlantDAO {

    @Query("SELECT * FROM favoriteplant")
    LiveData<List<FavoritePlant>> getAll();
    @Query("SELECT * FROM favoriteplant WHERE plant_name = :plantName LIMIT 1")
    List<FavoritePlant> findByName(String plantName);
    @Insert
    void insertAll(FavoritePlant... plants);
    @Insert
    long insert(FavoritePlant plant);
    @Delete
    void delete(FavoritePlant plant);
    @Update(onConflict = REPLACE)
    void updateCustomers(FavoritePlant... plants);
    @Query("DELETE FROM favoriteplant")
    void deleteAll();
    @Query("DELETE FROM favoriteplant WHERE plant_name=:plantName")
    void deleteByName(String plantName);
}
