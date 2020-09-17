package com.aezorspecialist.groceryshop.AllModels;

public class ModelCategories {
    String CategoryName, ImageUrl;

    public ModelCategories() {
    }

    public ModelCategories(String categoryName, String imageUrl) {
        CategoryName = categoryName;
        ImageUrl = imageUrl;
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
