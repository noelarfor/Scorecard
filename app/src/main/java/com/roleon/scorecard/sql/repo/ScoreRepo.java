package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ScoreRepo {
    public static String createTable() {

        return "CREATE TABLE " + Score.TABLE + "("
                + Score.KEY_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Score.KEY_USER_ID + " INTEGER,"
                + Score.KEY_SCORE_NAME + " TEXT,"
                + Score.KEY_SCORE_TYPE + " INTEGER,"
                + Score.KEY_SCORE_MODE + " INTEGER,"
                + Score.KEY_NUM_USERS + " INTEGER,"
                + Score.KEY_LAST_UPDDATE + " TEXT,"
                + Score.KEY_GAME_ID + " INTEGER"
                + ")";
    }

    public static void addScore(Score score) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Score.KEY_USER_ID, score.getUser_Id());
        values.put(Score.KEY_SCORE_NAME, score.getScore_name());
        values.put(Score.KEY_SCORE_TYPE, score.getScore_typ());
        values.put(Score.KEY_SCORE_MODE, score.getScore_mode());
        values.put(Score.KEY_NUM_USERS, score.getNum_users());
        values.put(Score.KEY_GAME_ID, score.getGame_id());
        values.put(Score.KEY_LAST_UPDDATE, score.getLast_update());
        // Inserting Row
        db.insert(Score.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean checkScore(String scorename) {

        // array of columns to fetch
        String[] columns = {Score.KEY_SCORE_ID};
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // selection criteria
        String selection = Score.KEY_SCORE_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {scorename};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'name@domain.com';
         */
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
                Score.KEY_SCORE_ID,
                Score.KEY_USER_ID,
                Score.KEY_SCORE_NAME,
                Score.KEY_SCORE_TYPE,
                Score.KEY_SCORE_MODE,
                Score.KEY_NUM_USERS,
                Score.KEY_GAME_ID,
                Score.KEY_LAST_UPDDATE
        };
        // sorting orders
        String sortOrder =
                Score.KEY_LAST_UPDDATE + " DESC";
        List<Score> scoreList = new ArrayList<Score>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
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
                score.setScore_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_ID))));
                score.setUser_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_USER_ID))));
                score.setScore_name(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_NAME)));
                score.setScore_typ(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_TYPE))));
                score.setScore_mode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_SCORE_MODE))));
                score.setNum_users(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_NUM_USERS))));
                score.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Score.KEY_GAME_ID))));
                score.setLast_update(cursor.getString(cursor.getColumnIndex(Score.KEY_LAST_UPDDATE)));
                // Adding user record to list
                scoreList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return scoreList;
    }

    public void updateScore(Score score) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Score.KEY_LAST_UPDDATE, score.getLast_update());

        // updating row
        db.update(Score.TABLE, values, Score.KEY_SCORE_ID + " = ?",
                new String[]{String.valueOf(score.getScore_Id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteUser(Score score) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(Score.TABLE, Score.KEY_USER_ID + " = ?",
                new String[]{String.valueOf(score.getScore_Id())});
        DatabaseManager.getInstance().closeDatabase();
    }
}
