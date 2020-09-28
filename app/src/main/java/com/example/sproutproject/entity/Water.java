package com.example.sproutproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Water {
    @PrimaryKey(autoGenerate = true)
    public int wid;
    @ColumnInfo(name = "water_plan_name")
    public String planName;
    @ColumnInfo(name = "water_date")
    public String waterDate;

    public Water(String planName, String waterDate) {
        this.planName = planName;
        this.waterDate = waterDate;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(String waterDate) {
        this.waterDate = waterDate;
    }
}
