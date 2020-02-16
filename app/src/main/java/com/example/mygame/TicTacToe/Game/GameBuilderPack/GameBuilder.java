package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import com.example.mygame.Bot.Bot;
import com.example.mygame.Bot.BotCircle;
import com.example.mygame.TicTacToe.Game.Game;

public class GameBuilder extends BasicGameBuilder {


    @Override
    public Game build() {
        Game game = new Game(HEIGHT_OF_THE_GAME, WIDTH_OF_THE_GAME, NUMBER_OF_PLAYERS, NUMBER_SYM_TO_WIN);
        if(bot != null) {
            bot.setGame(game);
            game.setBot(bot);
        }
        game.start(new String[]{"X", "O"});
        return game;
    }

    @Override
    public void setBot() {
        if(NUMBER_OF_PLAYERS == 1) {
            bot = new BotCircle(buttons, NUMBER_SYM_TO_WIN, DIFFICULTY);
        }
    }
}
