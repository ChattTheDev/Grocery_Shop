package com.aezorspecialist.groceryshop.AllModels;

public class SpecialOffersModel {

    String ImageUrl, CategoryName;

    public SpecialOffersModel() {
    }

    public SpecialOffersModel(String imageUrl, String categoryName) {
        ImageUrl = imageUrl;
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
