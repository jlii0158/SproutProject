package com.example.sproutproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sproutproject.dao.PlanDAO;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;

import java.util.List;

public class PlanRepository {

    private PlanDAO dao;
    private LiveData<List<Plan>> allPlans;
    private List<Plan> plan;
    public PlanRepository(Application application){
        PlanDatabase db = PlanDatabase.getInstance(application);
        dao=db.planDao();
    }
    public LiveData<List<Plan>> getAllPlans() {
        allPlans=dao.getAll();
        return allPlans;
    }
    public void insert(final Plan plan){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(plan);
            }
        });
    }
    public void deleteAll(){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    /*
    public void delete(final String plan){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteByName(plan);
            }
        });
    }*/

    public void insertAll(final Plan... plans){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(plans);
            }
        });
    }
    public void updateCustomers(final Plan... plans){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updatePlans(plans);
            }
        });
    }

    public List<Plan> findByID(final int plantId){
        PlanDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Plan> runPlant= dao.findByID(plantId);
                setPlant(runPlant);
            }
        });
        return plan;
    }


    public void setPlant(List<Plan> plan){
        this.plan=plan;
    }
}
