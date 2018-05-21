package com.roleon.scorecard.model;

public class Result {

    private int id;
    private int result;
    private String win_loss_drawn;

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

    public String getWin_loss_drawn() {
        return win_loss_drawn;
    }

    public void setWin_loss_drawn(String win_loss_drawn) {
        this.win_loss_drawn = win_loss_drawn;
    }
}
