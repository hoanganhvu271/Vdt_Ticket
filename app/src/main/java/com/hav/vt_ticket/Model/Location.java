package com.hav.vt_ticket.Model;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    private int id;

    @SerializedName("ten")
    private String name;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
