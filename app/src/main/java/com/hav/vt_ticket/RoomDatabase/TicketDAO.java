package com.hav.vt_ticket.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TicketDAO {

    @Insert
    void insertTicket(TicketRoom ticketRoom);

    @Delete
    void deleteTicket(TicketRoom ticketRoom);

    @Query("SELECT * FROM ticket")
    List<TicketRoom> getAllTickets();
}
