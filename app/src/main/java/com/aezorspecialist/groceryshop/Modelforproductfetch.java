package com.aezorspecialist.groceryshop;

public class Modelforproductfetch {
    String productName, quantity, price;

    public Modelforproductfetch() {
    }

    public Modelforproductfetch(String productName, String quantity, String price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
