package com.hav.vt_ticket.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM user WHERE username = :username")
    public UserRoom getUserByUsername(String username);

    @Insert
    void insertAccount(UserRoom User);

}
