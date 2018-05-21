package com.roleon.scorecard.model;

public class Result {

    private int id;
    private int result;
    private String win_loose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getWin_loose() {
        return win_loose;
    }

    public void setWin_loose(String win_loose) {
        this.win_loose = win_loose;
    }
}
