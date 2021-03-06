package com.example.sproutproject;

public class OnboardingItem {
    private int image;
    private String title;
    private String description;
    private String searchImg;

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSearchImg() {
        return searchImg;
    }

    public void setSearchImg(String searchImg) {
        this.searchImg = searchImg;
    }
}
