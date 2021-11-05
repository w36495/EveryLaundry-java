package com.w36495.everylaundry.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String parseDate(String currentDate) {
        StringBuilder stringBuilder = new StringBuilder();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(currentDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd HH:mm");
            stringBuilder.append(dateFormat1.format(date));


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return stringBuilder.toString();

    }

}
