package com.example.sproutproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sproutproject.dao.PlantDAO;
import com.example.sproutproject.databse.PlantDatabase;
import com.example.sproutproject.entity.FavoritePlant;

import java.util.List;

public class PlantRepository {
    private PlantDAO dao;
    private LiveData<List<FavoritePlant>> allPlants;
    private List<FavoritePlant> favoritePlant;
    public PlantRepository(Application application){
        PlantDatabase db = PlantDatabase.getInstance(application);
        dao=db.plantDao();
    }
    public LiveData<List<FavoritePlant>> getAllPlants() {
        allPlants=dao.getAll();
        return allPlants;
    }
    public void insert(final FavoritePlant favoritePlant){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(favoritePlant);
            }
        });
    }
    public void deleteAll(){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final String plant){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteByName(plant);
            }
        });
    }

    public void insertAll(final FavoritePlant... favoritePlants){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(favoritePlants);
            }
        });
    }
    public void updateCustomers(final FavoritePlant... favoritePlants){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateCustomers(favoritePlants);
            }
        });
    }

    public List<FavoritePlant> findByName(final String plantName){
        PlantDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<FavoritePlant> runPlant= dao.findByName(plantName);
                setPlant(runPlant);
            }
        });
        return favoritePlant;
    }


    public void setPlant(List<FavoritePlant> favoritePlant){
        this.favoritePlant=favoritePlant;
    }
}
