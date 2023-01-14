package com.example.goldendreamsbowling.JavaFile;

public class productModel {

    String Name,Price,Image;

    public productModel() {
    }

    public productModel(String name, String price, String image) {
        Name = name;
        Price = price;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
