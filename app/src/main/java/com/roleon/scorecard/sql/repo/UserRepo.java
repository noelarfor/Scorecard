package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class UserRepo {
   public static String createTable() {

        return "CREATE TABLE " + User.TABLE + "("
                + User.KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + User.KEY_USER_NAME + " TEXT,"
                + User.KEY_USER_PASSWORD + " TEXT,"
                + User.KEY_CREATED_AT + " TEXT,"
                + User.KEY_SYNC_STATUS + " INTEGER"
                + ")";
    }

    public static void addUser(User user) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_USER_NAME, user.getName());
        values.put(User.KEY_USER_PASSWORD, user.getPassword());
        values.put(User.KEY_CREATED_AT, user.getCreated_at());
        values.put(User.KEY_SYNC_STATUS, user.getSyncStatus());

        // Inserting Row
        db.insert(User.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                User.KEY_USER_ID,
                User.KEY_USER_NAME,
                User.KEY_USER_PASSWORD,
                User.KEY_CREATED_AT,
                User.KEY_SYNC_STATUS
        };
        // sorting orders
        String sortOrder =
                User.KEY_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        Cursor cursor = db.query(User.TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(User.KEY_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.KEY_USER_PASSWORD)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(User.KEY_CREATED_AT)));
                user.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_SYNC_STATUS))));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return userList;
    }

    public static User getUser(String username) {
        // array of columns to fetch
        String[] columns = {
                User.KEY_USER_ID,
                User.KEY_USER_NAME,
                User.KEY_USER_PASSWORD,
                User.KEY_CREATED_AT,
                User.KEY_SYNC_STATUS
        };

        // selection criteria
        String selection = User.KEY_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {username};

        User user = new User();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        Cursor cursor = db.query(User.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        if (cursor != null)
            cursor.moveToFirst();

        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_USER_ID))));
        user.setName(cursor.getString(cursor.getColumnIndex(User.KEY_USER_NAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(User.KEY_USER_PASSWORD)));
        user.setCreated_at(cursor.getString(cursor.getColumnIndex(User.KEY_CREATED_AT)));
        user.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_SYNC_STATUS))));

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return user;
    }

    public static void updateUserStatus(User user, int sync_status) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(User.KEY_SYNC_STATUS, sync_status);

        // updating row
        db.update(User.TABLE, values, User.KEY_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    /*
     * this method is for getting all the unsynced users
     * so that we can sync it with database
     * */
    public static List<User> getUnsyncedUsers() {
        // array of columns to fetch
        String[] columns = {
                User.KEY_USER_ID,
                User.KEY_USER_NAME,
                User.KEY_USER_PASSWORD,
                User.KEY_CREATED_AT,
                User.KEY_SYNC_STATUS
        };
        // selection criteria
        String selection = User.KEY_SYNC_STATUS + " = ?";

        // selection argument
        String[] selectionArgs = {"0"};

        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the user table
        Cursor cursor = db.query(User.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(User.KEY_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.KEY_USER_PASSWORD)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(User.KEY_CREATED_AT)));
                user.setSyncStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(User.KEY_SYNC_STATUS))));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return userList;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(User.TABLE, User.KEY_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean checkUser(String user) {

        // array of columns to fetch
        String[] columns = {User.KEY_USER_ID};
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // selection criteria
        String selection = User.KEY_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {user};

        // query user table with condition
        Cursor cursor = db.query(User.TABLE,    //Table to query
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

    public static boolean checkUser(String user, String password) {

        // array of columns to fetch
        String[] columns = {User.KEY_USER_ID};
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // selection criteria
        String selection = User.KEY_USER_NAME + " = ?" + " AND " + User.KEY_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {user, password};

        // query user table with conditions
        Cursor cursor = db.query(User.TABLE,    //Table to query
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
}



