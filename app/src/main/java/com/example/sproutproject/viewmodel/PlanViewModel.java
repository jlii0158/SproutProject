package com.example.sproutproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.repository.PlanRepository;

import java.util.List;

public class PlanViewModel extends ViewModel {

    private PlanRepository cRepository;
    private MutableLiveData<List<Plan>> allPlans;
    public PlanViewModel () {
        allPlans=new MutableLiveData<>();
    }
    public void setPlans(List<Plan> plans) {
        allPlans.setValue(plans);
    }
    public LiveData<List<Plan>> getAllPlans() {
        return cRepository.getAllPlans();
    }
    public void initalizeVars(Application application){
        cRepository = new PlanRepository(application);
    }
    public void insert(Plan plan) {
        cRepository.insert(plan);
    }
    public void insertAll(Plan... plans) {
        cRepository.insertAll(plans);
    }
    public void deleteAll() {
        cRepository.deleteAll();
    }
    /*
    public void delete(String plan) {
        cRepository.delete(plan);
    }

     */
    public void update(Plan... plans) {
        cRepository.updateCustomers(plans);
    }

    public List<Plan> findByID(int planName){
        return cRepository.findByID(planName);
    }
}
