package com.hav.vt_ticket.Api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ObjectResponse<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public ObjectResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ObjectResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}