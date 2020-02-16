package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mygame.R;
import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.multi.GameParams;

public class MultiplayerGameBuilder extends BasicGameBuilder {
    GameParams gameParams;
    Bundle bundle;

    public MultiplayerGameBuilder(Bundle bundle){
        this.bundle = bundle;
    }

    @Override
    public BasicGame build() {
        TextView roomName = (TextView) InterfaceManager.getInstance().getView(R.id.roomName);
        TextView firstPlayer = (TextView) InterfaceManager.getInstance().getView(R.id.first_player_name);

        roomName.setText(bundle.getString("roomName", "1"));
        firstPlayer.setText(bundle.getString("playerName", "Lena"));

        gameParams = new GameParams(firstPlayer.getText().toString(), "", -1, -1, "");
        return null;
    }
}
