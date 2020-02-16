package com.example.mygame.TicTacToe.Game;

import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mygame.TicTacToe.Game.Messangers.MessageReciever;
import com.example.mygame.TicTacToe.Game.Messangers.MessageSender;
import com.example.mygame.TicTacToe.Game.Messangers.Subscriber;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.multi.GameParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiplayerGameClient extends BasicGame implements Subscriber {

    private boolean isYourTurn;
    private boolean requestReceiving;
    private String roomName;
    private boolean synchronizationComplete;
    MessageReciever messageReciever;
    MessageSender messageSender;

    ImageButton buttons[][];
    GameParams gameParams;

    @Override
    public boolean isTurnOfFirstPlayer() {
        return isYourTurn;
    }

    public MultiplayerGameClient(int height, int width, int numberOfSymbolsToWin, /*final TicTacToeActivity parent,*/ final GameParams gameParamsIns, String roomName){
        super(height,width, numberOfSymbolsToWin, 2 );

        messageReciever = new MessageReciever(roomName);
        messageSender = new MessageSender(roomName);
        MessageReciever.subscribe(this);
        if(numberOfFirstPlayer == 0) numberOfFirstPlayer++;
        if(numberOfFirstPlayer == 1) numberOfFirstPlayer = 0;
        this.gameParams = gameParamsIns;
        isYourTurn = false;
        this.roomName = roomName;
        requestReceiving = false;
        isGameStarted = true;
        isTurnMaking = false;
        synchronizationComplete = false;
        FirebaseDatabase
                .getInstance()
                .getReference("games")
                .child(roomName)
                .setValue(gameParams);
        FirebaseDatabase
                .getInstance()
                .getReference("games")
                .child(roomName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GameParams newGameParams = (GameParams)dataSnapshot.getValue(GameParams.class);
                        if(gameParams.getX() == -1 && !synchronizationComplete) {
                            gameParams = newGameParams;
                            synchronizationComplete = true;
                        }
                        /*if(newGameParams != null
                                    && newGameParams.getFirstPlayerName().equals(gameParams.getFirstPlayerName())
                                    && newGameParams.getSecondPlayerName().equals(gameParams.getSecondPlayerName())
                                    && newGameParams.getX() >= 0
                                    && newGameParams.getX() < field.length
                                    && newGameParams.getY() >= 0
                                    && newGameParams.getY() < field[0].length)
                                if(gameParams.getX() != newGameParams.getX()
                                        || gameParams.getY() != newGameParams.getY())
                                {
                                    requestReceiving = true;
                                    gameParams = newGameParams;
                                    buttons[newGameParams.getX()][newGameParams.getY()].callOnClick();
                                    requestReceiving = false;
                                    isTurnMaking = true;
                                }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(InterfaceManager.getInstance().getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        super.start(new String[]{gameParams.getFirstPlayerName(), gameParams.getSecondPlayerName()});
        super.resetPlayers(new String[]{gameParams.getFirstPlayerName(), gameParams.getSecondPlayerName()});
    }

    @Override
    public void update(GameParams gameParams) {
        if(gameParams.getY() != this.gameParams.getY() || gameParams.getX() != this.gameParams.getX())
        if(gameParams.getY() > -1 && gameParams.getX() > -1){
            requestReceiving = true;
            buttons[gameParams.getX()][gameParams.getY()].callOnClick();
            requestReceiving = false;
            isTurnMaking = true;
            this.gameParams = gameParams;
        }
    }

    @Override
    public void disconnected() {
        try {
            InterfaceManager.getInstance().getActivityAsTicTac().disconnected(gameParams.getSecondPlayerName());
            disconnected = true;
            InterfaceManager.getInstance().getActivityAsTicTac().isDisconnected();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    boolean disconnected = false;

    @Override
    public void onExit() {
        MessageReciever.removeSubscribe(this);
        if(!disconnected) messageSender.sendSecondPlayer(gameParams, "");
        try {
            InterfaceManager.getInstance().getActivityAsTicTac().disconnected(gameParams.getFirstPlayerName());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setButtons(ImageButton[][] buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean makeTurn(int x, int y){
        if(isYourTurn){
            boolean result = super.makeTurn(x, y);
            if (!result) return false;
            if (numberOfFirstPlayer == 0)
                field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer + 1] : players[numberOfFirstPlayer]);
            else field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer] : players[numberOfFirstPlayer -1 ]);
            isYourTurn = false;
            gameParams.setX(x);
            gameParams.setY(y);
            FirebaseDatabase
                    .getInstance()
                    .getReference("games")
                    .child(roomName)
                    .setValue(gameParams);
            switchPlayers();
            return true;
        }
        return makeTurnAsAnotherPlayer(x, y);
    }

    public boolean makeTurnAsAnotherPlayer(int x, int y){
        if(requestReceiving){
            boolean result = super.makeTurn(x, y);
            if (!result) return false;
            if (numberOfFirstPlayer == 0)
                field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer + 1] : players[numberOfFirstPlayer]);
            else field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer] : players[numberOfFirstPlayer -1 ]);
            isYourTurn = true;
            switchPlayers();
            return true;
        }
        return false;
    }
}
