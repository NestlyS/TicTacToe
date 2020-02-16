package com.example.mygame;

import com.example.mygame.TicTacToe.Game.Game;
import com.example.mygame.TicTacToe.Player;
import com.example.mygame.TicTacToe.WinnerCheckHorizontal;
import com.example.mygame.TicTacToe.WinnerCheckInterface;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Game game = new Game(3,3,2,3, null);
        game.start();
        game.setField(1 ,0, 0);
        game.setField(1 ,1, 0);
        game.setField(1 ,2, 0);
        //game.setField(0 ,3, 0);
        WinnerCheckInterface WinnerCheck = new WinnerCheckHorizontal(game);
        Player resultP = WinnerCheck.checkWinner();
        String result = "";
        if(resultP != null) result = resultP.getSymbol();
        String expected = "X";
        assertEquals(expected, result);
    }


}