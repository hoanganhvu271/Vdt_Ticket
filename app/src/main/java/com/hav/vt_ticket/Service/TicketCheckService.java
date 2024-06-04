//package com.hav.vt_ticket.Service;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.util.Log;
//
//import java.util.List;
//
//import androidx.annotation.Nullable;
//
//import com.hav.vt_ticket.Api.ApiResponse;
//import com.hav.vt_ticket.Api.ApiService;
//import com.hav.vt_ticket.Model.Ticket;
//import com.hav.vt_ticket.RoomDatabase.AppDatabase;
//
//import com.hav.vt_ticket.RoomDatabase.NotificationRoom;
//import com.hav.vt_ticket.RoomDatabase.TicketRoom;
//import com.hav.vt_ticket.Utils.NotificationUtils;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TicketCheckService extends Service {
//
//    private static final int NOTIFICATION_ID = 123;
//    private static final String CHANNEL_ID = "TicketPriceChannel";
//    private static final long INTERVAL = 1 * 60 * 1000;
//    private Handler handler = new Handler(Looper.getMainLooper());
//
//    @SuppressLint("ForegroundServiceType")
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Notification notification = NotificationUtils.createNotification(this, "Viettel Ticket", "Luôn giúp bạn cập nhật giá vé nhanh nhất <3");
//        startForeground(NOTIFICATION_ID, notification);
//        schedulePriceCheck();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopForeground(true);
//    }
//
//    private void schedulePriceCheck() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<TicketRoom> ticket = AppDatabase.getInstance(TicketCheckService.this).ticketDAO().getAllTickets();
//
//                for (TicketRoom t : ticket) {
//                    ApiService.apiService.getPrice(t.getId()).enqueue(new Callback<ApiResponse<Ticket>>() {
//                        @Override
//                        public void onResponse(Call<ApiResponse<Ticket>> call, Response<ApiResponse<Ticket>> response) {
//                            if (response.body() != null) {
//                                List<Ticket> ticketList = response.body().getData();
//                                if (!ticketList.isEmpty()) {
//                                    Ticket ticket = ticketList.get(0);
//                                    if (ticket.getPrice() < t.getPrice()) {
//                                        String title = "Viettel Ticket";
//                                        String content = "Giá vé đi từ " + t.getStartPoint() + " đến " + t.getEndPoint() + " đã giảm, hãy đặt vé ngay!";
//                                        Notification noti = NotificationUtils.createNotification(TicketCheckService.this, title, content);
//                                        NotificationUtils.showNotification(TicketCheckService.this, noti);
//
//                                        //Add to database:
//                                        NotificationRoom notificationRoom = new NotificationRoom(title, content);
//                                        AppDatabase.getInstance(TicketCheckService.this).notificationDAO().insertNoti(notificationRoom);
//                                    }
//
//                                    //Update price:
//                                    AppDatabase.getInstance(TicketCheckService.this).ticketDAO().updatePrice(ticket.getPrice(), t.getId());
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ApiResponse<Ticket>> call, Throwable t) {
//                            Log.d("Vu", "onFailure: " + t.getMessage());
//                        }
//                    });
//
//                }
//                handler.postDelayed(this, INTERVAL);
//            }
//        }, INTERVAL);
//    }
//}
