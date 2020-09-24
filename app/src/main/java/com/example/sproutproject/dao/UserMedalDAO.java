package com.example.sproutproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sproutproject.entity.GetMedal;
import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface UserMedalDAO {
    @Query("SELECT * FROM getmedal")
    LiveData<List<GetMedal>> getAll();

    @Query("SELECT * FROM GetMedal WHERE medal_id = :medalId LIMIT 1")
    GetMedal findById(int medalId);

    @Insert
    void insertAll(GetMedal... getMedals);

    @Insert
    long insert(GetMedal getMedal);

    @Delete
    void delete(GetMedal getMedal);

    @Update(onConflict = REPLACE)
    void updateCustomers(GetMedal... getMedals);

    @Query("DELETE FROM GetMedal")
    void deleteAll();
}
