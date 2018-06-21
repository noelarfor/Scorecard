package com.roleon.scorecard.model;

public class Result {

    public static final String TAG = Result.class.getSimpleName();
    public static final String TABLE = "result";
    //Table Columns names
    public static final String KEY_RESULT_ID = "result_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SCORE_ID = "score_id";
    public static final String KEY_RESULT_WIN_LOSS = "result_win_loss";
    public static final String KEY_RESULIT_INFO_1 = "result_info_1";
    public static final String KEY_RESULIT_INFO_2 = "result_info_2";
    public static final String KEY_RESULIT_INFO_3 = "result_info_3";
    public static final String KEY_CREATED_AT = "created_at";

    private int user_id;
    private int score_id;
    private int result_win_loss;
    private int result_info_1;
    private int result_info_2;
    private int result_info_3;
    private String created_at;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }

    public int getResult_win_loss() {
        return result_win_loss;
    }

    public void setResult_win_loss(int result_win_loss) {
        this.result_win_loss = result_win_loss;
    }

    public int getResult_info_1() {
        return result_info_1;
    }

    public void setResult_info_1(int result_info_1) {
        this.result_info_1 = result_info_1;
    }

    public int getResult_info_2() {
        return result_info_2;
    }

    public void setResult_info_2(int result_info_2) {
        this.result_info_2 = result_info_2;
    }

    public int getResult_info_3() {
        return result_info_3;
    }

    public void setResult_info_3(int result_info_3) {
        this.result_info_3 = result_info_3;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
