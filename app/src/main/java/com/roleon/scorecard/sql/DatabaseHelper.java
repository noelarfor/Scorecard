package com.roleon.scorecard.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.ScoreRepo;
import com.roleon.scorecard.sql.repo.GameRepo;
import com.roleon.scorecard.sql.repo.ResultRepo;
import com.roleon.scorecard.sql.repo.UserRepo;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version - increase version each time after modified db
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "scorecardManager.db";
    private static final String TAG = DatabaseHelper.class.getSimpleName().toString();

    public DatabaseHelper() {
        super(AppHelper.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserRepo.createTable());
        db.execSQL(ScoreRepo.createTable());
        db.execSQL(GameRepo.createTable());
        db.execSQL(ResultRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        //Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Score.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Result.TABLE);

        // Create tables again
        onCreate(db);
    }
}
