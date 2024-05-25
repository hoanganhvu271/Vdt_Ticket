package com.hav.vt_ticket.RoomDatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDAO {

    @Insert
    public void insertNoti(NotificationRoom notificationRoom);

    @Delete
    public void deleteNoti(NotificationRoom notificationRoom);

    @Query("SELECT * FROM notification")
    public List<NotificationRoom> getAllNoti();
}
