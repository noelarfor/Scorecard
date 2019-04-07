package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ResultRepo {
    public static String createTable() {

        return "CREATE TABLE " + Result.TABLE + "("
                + Result.KEY_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Result.KEY_USER_NAME + " TEXT,"
                + Result.KEY_SCORE_ID + " INTEGER,"
                + Result.KEY_RESULT_WIN + " INTEGER,"
                + Result.KEY_RESULT_DRAWN + " INTEGER,"
                + Result.KEY_RESULT_LOSS + " INTEGER,"
                + Result.KEY_RESULT_DIFF + " INTEGER,"
                + Result.KEY_RESULT_POINTS + " INTEGER,"
                + Result.KEY_CREATED_AT + " TEXT,"
                + Result.KEY_SYNC_STATUS + " INTEGER"
                + ")";
    }

    public static void addResult(Result result) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Result.KEY_USER_NAME, result.getUser_name());
        values.put(Result.KEY_SCORE_ID, result.getScore_id());
        values.put(Result.KEY_RESULT_WIN, result.getResult_win());
        values.put(Result.KEY_RESULT_DRAWN, result.getResult_drawn());
        values.put(Result.KEY_RESULT_LOSS, result.getResult_loss());
        values.put(Result.KEY_RESULT_DIFF, result.getResult_diff());
        values.put(Result.KEY_RESULT_POINTS, result.getResult_points());
        values.put(Result.KEY_CREATED_AT, result.getCreated_at());
        values.put(Result.KEY_SYNC_STATUS, result.getSyncStatus());

        // Inserting Row
        db.insert(Result.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Result> getAllResult() {
        // array of columns to fetch
        String[] columns = {
                Result.KEY_RESULT_ID,
                Result.KEY_USER_NAME,
                Result.KEY_SCORE_ID,
                Result.KEY_RESULT_WIN,
                Result.KEY_RESULT_DRAWN,
                Result.KEY_RESULT_LOSS,
                Result.KEY_RESULT_DIFF,
                Result.KEY_RESULT_POINTS,
                Result.KEY_CREATED_AT,
                Result.KEY_SYNC_STATUS
        };
        // sorting orders
        String sortOrder =
                Result.KEY_RESULT_ID + " ASC";
        List<Result> resultList = new ArrayList<Result>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the result table
        Cursor cursor = db.query(Result.TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setResult_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_ID))));
                result.setUser_name(cursor.getString(cursor.getColumnIndex(Result.KEY_USER_NAME)));
                result.setScore_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SCORE_ID))));
                result.setResult_win(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_WIN))));
                result.setResult_drawn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DRAWN))));
                result.setResult_loss(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_LOSS))));
                result.setResult_diff(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DIFF))));
                result.setResult_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_POINTS))));
                result.setCreated_at(cursor.getString(cursor.getColumnIndex(Result.KEY_CREATED_AT)));
                result.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SYNC_STATUS))));
                // Adding user record to list
                resultList.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return result list
        return resultList;
    }

    public void deleteResultUser(Result result) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(Result.TABLE, Result.KEY_RESULT_ID + " = ?",
                new String[]{String.valueOf(result.getResult_id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateResult(Result result, int sync_status) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Result.KEY_RESULT_WIN, result.getResult_win());
        values.put(Result.KEY_RESULT_DRAWN, result.getResult_drawn());
        values.put(Result.KEY_RESULT_LOSS, result.getResult_loss());
        values.put(Result.KEY_RESULT_DIFF, result.getResult_diff());
        values.put(Result.KEY_RESULT_POINTS, result.getResult_points());
        values.put(Result.KEY_CREATED_AT, AppHelper.getDateTime());
        values.put(Result.KEY_SYNC_STATUS, sync_status);

        // updating row
        db.update(Result.TABLE, values, Result.KEY_RESULT_ID + " = ?",
                new String[]{String.valueOf(result.getResult_id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<String> getScoreIdsOfUser(String user_name) {
        // array of columns to fetch
        String[] columns = {
                Result.KEY_USER_NAME,
                Result.KEY_SCORE_ID,
                Result.KEY_CREATED_AT,
        };

        // selection criteria
        String selection = Result.KEY_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {user_name};

        // sorting orders
        String sortOrder = Result.KEY_CREATED_AT + " DESC";

        List<String> scoreIdsList = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(Result.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
                scoreIdsList.add(cursor.getString(cursor.getColumnIndex(Result.KEY_SCORE_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return scoreIdsList;
    }

    public static List<Result> getResultsOfScore(String score_id) {
        // array of columns to fetch
        String[] columns = {
                Result.KEY_RESULT_ID,
                Result.KEY_USER_NAME,
                Result.KEY_SCORE_ID,
                Result.KEY_RESULT_WIN,
                Result.KEY_RESULT_DRAWN,
                Result.KEY_RESULT_LOSS,
                Result.KEY_RESULT_DIFF,
                Result.KEY_RESULT_POINTS,
                Result.KEY_CREATED_AT,
                Result.KEY_SYNC_STATUS
        };

        // selection criteria
        String selection = Result.KEY_SCORE_ID + " = ?";

        // selection argument
        String[] selectionArgs = {score_id};

        List<Result> resultList = new ArrayList<Result>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the score table
         Cursor cursor = db.query(Result.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Log.d("SCORECARD: ", "Result ScoreID " + score_id);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setResult_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_ID))));
                result.setUser_name(cursor.getString(cursor.getColumnIndex(Result.KEY_USER_NAME)));
                result.setScore_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SCORE_ID))));
                result.setResult_win(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_WIN))));
                result.setResult_drawn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DRAWN))));
                result.setResult_loss(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_LOSS))));
                result.setResult_diff(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DIFF))));
                result.setResult_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_POINTS))));
                result.setCreated_at(cursor.getString(cursor.getColumnIndex(Result.KEY_CREATED_AT)));
                result.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SYNC_STATUS))));
                Log.d("SCORECARD: ", "Result User " + cursor.getString(cursor.getColumnIndex(Result.KEY_USER_NAME)));
                // Adding user record to list
                resultList.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return result list
        return resultList;
    }

    public static void updateResultStatus(Result result, int sync_status) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Result.KEY_SYNC_STATUS, sync_status);

        // updating row
        db.update(Result.TABLE, values, Result.KEY_RESULT_ID + " = ?",
                new String[]{String.valueOf(result.getResult_id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    /*
     * this method is for getting all the unsynced results
     * so that we can sync it with database
     * */
    public static List<Result> getUnsyncedResults() {
        // array of columns to fetch
        String[] columns = {
                Result.KEY_RESULT_ID,
                Result.KEY_USER_NAME,
                Result.KEY_SCORE_ID,
                Result.KEY_RESULT_WIN,
                Result.KEY_RESULT_DRAWN,
                Result.KEY_RESULT_LOSS,
                Result.KEY_RESULT_DIFF,
                Result.KEY_RESULT_POINTS,
                Result.KEY_CREATED_AT,
                Result.KEY_SYNC_STATUS
        };
        // selection criteria
        String selection = Result.KEY_SYNC_STATUS + " = ?";

        // selection argument
        String[] selectionArgs = {"0"};

        List<Result> resultList = new ArrayList<Result>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        Cursor cursor = db.query(Result.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setResult_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_ID))));
                result.setUser_name(cursor.getString(cursor.getColumnIndex(Result.KEY_USER_NAME)));
                result.setScore_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SCORE_ID))));
                result.setResult_win(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_WIN))));
                result.setResult_drawn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DRAWN))));
                result.setResult_loss(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_LOSS))));
                result.setResult_diff(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_DIFF))));
                result.setResult_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_POINTS))));
                result.setCreated_at(cursor.getString(cursor.getColumnIndex(Result.KEY_CREATED_AT)));
                result.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SYNC_STATUS))));
                // Adding result record to list
                resultList.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return result list
        return resultList;
    }
}

