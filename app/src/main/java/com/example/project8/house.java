package com.example.project8;

public class house {

    String place,address,price,imageUrl;

    public  house(){



    }

    public house(String place, String address, String price, String imageUrl) {
        this.place = place;
        this.address = address;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
