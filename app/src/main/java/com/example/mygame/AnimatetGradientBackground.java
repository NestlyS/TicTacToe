package com.example.mygame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

public class AnimatetGradientBackground {
    private AnimationDrawable animationDrawable;

    public void animate(final ViewGroup layout, final Activity activity) {
        Drawable[] layers = new Drawable[]{
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{
                        Color.rgb(130, 75, 75), Color.rgb(130, 130, 75)
                }),
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{
                        Color.rgb(130, 130, 75), Color.rgb(75, 130, 75)
                }),
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{
                        Color.rgb(75, 130, 75), Color.rgb(130, 130, 75)
                }),
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{
                Color.rgb(130, 130, 75), Color.rgb(130, 75, 75)
                })
        };
        final TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
        transitionDrawable.startTransition(5000);
        layout.setBackground(transitionDrawable);
        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                transitionDrawable.resetTransition();
                transitionDrawable.startTransition(5000);
            }
        }, 0, 5000);*/
    }
}
