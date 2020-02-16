package com.example.mygame.TicTacToe.Listeners;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.util.Pair;

import com.example.mygame.R;
import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.Player;

import java.util.Vector;

public class ClientListener implements ListenerInterface {

    private int x;
    private int y;
    private BasicGame game;


    public ClientListener(int x, int y, BasicGame game){
        this.x = x;
        this.y = y;
        this.game = game;
    }

    @Override
    public void onClick(View v) {
        if(game.isGameStarted() && !game.getIsTurnMaking()) {
            game.setIsTurnMaking(true);
            ImageButton button = (ImageButton) v;
            int duration = 0;
            if(game.makeTurn(x, y)) {
                button.setImageResource(game.isTurnOfFirstPlayer() ? R.drawable.cross_anim : R.drawable.circle_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) button.getDrawable();
                animationDrawable.start();
                duration = animationDrawable.getDuration(0)*animationDrawable.getNumberOfFrames();
            }
            new CountDownTimer(duration, duration) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                public  void onFinish() {
                    try {
                        Pair<Player, Vector<Pair<Integer, Integer>>> winner = game.checkWinners();
                        if (winner != null) {
                            game.endGame();
                            InterfaceManager
                                    .getInstance()
                                    .getActivityAsTicTac()
                                    .gameOver(winner);
                        }
                        if (game.isFieldFilled()) {
                            game.endGame();
                            InterfaceManager
                                    .getInstance()
                                    .getActivityAsTicTac()
                                    .gameOver();
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                    game.setIsTurnMaking(false);
                }
            }.start();
        }
    }

    @Override
    public void onFinish() {

    }
}
