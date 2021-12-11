package com.divine.project.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
  
    //in minutes
//    private static Integer resetPasswordExpiryTime = 2;
  
    public static String verificationCodeExpiryTimeLimit(Integer resetPasswordExpiryTime){
        Date date = new Date();
      
        //time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
        //calender object to add time to the current time
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, resetPasswordExpiryTime);

        return dateFormat.format(calendar.getTime());
    }
  
    public static String getCurrentTime(){
        Date date = new Date();
      
        //time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
        return dateFormat.format(date);
    }
}
