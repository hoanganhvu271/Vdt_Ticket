package com.hav.vt_ticket.BroadcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hav.vt_ticket.Service.TicketCheckFirebase;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, TicketCheckFirebase.class);
            context.startService(serviceIntent);
        }
    }
}
