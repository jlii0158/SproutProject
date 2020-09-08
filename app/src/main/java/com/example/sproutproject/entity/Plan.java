package com.example.sproutproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Plan {

    @PrimaryKey(autoGenerate = true)
    public int planId;
    @ColumnInfo(name = "plan_name")
    public String planName;
    @ColumnInfo(name = "start_date")
    public String startDate;
    @ColumnInfo(name = "end_date")
    public String endDate;
    @ColumnInfo(name = "water_count")
    public int waterCount;
    @ColumnInfo(name = "water_state")
    public int waterState;
    @ColumnInfo(name = "plant_name")
    public String plantName;
    @ColumnInfo(name = "plant_img")
    public String plantImg;
    @ColumnInfo(name = "real_water_count")
    public int realWaterCount;
    @ColumnInfo(name = "plan_background")
    public String planBackground;

    public Plan(String planName, String startDate, String endDate, int waterCount, int waterState, String plantName, String plantImg, int realWaterCount) {
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.waterCount = waterCount;
        this.waterState = waterState;
        this.plantName = plantName;
        this.plantImg = plantImg;
        this.realWaterCount = realWaterCount;
    }

    public Plan() {
    }

    public String getPlanBackground() {
        return planBackground;
    }

    public void setPlanBackground(String planBackground) {
        this.planBackground = planBackground;
    }

    public int getRealWaterCount() {
        return realWaterCount;
    }

    public void setRealWaterCount(int realWaterCount) {
        this.realWaterCount = realWaterCount;
    }

    public String getPlantImg() {
        return plantImg;
    }

    public void setPlantImg(String plantImg) {
        this.plantImg = plantImg;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getWaterCount() {
        return waterCount;
    }

    public void setWaterCount(int waterCount) {
        this.waterCount = waterCount;
    }

    public int getWaterState() {
        return waterState;
    }

    public void setWaterState(int waterState) {
        this.waterState = waterState;
    }

    public String getPlanPlantName() {
        return plantName;
    }

    public void setPlanPlantName(String plantName) {
        this.plantName = plantName;
    }
}
