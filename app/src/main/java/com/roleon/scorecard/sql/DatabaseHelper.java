package com.roleon.scorecard.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.UserRepo;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version - increase version each time after modified db
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "scorecardManager.db";
    private static final String TAG = DatabaseHelper.class.getSimpleName().toString();

    public DatabaseHelper() {
        super(AppHelper.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserRepo.createTable());
        //db.execSQL(CREATE_SCORE_TABLE);
        //db.execSQL(CREATE_RESULT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        //Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);

        // Create tables again
        onCreate(db);
    }
}

    /*


    // Result Table Column names
    private static final String COLUMN_RESULT = "result";
    private static final String COLUmN_WIN_LOSE = "win_lose";

    // create table sql query
    // Score table create statement
    private String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SCORE_NAME + " TEXT,"
            + COLUMN_SCORE_TYPE + " INTEGER,"
            + COLUMN_SCORE_MODE + " INTEGER,"
            + COLUMN_NUM_USERS + " INTEGER,"
            + COLUMN_SCORE_TIMESTAMP + " DATETIME"
            + ")";

    // Result table create statement
    private String CREATE_RESULT_TABLE = "CREATE TABLE " + TABLE_RESULT + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RESULT + " INTEGER,"
            + COLUmN_WIN_LOSE + " TEXT"
            + ")";*/