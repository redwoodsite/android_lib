package com.sjwlib111.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommUtil {
    public static boolean greaterThan(Date d1, Date d2){
        String str1 = new SimpleDateFormat("yyyyMMddHHmm").format(d1);
        String str2 = new SimpleDateFormat("yyyyMMddHHmm").format(d2);
        long L1 = Long.parseLong(str1);
        long L2 = Long.parseLong(str2);
        return L1 > L2;
    }

    public static boolean isBeforeDate(String date0, String date1) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = formatter.format(new Date());
        date0 = date0.equals("") ? currDate : date0;
        date1 = date1.equals("") ? currDate : date1;
        try {
            java.util.Date compDate0 = formatter.parse(date0);
            java.util.Date compDate1 = formatter.parse(date1);
            return compDate0.before(compDate1);
        } catch (Exception e) {
            return true;
        }
    }
}