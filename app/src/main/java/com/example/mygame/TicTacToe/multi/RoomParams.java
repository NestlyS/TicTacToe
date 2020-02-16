package com.example.mygame.TicTacToe.multi;

public class RoomParams {
    private String playerName;
    private int sizeOfField;
    private int numberOfWins;
    private String roomName;

    public RoomParams(String playerName, int sizeOfField , int numberOfWins, String roomName){
        this.playerName = playerName;
        this.sizeOfField = sizeOfField;
        this.numberOfWins = numberOfWins;
        this.roomName = roomName;
    }

    public RoomParams(){
        playerName = "Kazan";
        sizeOfField = 3;
        numberOfWins = 0;
        roomName = "";
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public void setSizeOfField(int sizeOfField) {
        this.sizeOfField = sizeOfField;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getSizeOfField() {
        return sizeOfField;
    }

    public String getRoomName() {
        return roomName;
    }
}
