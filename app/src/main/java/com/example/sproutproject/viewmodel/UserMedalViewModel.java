package com.example.sproutproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sproutproject.entity.GetMedal;
import com.example.sproutproject.repository.UserMedalRepository;

import java.util.List;

public class UserMedalViewModel extends ViewModel {

    private UserMedalRepository cRepository;
    private MutableLiveData<List<GetMedal>> allGetMedal;
    public UserMedalViewModel () {
        allGetMedal=new MutableLiveData<>();
    }
    public void setGetMedal(List<GetMedal> getMedals) {
        allGetMedal.setValue(getMedals);
    }
    public LiveData<List<GetMedal>> getAllGetMedal() {
        return cRepository.getAllUserMedal();
    }
    public void initalizeVars(Application application){
        cRepository = new UserMedalRepository(application);
    }
    public void insert(GetMedal getMedal) {
        cRepository.insert(getMedal);
    }

    public GetMedal findByID(int medalId){
        return cRepository.findByID(medalId);
    }
}
