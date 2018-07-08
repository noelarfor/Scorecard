package com.roleon.scorecard.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.UserRepo;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;
import com.roleon.scorecard.sql.DatabaseHelper;
import com.roleon.scorecard.sql.DatabaseManager;

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
            user.setPassword("admin");
            user.setCreated_at(getDateTime());
            userRepo.addUser(user);
        }
        if (!userRepo.checkUser("tu1")) {
            user.setName("tu1");
            user.setPassword("tu1");
            user.setCreated_at(getDateTime());
            userRepo.addUser(user);
        }
        if (!userRepo.checkUser("tu2")) {
            user.setName("tu2");
            user.setPassword("tu2");
            user.setCreated_at(getDateTime());
            userRepo.addUser(user);
        }

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
}

