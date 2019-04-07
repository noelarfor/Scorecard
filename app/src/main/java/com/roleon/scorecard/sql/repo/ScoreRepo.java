package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import static com.roleon.scorecard.model.Score.KEY_SCORE_ID;

public class ScoreRepo {
    public static String createTable() {

        return "CREATE TABLE " + Score.TABLE + "("
                + KEY_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Score.KEY_SCORE_NAME + " TEXT,"
                + Score.KEY_SCORE_TYPE + " INTEGER,"
                + Score.KEY_SCORE_MODE + " INTEGER,"
                + Score.KEY_NUM_USERS + " INTEGER,"
                + Score.KEY_LAST_UPDDATE + " TEXT,"
                + Score.KEY_GAME_ID + " INTEGER,"
                + Score.KEY_SYNC_STATUS + " INTEGER"
                + ")";
    }

    public static void addScore(Score score) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Score.KEY_SCORE_NAME, score.getScore_name());
        values.put(Score.KEY_SCORE_TYPE, score.getScore_typ());
        values.put(Score.KEY_SCORE_MODE, score.getScore_mode());
        values.put(Score.KEY_NUM_USERS, score.getNum_users());
        values.put(Score.KEY_GAME_ID, score.getGame_id());
        values.put(Score.KEY_LAST_UPDDATE, score.getLast_update());
        values.put(Score.KEY_SYNC_STATUS, score.getSyncStatus());

        // Inserting Row
        db.insert(Score.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean checkScore(String scorename) {

        // array of columns to fetch
        String[] columns = {KEY_SCORE_ID};
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // selection criteria
        String selection = Score.KEY_SCORE_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {scorename};

        // query score table with condition
        Cursor cursor = db.query(Score.TABLE,    //Table to query
                columns,                        //columns to return
                selection,                      //columns for the WHERE clause
                selectionArgs,                  //The values for the WHERE clause
                null,                   //group the rows
                null,                    //filter by row groups
                null);                  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public static List<Score> getAllScore() {
        // array of columns to fetch
        String[] columns = {
                KEY_SCORE_ID,
                Score.KEY_SCORE_NAME,
                Score.KEY_SCORE_TYPE,
                Score.KEY_SCORE_MODE,
                Score.KEY_NUM_USERS,
                Score.KEY_GAME_ID,
                Score.KEY_LAST_UPDDATE,
                Score.KEY_SYNC_STATUS
        };
        // sorting orders
        String sortOrder =
                Score.KEY_LAST_UPDDATE + " DESC";
        List<Score> scoreList = new ArrayList<Score>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the score table
        Cursor cursor = db.query(Score.TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setScore_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SCORE_ID))));
                score.setScore_name(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_NAME)));
                score.setScore_typ(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_TYPE))));
                score.setScore_mode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_MODE))));
                score.setNum_users(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_NUM_USERS))));
                score.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_GAME_ID))));
                score.setLast_update(cursor.getString(cursor.getColumnIndex(Score.KEY_LAST_UPDDATE)));
                score.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SYNC_STATUS))));
                // Adding user record to list
                scoreList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return result list
        return scoreList;
    }

    public static Score getScoreByName(String scorename) {
        // array of columns to fetch
        String[] columns = {
                KEY_SCORE_ID,
                Score.KEY_SCORE_NAME,
                Score.KEY_SCORE_TYPE,
                Score.KEY_SCORE_MODE,
                Score.KEY_NUM_USERS,
                Score.KEY_LAST_UPDDATE,
                Score.KEY_GAME_ID,
                Score.KEY_SYNC_STATUS
        };

        // selection criteria
        String selection = Score.KEY_SCORE_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {scorename};

        Score score = new Score();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the score table
        Cursor cursor = db.query(Score.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        if ((cursor != null) && cursor.moveToFirst()) {
            score.setScore_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SCORE_ID))));
            score.setScore_name(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_NAME)));
            score.setScore_typ(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_TYPE))));
            score.setScore_mode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_MODE))));
            score.setNum_users(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_NUM_USERS))));
            score.setLast_update(cursor.getString(cursor.getColumnIndex(Score.KEY_LAST_UPDDATE)));
            score.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_GAME_ID))));
            score.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SYNC_STATUS))));

            cursor.close();
        }
        DatabaseManager.getInstance().closeDatabase();

        // return score list
        return score;
    }

    public static Score getScoreById(String score_id) {
        // array of columns to fetch
        String[] columns = {
                Score.KEY_SCORE_ID,
                Score.KEY_SCORE_NAME,
                Score.KEY_SCORE_TYPE,
                Score.KEY_SCORE_MODE,
                Score.KEY_NUM_USERS,
                Score.KEY_LAST_UPDDATE,
                Score.KEY_GAME_ID,
                Score.KEY_SYNC_STATUS
        };

        // selection criteria
        String selection = KEY_SCORE_ID + " = ?";

        // selection argument
        String[] selectionArgs = {score_id};

        Score score = new Score();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the score table
        Cursor cursor = db.query(Score.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        if (cursor != null && cursor.moveToFirst()) {
            score.setScore_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SCORE_ID))));
            score.setScore_name(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_NAME)));
            score.setScore_typ(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_TYPE))));
            score.setScore_mode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_MODE))));
            score.setNum_users(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_NUM_USERS))));
            score.setLast_update(cursor.getString(cursor.getColumnIndex(Score.KEY_LAST_UPDDATE)));
            score.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_GAME_ID))));
            score.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SYNC_STATUS))));

            cursor.close();
        }
        DatabaseManager.getInstance().closeDatabase();

        return score;
    }

    public static void updateScore(Score score, int sync_status) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Score.KEY_SYNC_STATUS, sync_status);
        values.put(Score.KEY_LAST_UPDDATE, AppHelper.getDateTime());

        // updating row
        db.update(Score.TABLE, values, Score.KEY_SCORE_ID + " = ?",
                new String[]{String.valueOf(score.getScore_Id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    /*
     * this method is for getting all the unsynced scores
     * so that we can sync it with database
     * */
    public static List<Score> getUnsyncedScores() {
        // array of columns to fetch
        String[] columns = {
                Score.KEY_SCORE_ID,
                Score.KEY_SCORE_NAME,
                Score.KEY_SCORE_TYPE,
                Score.KEY_SCORE_MODE,
                Score.KEY_NUM_USERS,
                Score.KEY_LAST_UPDDATE,
                Score.KEY_GAME_ID,
                Score.KEY_SYNC_STATUS
        };
        // selection criteria
        String selection = Score.KEY_SYNC_STATUS + " = ?";

        // selection argument
        String[] selectionArgs = {"0"};

        List<Score> scoreList = new ArrayList<Score>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        Cursor cursor = db.query(Score.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setScore_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SCORE_ID))));
                score.setScore_name(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_NAME)));
                score.setScore_typ(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_TYPE))));
                score.setScore_mode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_MODE))));
                score.setNum_users(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_NUM_USERS))));
                score.setLast_update(cursor.getString(cursor.getColumnIndex(Score.KEY_LAST_UPDDATE)));
                score.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_GAME_ID))));
                score.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SYNC_STATUS))));
                // Adding user record to list
                scoreList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return scoreList;
    }

    public void deleteScore(Score score) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(Score.TABLE, KEY_SCORE_ID + " = ?",
                new String[]{String.valueOf(score.getScore_Id())});
        DatabaseManager.getInstance().closeDatabase();
    }
}
