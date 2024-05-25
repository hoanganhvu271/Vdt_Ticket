package com.hav.vt_ticket.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TicketDAO {

    @Insert
    void insertTicket(TicketRoom ticketRoom);

    @Delete
    void deleteTicket(TicketRoom ticketRoom);

    @Query("DELETE FROM ticket WHERE id=:id")
    void deleteTicketById(int id);

    @Query("SELECT * FROM ticket")
    List<TicketRoom> getAllTickets();

    @Query("UPDATE ticket SET price = :price WHERE id = :id")
    void updatePrice(int price, int id);

    @Query("SELECT * FROM ticket WHERE id = :id LIMIT 1")
    TicketRoom getTicketById(int id);

    @Update
    void updateTicket(TicketRoom ticketRoom);

    @Query("SELECT * FROM ticket WHERE beFollowed = 1")
    List<TicketRoom> getFollowedTicket();
}
