package com.example.sproutproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sproutproject.dao.UserMedalDAO;
import com.example.sproutproject.databse.UserMedalDatabase;
import com.example.sproutproject.entity.GetMedal;

import java.util.List;

public class UserMedalRepository {

    private UserMedalDAO dao;
    private LiveData<List<GetMedal>> allGetMedal;
    private GetMedal getMedal;
    public UserMedalRepository(Application application){
        UserMedalDatabase db = UserMedalDatabase.getInstance(application);
        dao=db.userMedalDAO();
    }
    public LiveData<List<GetMedal>> getAllUserMedal() {
        allGetMedal=dao.getAll();
        return allGetMedal;
    }
    public void insert(final GetMedal getMedal){
        UserMedalDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(getMedal);
            }
        });
    }

    public GetMedal findByID(final int medalId){
        UserMedalDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                GetMedal runUserMedal= dao.findById(medalId);
                setPlant(runUserMedal);
            }
        });
        return getMedal;
    }


    public void setPlant(GetMedal getMedal){
        this.getMedal=getMedal;
    }
}
