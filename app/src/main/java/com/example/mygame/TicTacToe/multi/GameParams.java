package com.example.mygame.TicTacToe.multi;

public class GameParams {
    private String firstPlayerName;
    private String secondPlayerName;
    private int x;
    private int y;
    private String message;

    public GameParams(String fPN, String sPN, int x, int y, String message){
        firstPlayerName = fPN;
        secondPlayerName = sPN;
        this.x = x;
        this.y = y;
        this.message = message;
    }

    public GameParams(){
        firstPlayerName = "Lena";
        secondPlayerName = "Katya";
        x = -1;
        y = -1;
        message = "";
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getMessage() {
        return message;
    }
}
