package com.example.sproutproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoritePlant {
    @PrimaryKey(autoGenerate = true)
    public int pid;
    @ColumnInfo(name = "plant_img")
    public String plantImg;
    @ColumnInfo(name = "plant_name")
    public String plantName;

    public FavoritePlant(String plantImg, String plantName) {
        this.plantImg = plantImg;
        this.plantName = plantName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPlantImg() {
        return plantImg;
    }

    public void setPlantImg(String plantImg) {
        this.plantImg = plantImg;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
}
