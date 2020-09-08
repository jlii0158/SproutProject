package com.example.sproutproject.database_entity;

import java.io.Serializable;

public class PlanDisplay implements Serializable {
    public int planId;
    public String planName;
    public String startDate;
    public String endDate;
    public int waterCount;
    public int realWaterCount;
    public int waterState;
    public String plant_name;
    public String plant_img;
    public String daysToCurrentDate;
    public String planBackground;

    public PlanDisplay() {
    }

    public PlanDisplay(int planId, String planName, String startDate, String endDate, int waterCount, int realWaterCount, int waterState, String plant_name, String plant_img, String daysToCurrentDate) {
        this.planId = planId;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.waterCount = waterCount;
        this.realWaterCount = realWaterCount;
        this.waterState = waterState;
        this.plant_name = plant_name;
        this.plant_img = plant_img;
        this.daysToCurrentDate = daysToCurrentDate;
    }

    public String getPlanBackground() {
        return planBackground;
    }

    public void setPlanBackground(String planBackground) {
        this.planBackground = planBackground;
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

    public int getRealWaterCount() {
        return realWaterCount;
    }

    public void setRealWaterCount(int realWaterCount) {
        this.realWaterCount = realWaterCount;
    }

    public int getWaterState() {
        return waterState;
    }

    public void setWaterState(int waterState) {
        this.waterState = waterState;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_img() {
        return plant_img;
    }

    public void setPlant_img(String plant_img) {
        this.plant_img = plant_img;
    }

    public String getDaysToCurrentDate() {
        return daysToCurrentDate;
    }

    public void setDaysToCurrentDate(String daysToCurrentDate) {
        this.daysToCurrentDate = daysToCurrentDate;
    }
}
