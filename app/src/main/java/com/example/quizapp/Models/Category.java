package com.example.quizapp.Models;

public class Category {
    private int categoryId;
    private String categoryName;
    private String categoryDescription;
    private String imageUrl;

    public Category() {
    }

    public Category(int categoryId, String categoryName, String categoryDescription, String imageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.imageUrl = imageUrl;
    }

    public Category(String categoryName, String categoryDescription, String imageUrl) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.imageUrl = imageUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}