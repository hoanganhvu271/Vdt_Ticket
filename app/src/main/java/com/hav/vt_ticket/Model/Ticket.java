package com.hav.vt_ticket.Model;

import com.google.gson.annotations.SerializedName;
public class Ticket {

    @SerializedName("id")
    private int id;


    @SerializedName("id_xe")
    private int carId;

    @SerializedName("diem_di")
    private String startPoint;

    @SerializedName("diem_den")
    private String endPoint;

    @SerializedName("xuat_phat")
    private String startTime;

    @SerializedName("thoi_gian")
    private int totalTime;

    @SerializedName("gia_ve")
    private int price;

    @SerializedName("con_lai")
    private int amount;

    @SerializedName("ten_nha_xe")
    private String carName;

    public Ticket(int id, int carId, String startPoint, String endPoint, String startTime, int totalTime, int price, int amount, String carName) {
        this.id = id;
        this.carId = carId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.price = price;
        this.amount = amount;
        this.carName = carName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
