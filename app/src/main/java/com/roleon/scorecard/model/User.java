package com.roleon.scorecard.model;

public class User {

    public static final String TABLE = "user";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PASSWORD = "user_password";
    public static final String KEY_CREATED_AT = "created_at";

    private int user_id;
    private String user_name;
    private String user_password;
    private String created_at;

    public int getId() { return user_id; }

    public void setId(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getPassword() {
        return user_password;
    }

    public void setPassword(String password) {
        this.user_password = password;
    }

    public String getCreated_at() { return created_at; }

    public void setCreated_at(String created_at) { this.created_at = created_at; }
}