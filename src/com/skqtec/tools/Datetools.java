package com.skqtec.tools;

import java.sql.Timestamp;
import java.util.Date;

public class Datetools{
    public static Date timeStampToDate(Timestamp ts){
        Date date = new Date();
        try {
            date = ts;
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String[] time_diff(Date date1,Date date2){
        long diff = date2.getTime() - date1.getTime();
        String timeDiff[]=new String[3];
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        timeDiff[0]=String.valueOf(days);
        timeDiff[1]=String.valueOf(hours);
        timeDiff[2]=String.valueOf(minutes);
        return timeDiff;
    }
}
