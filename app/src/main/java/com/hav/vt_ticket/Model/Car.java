package com.hav.vt_ticket.Model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
public class Car implements Serializable {

    @SerializedName("ten")
    private String name;

    @SerializedName("so_cho")
    private int capacity;

    @SerializedName("loai_xe")
    private String type;

    @SerializedName("tien_ich")
    private List<String> service;

    public Car(String name, int capacity, String type, List<String> service) {
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }
}
