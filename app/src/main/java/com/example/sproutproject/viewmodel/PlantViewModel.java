package com.example.sproutproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sproutproject.entity.FavoritePlant;
import com.example.sproutproject.repository.PlantRepository;

import java.util.List;

public class PlantViewModel extends ViewModel {

    private PlantRepository cRepository;
    private MutableLiveData<List<FavoritePlant>> allPlants;
    public PlantViewModel () {
        allPlants=new MutableLiveData<>();
    }
    public void setPlants(List<FavoritePlant> plants) {
        allPlants.setValue(plants);
    }
    public LiveData<List<FavoritePlant>> getAllPlants() {
        return cRepository.getAllPlants();
    }
    public void initalizeVars(Application application){
        cRepository = new PlantRepository(application);
    }
    public void insert(FavoritePlant plant) {
        cRepository.insert(plant);
    }
    public void insertAll(FavoritePlant... plants) {
        cRepository.insertAll(plants);
    }
    public void deleteAll() {
        cRepository.deleteAll();
    }
    public void delete(String plant) {
        cRepository.delete(plant);
    }
    public void update(FavoritePlant... plants) {
        cRepository.updateCustomers(plants);
    }

    public List<FavoritePlant> findByName(String plantName){
        return cRepository.findByName(plantName);
    }

}
