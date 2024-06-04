package com.hav.vt_ticket.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
    public static String getEndDay(String startTime, int totalTime) {
        String endDay = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date startDate = sdf.parse(startTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.HOUR, totalTime);

            endDay = sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDay;
    }

    public static String getHour(String fullHour){
        return fullHour.split(" ")[1].split(":")[0] + ":" + fullHour.split(" ")[1].split(":")[1];
    }

    public static String getMoneyType(int money){
        //insert '.' every 3 digits from the right
        String moneyString = String.valueOf(money);
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = moneyString.length() - 1; i >= 0; i--) {
            result.append(moneyString.charAt(i));
            count++;
            if (count % 3 == 0 && i != 0) {
                result.append(".");
            }
        }
        return result.reverse().toString() + " VND";
    }
}
