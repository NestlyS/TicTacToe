package com.example.mygame.TicTacToe.Listeners;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.util.Pair;

import com.example.mygame.R;
import com.example.mygame.TicTacToe.AnimationSquareDrawer;
import com.example.mygame.TicTacToe.AnimationSquareDrawerInterface;
import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.Game.Game;
import com.example.mygame.TicTacToe.Game.MultiplayerGame;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.Player;

import java.util.Vector;

class Listener implements ListenerInterface{

    private int x;
    private int y;
    BasicGame game;

    Listener(int x, int y, BasicGame game){
        this.x = x;
        this.y = y;
        this.game = game;
    }

    @Override
    public void onClick(View v) {
        if(game.isGameStarted() && !game.getIsTurnMaking()) {
            game.setIsTurnMaking(true);
            ImageButton button = (ImageButton) v;
            Player player = new Player("");
            int duration = 0;
            int anim = game.isTurnOfFirstPlayer() ?
                    R.drawable.cross_anim
                    :
                    R.drawable.circle_anim;
            if(game.makeTurn(x, y)) {
                AnimationSquareDrawerInterface ASDI = new AnimationSquareDrawer();
                ASDI.drawSquare((ImageButton) v, anim, this);
            } else {
                game.setIsTurnMaking(false);
            }
        }
    }

    @Override
    public void onFinish() {
        Pair<Player, Vector<Pair<Integer, Integer>>> winner = game.checkWinners();
        try {
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
            Toast.makeText(InterfaceManager.getInstance().getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        game.setIsTurnMaking(false);
        if(game instanceof Game) {
            ((Game) game).makeTurnAsBot();
        }
    }
}
