package com.example.mygame.TicTacToe;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.widget.ImageButton;

import androidx.annotation.DrawableRes;

import com.example.mygame.TicTacToe.Listeners.ListenerInterface;

public class AnimationSquareDrawer implements AnimationSquareDrawerInterface{

    @Override
    public void drawSquare(ImageButton v, @DrawableRes int resId, final ListenerInterface listener ) {
        v.setImageResource(resId);
        AnimationDrawable animationDrawable = (AnimationDrawable) v.getDrawable();
        animationDrawable.start();
        int duration = animationDrawable.getDuration(0)*animationDrawable.getNumberOfFrames();
        new CountDownTimer(duration, duration) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            public  void onFinish() {
                listener.onFinish();
            }
        }.start();
    }
}
