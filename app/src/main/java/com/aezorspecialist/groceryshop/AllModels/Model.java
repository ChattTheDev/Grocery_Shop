package com.aezorspecialist.groceryshop.AllModels;

public class Model {

    public  String productname,productdescription,productcategory,originalprice,discountpercentage,finalprice,productimage;

    public Model() {
    }

    public Model(String productname, String productdescription, String productcategory, String originalprice, String discountpercentage, String finalprice, String productimage) {
        this.productname = productname;
        this.productdescription = productdescription;
        this.productcategory = productcategory;
        this.originalprice = originalprice;
        this.discountpercentage = discountpercentage;
        this.finalprice = finalprice;
        this.productimage = productimage;


    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(String originalprice) {
        this.originalprice = originalprice;
    }

    public String getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(String discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public String getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(String finalprice) {
        this.finalprice = finalprice;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }
}
