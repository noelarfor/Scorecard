package com.roleon.scorecard.model;

public class Score {

    private int id;
    private String score_name;
    private int score_typ;
    private int score_mode;
    private int num_users;
    private String timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
