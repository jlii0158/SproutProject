package com.example.sproutproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sproutproject.entity.Water;
import com.example.sproutproject.repository.WaterRepository;

import java.util.List;

public class WaterViewModel extends ViewModel{

    private WaterRepository cRepository;
    private MutableLiveData<List<Water>> allWaters;
    public WaterViewModel () {
        allWaters=new MutableLiveData<>();
    }
    public void setWaters(List<Water> waters) {
        allWaters.setValue(waters);
    }
    public LiveData<List<Water>> getAllWaters() {
        return cRepository.getAllWaters();
    }
    public void initalizeVars(Application application){
        cRepository = new WaterRepository(application);
    }
    public void insert(Water water) {
        cRepository.insert(water);
    }
    public void insertAll(Water... waters) {
        cRepository.insertAll(waters);
    }
    public void deleteAll() {
        cRepository.deleteAll();
    }

    public void delete(Water water) {
        cRepository.delete(water);
    }

}
