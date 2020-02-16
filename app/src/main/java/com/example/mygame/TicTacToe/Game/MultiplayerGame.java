package com.example.mygame.TicTacToe.Game;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mygame.R;
import com.example.mygame.TicTacToe.Game.Messangers.MessageReciever;
import com.example.mygame.TicTacToe.Game.Messangers.MessageSender;
import com.example.mygame.TicTacToe.Game.Messangers.MessageSenderPlus;
import com.example.mygame.TicTacToe.Game.Messangers.Subscriber;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.multi.GameParams;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


public class MultiplayerGame extends BasicGame implements Subscriber {

    static final String CHECK_STATE = "CHECK";
    static final String ANSWER = "OK";
    static final String UNKNOWN_MESSAGE = "UNKNOWN";

    MessageSender messageSender;
    MessageReciever messageReciever;


    private GameParams gameParams;
    private Timer timer;
    private final TextView secondPlayer;
    private boolean isYourTurn;
    private boolean requestReceiving;
    private String roomName;

    private boolean waitingAnotherPlayerPhase = true;
    private boolean playingPhase = false;

    public MultiplayerGame(int height, int weight, int numberOfSymbolsToWin/*, TicTacToeActivity parent*/, GameParams gameParams){
        //super(height,weight, numberOfSymbolsToWin, parent, 2);
        super(height,weight, numberOfSymbolsToWin, 2);
        this.gameParams = gameParams;
        secondPlayer = InterfaceManager
                .getInstance()
                .getActivity()
                .findViewById(R.id.second_player_name);
        isYourTurn = true;
        requestReceiving = false;
    }

    public void start(final String roomName, final ImageButton[][] buttons) {
        super.start(new String[]{gameParams.getFirstPlayerName(), gameParams.getSecondPlayerName()});
        isGameStarted = false;

        this.roomName = roomName;
        messageSender = new MessageSender(roomName);
        messageReciever = new MessageReciever(roomName);
        MessageReciever.subscribe(this);

        DatabaseReference newRef = FirebaseDatabase
                .getInstance()
                .getReference("games");
        newRef.child(roomName)
                .setValue(
                        gameParams
                );
        newRef.child(roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(waitingAnotherPlayerPhase && dataSnapshot.getValue(GameParams.class) != null) {
                    gameParams = (GameParams)dataSnapshot.getValue(GameParams.class);
                    if (!gameParams.getSecondPlayerName().equals("")) {
                        connected(gameParams.getSecondPlayerName());
                        waitingAnotherPlayerPhase = false;
                        playingPhase = true;
                        isGameStarted = true;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InterfaceManager
                        .getInstance()
                        .getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        activateTimer();
    }

    @Override
    public void playerXWon(String nameOfWinner) {
            MessageSenderPlus
                    .getInstance(roomName, scores[0]);
        super.playerXWon(nameOfWinner);
    }

    @Override
    public void update(GameParams gameParams) {
        if(!gameParams.getMessage().equals(this.gameParams.getMessage())){
            switch (gameParams.getMessage()) {
                case CHECK_STATE:
                    messageSender.sendMessage(gameParams, ANSWER);
                    break;
                default:
                    messageSender.sendMessage(gameParams, UNKNOWN_MESSAGE);
            }

        }
        if(gameParams.getY() != this.gameParams.getY() || gameParams.getX() != this.gameParams.getX()){
            requestReceiving = true;
            try {
                buttons[gameParams.getX()][gameParams.getY()].callOnClick();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            isYourTurn = true;
            requestReceiving = false;
            switchPlayers();

        }
        if(!gameParams.getSecondPlayerName().equals(this.gameParams.getSecondPlayerName())){
            if(gameParams.getSecondPlayerName().equals("")) {
                Snackbar.make(InterfaceManager
                                .getInstance()
                                .getActivity()
                                .findViewById(R.id.background_tictac),
                        InterfaceManager
                                .getInstance()
                                .getActivity()
                                .getString(R.string.message_first_part) + " "
                                + this.gameParams.getSecondPlayerName() + " disconnected.",
                        Snackbar.LENGTH_LONG).show();
                //scores[0]++;
                scores[1] = 0;
                playingPhase = false;
                isGameStarted = false;
                waitingAnotherPlayerPhase = true;
                messageSender.sendCoordinates(gameParams, -1, -1);
                resetField();
                try {
                    InterfaceManager
                            .getInstance()
                            .getActivityAsTicTac()
                            .refresh();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                activateTimer();
            }
        }

    }

    public boolean makeTurnAsAnotherPlayer(int x, int y){
        if(requestReceiving){
            boolean result = super.makeTurn(x, y);
            if (!result) return false;
            if (numberOfFirstPlayer == 0)
                field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer] : players[numberOfFirstPlayer + 1]);
            else field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer - 1] : players[numberOfFirstPlayer]);
            setWhichTurnNow(true);
            return true;
        }
        return false;
    }

    @Override
    public void disconnected() {
    }

    private void activateTimer(){
        timer = new Timer();

        timer.schedule(new TimerTask() {
            String message = "";
            int dotsNum = 0;
            @Override
            public void run() {
                try {
                    if(dotsNum > 3) {
                        dotsNum = 0;
                        //zaglushka(roomName);
                    }
                    message = InterfaceManager
                            .getInstance()
                            .getActivity().getString(R.string.waiting);
                    for (int j = 0; j < dotsNum; j++)
                        message += ".";
                    InterfaceManager
                            .getInstance()
                            .getActivity().runOnUiThread(runnable);
                    dotsNum++;
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    secondPlayer.setText(message);
                }
            };
        }, 0, 750);
    }

    @Override
    protected void resetPlayers(String[] namePlayers){
        super.resetPlayers(namePlayers);
        TextView textView = (TextView)InterfaceManager.getInstance().getActivity().findViewById(R.id.turn_t);
        textView.setText(gameParams.getFirstPlayerName());
    }


    public void stop(){
        timer.cancel();
    }

    private void connected(String secondPlayerName){
        stop();
        Snackbar.make(InterfaceManager.getInstance().getActivity().findViewById(R.id.background_tictac), "Connected with "+secondPlayerName+"!", Snackbar.LENGTH_LONG).show();
        secondPlayer.setText(secondPlayerName);
        resetPlayers(new String[]{gameParams.getFirstPlayerName(), gameParams.getSecondPlayerName()});
    }

    public boolean isYourTurnNow(){
        return isYourTurn;
    }

    public void setWhichTurnNow(boolean set){
        isYourTurn = set;
    }

    @Override
    public boolean makeTurn(int x, int y){
        if(isYourTurn){
            boolean result = super.makeTurn(x, y);
            if (!result) return false;
            if (numberOfFirstPlayer == 0)
                field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer] : players[numberOfFirstPlayer + 1]);
            else field[x][y].fill(isYourTurn ? players[numberOfFirstPlayer - 1] : players[numberOfFirstPlayer]);
            setWhichTurnNow(false);
            switchPlayers();
            messageSender.sendCoordinates(gameParams, x, y);
            return true;
        }
        return makeTurnAsAnotherPlayer(x, y);
    }

    @Override
    public boolean isTurnOfFirstPlayer() {
        return isYourTurn;
    }

    @Override
    public void onExit(){
        FirebaseDatabase
                .getInstance()
                .getReference("games")
                .child(roomName)
                .removeValue();
        FirebaseDatabase
                .getInstance()
                .getReference("rooms")
                .child(roomName)
                .removeValue();
        stop();
        MessageReciever.removeSubscribe(this);
        try {
            InterfaceManager.getInstance().getActivityAsTicTac().disconnected(gameParams.getFirstPlayerName());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
