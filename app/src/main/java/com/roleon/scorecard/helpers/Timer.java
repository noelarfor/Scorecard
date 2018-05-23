package com.roleon.scorecard.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timer {

    public static String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
