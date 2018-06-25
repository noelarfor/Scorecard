package com.roleon.scorecard.sql.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class GameRepo {

    public static String createTable() {

        return "CREATE TABLE " + Game.TABLE + "("
                + Game.KEY_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Game.KEY_GAME_NAME + " TEXT,"
                + Game.KEY_WIN_POINTS + " INTEGER,"
                + Game.KEY_LOSS_POINTS + " INTEGER,"
                + Game.KEY_DRAWN_POINTS + " INTEGER"
                + ")";
    }

    public static void addGame(Game game) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Game.KEY_GAME_NAME, game.getGame_name());
        values.put(Game.KEY_WIN_POINTS, game.getWin_points());
        values.put(Game.KEY_LOSS_POINTS, game.getLoss_points());
        values.put(Game.KEY_DRAWN_POINTS, game.getDrawn_points());

        // Inserting Row
        db.insert(Game.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Game> getAllGame() {
        // array of columns to fetch
        String[] columns = {
                Game.KEY_GAME_ID,
                Game.KEY_GAME_NAME,
                Game.KEY_WIN_POINTS,
                Game.KEY_LOSS_POINTS,
                Game.KEY_DRAWN_POINTS
        };
        // sorting orders
        String sortOrder =
                Game.KEY_GAME_ID + " DESC";
        List<Game> gameList = new ArrayList<Game>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the game table
        Cursor cursor = db.query(Game.TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Game game = new Game();
                game.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_GAME_ID))));
                game.setGame_name(cursor.getString(cursor.getColumnIndex(Game.KEY_GAME_NAME)));
                game.setWin_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_WIN_POINTS))));
                game.setLoss_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_LOSS_POINTS))));
                game.setDrawn_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_DRAWN_POINTS))));
                // Adding user record to list
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return gameList;
    }

    public static Game getGame(String gamename) {
        // array of columns to fetch
        String[] columns = {
                Game.KEY_GAME_ID,
                Game.KEY_GAME_NAME,
                Game.KEY_WIN_POINTS,
                Game.KEY_LOSS_POINTS,
                Game.KEY_DRAWN_POINTS
        };

        // selection criteria
        String selection = Game.KEY_GAME_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {gamename};

        Game game = new Game();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // query the game table
        Cursor cursor = db.query(Game.TABLE, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        if (cursor != null)
            cursor.moveToFirst();

        game.setGame_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_GAME_ID))));
        game.setGame_name(cursor.getString(cursor.getColumnIndex(Game.KEY_GAME_NAME)));
        game.setWin_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_WIN_POINTS))));
        game.setLoss_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_LOSS_POINTS))));
        game.setDrawn_points(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Game.KEY_DRAWN_POINTS))));

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        // return user list
        return game;
    }

    public void updateUser(Game game) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Game.KEY_GAME_NAME, game.getGame_name());
        values.put(Game.KEY_WIN_POINTS, game.getWin_points());
        values.put(Game.KEY_LOSS_POINTS, game.getLoss_points());
        values.put(Game.KEY_DRAWN_POINTS, game.getDrawn_points());

        // updating row
        db.update(Game.TABLE, values, Game.KEY_GAME_ID + " = ?",
                new String[]{String.valueOf(game.getGame_id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteUser(Game game) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // delete user record by id
        db.delete(Game.TABLE, Game.KEY_GAME_ID + " = ?",
                new String[]{String.valueOf(game.getGame_id())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public static boolean checkGame(String game) {

        // array of columns to fetch
        String[] columns = {Game.KEY_GAME_ID};
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // selection criteria
        String selection = Game.KEY_GAME_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {game};

        // query game table with condition
        Cursor cursor = db.query(Game.TABLE,    //Table to query
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
