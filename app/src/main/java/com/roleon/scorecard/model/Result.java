package com.roleon.scorecard.model;

public class Result {

    public static final String TABLE = "result";

    public static final String KEY_RESULT_ID = "result_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_SCORE_ID = "score_id";
    public static final String KEY_RESULT_WIN = "result_win";
    public static final String KEY_RESULT_DRAWN = "result_drawn";
    public static final String KEY_RESULT_LOSS = "result_add_loss";
    public static final String KEY_RESULT_DIFF = "result_add_diff";
    public static final String KEY_RESULT_POINTS = "result_points";
    public static final String KEY_CREATED_AT = "created_at";

    private int result_id;
    private String user_name;
    private int score_id;
    private int result_win;
    private int result_drawn;
    private int result_add_loss;
    private int result_add_diff;
    private int result_points;
    private String created_at;

    public int getResult_id() { return result_id; }

    public void setResult_id(int result_id) { this.result_id = result_id; }

    public String getUser_name() { return user_name; }

    public void setUser_name(String user_name) { this.user_name = user_name; }

    public int getScore_id() { return score_id; }

    public void setScore_id(int score_id) { this.score_id = score_id; }

    public String getCreated_at() { return created_at; }

    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public int getResult_win() { return result_win; }

    public void setResult_win(int result_win) { this.result_win = result_win; }

    public int getResult_loss() { return result_drawn; }

    public void setResult_loss(int result_drawn) { this.result_drawn = result_drawn; }

    public int getResult_drawn() { return result_add_loss; }

    public void setResult_drawn(int result_add_loss) { this.result_add_loss = result_add_loss; }

    public int getResult_diff() { return result_add_diff; }

    public void setResult_diff(int result_add_diff) { this.result_add_diff = result_add_diff; }

    public int getResult_points() { return result_points; }

    public void setResult_points(int result_points) { this.result_points = result_points; }
}
