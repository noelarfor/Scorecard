package com.roleon.scorecard.helpers;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.roleon.scorecard.R;
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.UserRepo;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;
import com.roleon.scorecard.sql.DatabaseHelper;
import com.roleon.scorecard.sql.DatabaseManager;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppHelper extends Application {

    private static Context context;
    private static DatabaseHelper databaseHelper;

    private static GameRepo gameRepo;
    private static Game game;
    private static UserRepo userRepo;
    private User user;

    public static List<User> listUsers;
    public static User currentUser;
    public static boolean isInit;
    public static boolean shouldAllowOnBackPressed;
    public static String gameName;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "dataSaved";

    //1 means data is synced and 0 means data is not synced
    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        databaseHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(databaseHelper);

        listUsers = new ArrayList<>();
        currentUser = new User();
        isInit = true;
        shouldAllowOnBackPressed = false;


        user = new User();
        userRepo = new UserRepo();
        game = new Game();
        gameRepo = new GameRepo();

        if (!gameRepo.checkGame("Fifa")) {
            game.setGame_name("Fifa");
            game.setWin_points(3);
            game.setLoss_points(0);
            game.setDrawn_points(1);
            gameRepo.addGame(game);
        }

        if (!userRepo.checkUser("Admin")) {
            user.setName("Admin");
            user.setPassword(MD5("admin"));
            user.setCreated_at(getDateTime());
            user.setSyncStatus(NOT_SYNCED_WITH_SERVER);
            userRepo.addUser(user);
        }
        /*
        if (!userRepo.checkUser("tu1")) {
            user.setName("tu1");
            user.setPassword("tu1");
            user.setCreated_at(getDateTime());
            user.setSyncStatus(NOT_SYNCED_WITH_SERVER);
            userRepo.addUser(user);
        }
        if (!userRepo.checkUser("tu2")) {
            user.setName("tu2");
            user.setPassword("tu2");
            user.setCreated_at(getDateTime());
            user.setSyncStatus(NOT_SYNCED_WITH_SERVER);
            userRepo.addUser(user);
        }*/

    }

    public static Context getContext(){
        return context;
    }

    public static String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showProgressDialogTimed(Context context, String title, String message, int miliSeconds) {
        final ProgressDialog progress = new ProgressDialog(context, R.style.ProgressDialog);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, miliSeconds);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }
}

