package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ResultRepo {
    public static String createTable() {

        return "CREATE TABLE " + Result.TABLE + "("
                + Result.KEY_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Result.KEY_USER_ID + " INTEGER,"
                + Result.KEY_SCORE_ID + " INTEGER,"
                + Result.KEY_RESULT_MATCH_ID + " INTEGER,"
                + Result.KEY_RESULT_POINTS + " INTEGER,"
                + Result.KEY_RESULT_INFO_1 + " INTEGER,"
                + Result.KEY_RESULT_INFO_2 + " INTEGER,"
                + Result.KEY_RESULT_INFO_3 + " INTEGER,"
                + Result.KEY_CREATED_AT + " TEXT"
                + ")";
    }

    public static void addResult(Result result) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Result.KEY_USER_ID, result.getUser_id());
        values.put(Result.KEY_SCORE_ID, result.getScore_id());
        values.put(Result.KEY_RESULT_MATCH_ID, result.getResult_match_id());
        values.put(Result.KEY_RESULT_POINTS, result.getResult_result_points());
        values.put(Result.KEY_RESULT_INFO_1, result.getResult_info_1());
        values.put(Result.KEY_RESULT_INFO_2, result.getResult_info_2());
        values.put(Result.KEY_RESULT_INFO_3, result.getResult_info_3());
        values.put(Result.KEY_CREATED_AT, result.getCreated_at());
        // Inserting Row
        db.insert(Result.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Result> getAllResult() {
        // array of columns to fetch
        String[] columns = {
                Result.KEY_RESULT_ID,
                Result.KEY_USER_ID,
                Result.KEY_SCORE_ID,
                Result.KEY_RESULT_MATCH_ID,
                Result.KEY_RESULT_POINTS,
                Result.KEY_RESULT_INFO_1,
                Result.KEY_RESULT_INFO_2,
                Result.KEY_RESULT_INFO_3,
                Result.KEY_CREATED_AT
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
                result.setUser_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_USER_ID))));
                result.setScore_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_SCORE_ID))));
                result.setResult_match_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_MATCH_ID))));
                result.setResult_result_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_POINTS))));
                result.setResult_info_1(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_INFO_1))));
                result.setResult_info_2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_INFO_2))));
                result.setResult_info_3(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Result.KEY_RESULT_INFO_3))));
                result.setCreated_at(cursor.getString(cursor.getColumnIndex(Result.KEY_CREATED_AT)));
                // Adding user record to list
                resultList.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return result list
        return resultList;
    }

    public void resultUser(Result result) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(Result.TABLE, Result.KEY_RESULT_ID + " = ?",
                new String[]{String.valueOf(result.getResult_id())});
        DatabaseManager.getInstance().closeDatabase();
    }
}
