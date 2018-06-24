package com.roleon.scorecard.helpers;

import android.app.Application;
import android.content.Context;

import com.roleon.scorecard.sql.DatabaseHelper;
import com.roleon.scorecard.sql.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppHelper extends Application {

    private static Context context;
    private static DatabaseHelper databaseHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        databaseHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(databaseHelper);
    }

    public static Context getContext(){
        return context;
    }

    public static String getDateTime() {

        //SimpleDateFormat dateFormat = new SimpleDateFormat(
        //        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}

