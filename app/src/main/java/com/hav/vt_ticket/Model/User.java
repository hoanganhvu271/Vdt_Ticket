package com.hav.vt_ticket.Model;

import com.hav.vt_ticket.RoomDatabase.UserRoom;

import java.util.List;

public class User {
    private UserRoom user;
    private String token;

    public User(UserRoom user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserRoom getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setUser(UserRoom user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
