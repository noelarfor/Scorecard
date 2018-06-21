package com.roleon.scorecard.model;

public class Game {

    public static final String TAG = Game.class.getSimpleName();
    public static final String TABLE = "game";
    //Table Columns names
    public static final String KEY_GAME_ID = "game_id";
    public static final String KEY_GAME_NAME = "game_name";
    public static final String KEY_WIN_POINTS = "win_points";
    public static final String KEY_LOSS_POINTS = "loss_points";
    public static final String KEY_DRAWN_POINTS = "drawn_points";

    private int game_id;
    private String game_name;
    private int win_points;
    private int loss_points;
    private int drawn_points;

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public int getWin_points() {
        return win_points;
    }

    public void setWin_points(int win_points) {
        this.win_points = win_points;
    }

    public int getLoss_points() {
        return loss_points;
    }

    public void setLoss_points(int loss_points) {
        this.loss_points = loss_points;
    }

    public int getDrawn_points() {
        return drawn_points;
    }

    public void setDrawn_points(int drawn_points) {
        this.drawn_points = drawn_points;
    }
}
