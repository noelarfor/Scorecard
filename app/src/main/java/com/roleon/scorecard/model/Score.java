package com.roleon.scorecard.model;

public class Score {

    public static final String TAG = Score.class.getSimpleName();
    public static final String TABLE = "score";

    public static final String KEY_SCORE_ID = "score_id";
    public static final String KEY_SCORE_NAME = "score_name";
    public static final String KEY_SCORE_TYPE = "score_type";
    public static final String KEY_SCORE_MODE = "score_mode";
    public static final String KEY_NUM_USERS = "num_users";
    public static final String KEY_LAST_UPDDATE = "last_update";
    public static final String KEY_GAME_ID = "game_id";

    private int score_id;
    private String score_name;
    private int score_typ;
    private int score_mode;
    private int num_users;
    private String last_update;
    private int game_id;

    public int getScore_Id() {
        return score_id;
    }

    public void setScore_Id(int score_id) {
        this.score_id = score_id;
    }

    public String getScore_name() {
        return score_name;
    }

    public void setScore_name(String score_name) {
        this.score_name = score_name;
    }

    public int getScore_typ() {
        return score_typ;
    }

    public void setScore_typ(int score_typ) {
        this.score_typ = score_typ;
    }

    public int getScore_mode() {
        return score_mode;
    }

    public void setScore_mode(int score_mode) {
        this.score_mode = score_mode;
    }

    public int getNum_users() {
        return num_users;
    }

    public void setNum_users(int num_users) {
        this.num_users = num_users;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public int getGame_id() { return game_id; }

    public void setGame_id(int game_id) { this.game_id = game_id; }
}
