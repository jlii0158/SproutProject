package com.example.sproutproject.database_entity;

import java.io.Serializable;

public class Plant implements Serializable {

    private String plant_name;
    private String plant_nickname;
    private String plant_sow_ins;
    private String plant_space_ins;
    private String growth_cycle;
    private String plant_harvest_ins;
    private String comp_plant;
    private String water_need;
    private String plant_desc;
    private String plant_img;
    private String harvest_week;

    public Plant() {
    }

    public Plant(String plant_name, String plant_nickname, String plant_sow_ins, String plant_space_ins, String growth_cycle, String plant_harvest_ins, String comp_plant, String water_need, String plant_desc, String plant_img, String harvest_week) {
        this.plant_name = plant_name;
        this.plant_nickname = plant_nickname;
        this.plant_sow_ins = plant_sow_ins;
        this.plant_space_ins = plant_space_ins;
        this.growth_cycle = growth_cycle;
        this.plant_harvest_ins = plant_harvest_ins;
        this.comp_plant = comp_plant;
        this.water_need = water_need;
        this.plant_desc = plant_desc;
        this.plant_img = plant_img;
        this.harvest_week = harvest_week;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_nickname() {
        return plant_nickname;
    }

    public void setPlant_nickname(String plant_nickname) {
        this.plant_nickname = plant_nickname;
    }

    public String getPlant_sow_ins() {
        return plant_sow_ins;
    }

    public void setPlant_sow_ins(String plant_sow_ins) {
        this.plant_sow_ins = plant_sow_ins;
    }

    public String getPlant_space_ins() {
        return plant_space_ins;
    }

    public void setPlant_space_ins(String plant_space_ins) {
        this.plant_space_ins = plant_space_ins;
    }

    public String getGrowth_cycle() {
        return growth_cycle;
    }

    public void setGrowth_cycle(String growth_cycle) {
        this.growth_cycle = growth_cycle;
    }

    public String getPlant_harvest_ins() {
        return plant_harvest_ins;
    }

    public void setPlant_harvest_ins(String plant_harvest_ins) {
        this.plant_harvest_ins = plant_harvest_ins;
    }

    public String getComp_plant() {
        return comp_plant;
    }

    public void setComp_plant(String comp_plant) {
        this.comp_plant = comp_plant;
    }

    public String getWater_need() {
        return water_need;
    }

    public void setWater_need(String water_need) {
        this.water_need = water_need;
    }

    public String getPlant_desc() {
        return plant_desc;
    }

    public void setPlant_desc(String plant_desc) {
        this.plant_desc = plant_desc;
    }

    public String getPlant_img() {
        return plant_img;
    }

    public void setPlant_img(String plant_img) {
        this.plant_img = plant_img;
    }

    public String getHarvest_week() {
        return harvest_week;
    }

    public void setHarvest_week(String harvest_week) {
        this.harvest_week = harvest_week;
    }
}
