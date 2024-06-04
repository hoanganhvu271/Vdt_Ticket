package com.hav.vt_ticket.RoomDatabase;

import android.app.PendingIntent;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class NotificationRoom {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String content;

    private String intentName;

    private int ticketId;

    public NotificationRoom(String title, String content, String intentName, int ticketId) {
        this.title = title;
        this.content = content;
        this.intentName = intentName;
        this.ticketId = ticketId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

}
