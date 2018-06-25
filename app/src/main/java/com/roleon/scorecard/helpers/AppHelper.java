package com.roleon.scorecard.helpers;

import android.app.Application;
import android.content.Context;

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


    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        databaseHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(databaseHelper);
        listUsers = new ArrayList<>();
        currentUser = new User();

        game = new Game();
        game.setGame_name("Fifa");
        game.setWin_points(3);
        game.setLoss_points(0);
        game.setDrawn_points(1);

        gameRepo = new GameRepo();
        gameRepo.addGame(game);

        user = new User();
        user.setName("Admin");
        user.setPassword("admin");
        user.setCreated_at(getDateTime());
        userRepo.addUser(user);
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

