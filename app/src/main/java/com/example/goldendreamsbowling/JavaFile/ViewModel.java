package com.example.goldendreamsbowling.JavaFile;


public class ViewModel {

    String Name,Price,Image;

    public ViewModel() {
    }

    public ViewModel(String name, String price, String image) {
        this.Name = name;
        this.Price = price;
        this.Image = image;
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
