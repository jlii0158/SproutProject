package com.example.sproutproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GetMedal {
    @PrimaryKey(autoGenerate = true)
    public int mid;
    @ColumnInfo(name = "medal_id")
    public int medalId;
    @ColumnInfo(name = "user_account")
    public String userAccount;
    @ColumnInfo(name = "medel_name")
    public String medalName;
    @ColumnInfo(name = "medal_desc")
    public String medalDesc;
    @ColumnInfo(name = "grow_vale")
    public int growValue;
    @ColumnInfo(name = "medal_image")
    public String medalImage;
    @ColumnInfo(name = "medal_image_grey")
    public String medalImageGrey;



    public GetMedal(int medalId, String userAccount, String medalName, String medalDesc, int growValue, String medalImage, String medalImageGrey) {
        this.medalId = medalId;
        this.userAccount = userAccount;
        this.medalName = medalName;
        this.medalDesc = medalDesc;
        this.growValue = growValue;
        this.medalImage = medalImage;
        this.medalImageGrey = medalImageGrey;
    }

    public int getMedalId() {
        return medalId;
    }

    public void setMedalId(int medalId) {
        this.medalId = medalId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public String getMedalDesc() {
        return medalDesc;
    }

    public void setMedalDesc(String medalDesc) {
        this.medalDesc = medalDesc;
    }

    public int getGrowValue() {
        return growValue;
    }

    public void setGrowValue(int growValue) {
        this.growValue = growValue;
    }

    public String getMedalImage() {
        return medalImage;
    }

    public void setMedalImage(String medalImage) {
        this.medalImage = medalImage;
    }

    public String getMedalImageGrey() {
        return medalImageGrey;
    }

    public void setMedalImageGrey(String medalImageGrey) {
        this.medalImageGrey = medalImageGrey;
    }
}
