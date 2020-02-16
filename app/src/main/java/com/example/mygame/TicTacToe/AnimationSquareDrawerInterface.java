package com.example.mygame.TicTacToe;

import android.widget.ImageButton;

import androidx.annotation.DrawableRes;

import com.example.mygame.TicTacToe.Listeners.ListenerInterface;

public interface AnimationSquareDrawerInterface {
    public void drawSquare(ImageButton v, @DrawableRes int resId, ListenerInterface listener);
}
