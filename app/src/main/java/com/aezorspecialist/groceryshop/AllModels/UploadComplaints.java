package com.aezorspecialist.groceryshop.AllModels;

public class UploadComplaints {

    private String CustomerName, Customeremail, CustomerMobile, CustomerFullAddress, Fullcity, Fullpincode, image;

    public UploadComplaints() {
    }

    public UploadComplaints(String customerName, String customeremail, String customerMobile, String customerFullAddress, String fullcity, String fullpincode, String image) {
        CustomerName = customerName;
        Customeremail = customeremail;
        CustomerMobile = customerMobile;
        CustomerFullAddress = customerFullAddress;
        Fullcity = fullcity;
        Fullpincode = fullpincode;
        this.image = image;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomeremail() {
        return Customeremail;
    }

    public void setCustomeremail(String customeremail) {
        Customeremail = customeremail;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }

    public String getCustomerFullAddress() {
        return CustomerFullAddress;
    }

    public void setCustomerFullAddress(String customerFullAddress) {
        CustomerFullAddress = customerFullAddress;
    }

    public String getFullcity() {
        return Fullcity;
    }

    public void setFullcity(String fullcity) {
        Fullcity = fullcity;
    }

    public String getFullpincode() {
        return Fullpincode;
    }

    public void setFullpincode(String fullpincode) {
        Fullpincode = fullpincode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
