package com.hav.vt_ticket.RoomDatabase;

import androidx.room.Entity;

import androidx.room.PrimaryKey;

@Entity(tableName = "ticket")
public class TicketRoom {
    @PrimaryKey
    public int id;

    public int carId;

    public String startPoint;

    public String endPoint;

    public String startTime;

    public int totalTime;

    public int price;

    public int amount;

    public String carName;

    public boolean beFollowed;

    public TicketRoom(int id, int carId, String startPoint, String endPoint, String startTime, int totalTime, int price, int amount, String carName, boolean beFollowed) {
        this.id = id;
        this.carId = carId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.price = price;
        this.amount = amount;
        this.carName = carName;
        this.beFollowed = beFollowed;
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

    public boolean isBeFollowed() {
        return beFollowed;
    }

    public void setBeFollowed(boolean beFollowed) {
        this.beFollowed = beFollowed;
    }
}

