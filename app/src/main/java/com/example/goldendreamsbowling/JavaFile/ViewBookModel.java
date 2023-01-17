package com.example.goldendreamsbowling.JavaFile;


public class ViewBookModel {

    String Date,Email,FullName,Lane,NumberGame,NumberPlayer,NumberShoes,PaymentID,Time,TotalPrice;

    public ViewBookModel() {
    }

    public ViewBookModel(String date, String email, String fullName, String lane, String numberGame, String numberPlayer, String numberShoes, String paymentID, String time, String totalPrice) {
        Date = date;
        Email = email;
        FullName = fullName;
        Lane = lane;
        NumberGame = numberGame;
        NumberPlayer = numberPlayer;
        NumberShoes = numberShoes;
        PaymentID = paymentID;
        Time = time;
        TotalPrice = totalPrice;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLane() {
        return Lane;
    }

    public void setLane(String lane) {
        Lane = lane;
    }

    public String getNumberGame() {
        return NumberGame;
    }

    public void setNumberGame(String numberGame) {
        NumberGame = numberGame;
    }

    public String getNumberPlayer() {
        return NumberPlayer;
    }

    public void setNumberPlayer(String numberPlayer) {
        NumberPlayer = numberPlayer;
    }

    public String getNumberShoes() {
        return NumberShoes;
    }

    public void setNumberShoes(String numberShoes) {
        NumberShoes = numberShoes;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
