package com.hav.vt_ticket.Api;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<T> data;

    public ApiResponse(int status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }


}