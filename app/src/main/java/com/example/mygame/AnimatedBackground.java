package com.example.mygame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

public class AnimatedBackground {

    private AnimatorSet animatorSet;

    public void animate(final ViewGroup layout, Activity activity) {
        ValueAnimator.AnimatorUpdateListener valueAnimator = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layout.setBackgroundColor((int)animator.getAnimatedValue());
            }
        };

        int colorFrom = ContextCompat.getColor(activity, R.color.colorFirst);//Color.rgb(130, 75, 75);
        int colorTo = ContextCompat.getColor(activity, R.color.colorSecond);//Color.rgb(130, 130, 75);
        final ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation1.addUpdateListener(valueAnimator);

        colorFrom = ContextCompat.getColor(activity, R.color.colorSecond);//Color.rgb(130, 130, 75);
        colorTo = ContextCompat.getColor(activity, R.color.colorThird);//Color.rgb(75, 130, 75);
        final ValueAnimator colorAnimation2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation2.addUpdateListener(valueAnimator);

        colorFrom = ContextCompat.getColor(activity, R.color.colorThird);//(75, 130, 75);
        colorTo = ContextCompat.getColor(activity, R.color.colorSecond);//Color.rgb(130, 130, 75);
        final ValueAnimator colorAnimation3 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation3.addUpdateListener(valueAnimator);

        colorFrom = ContextCompat.getColor(activity, R.color.colorSecond);//Color.rgb(130, 130, 75);
        colorTo = ContextCompat.getColor(activity, R.color.colorFirst);//Color.rgb(130, 75, 75);
        final ValueAnimator colorAnimation4 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation3.addUpdateListener(valueAnimator);

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(colorAnimation1,colorAnimation2,colorAnimation3,colorAnimation4);
        animatorSet.setDuration(4000);
        animatorSet.addListener(new
                            AnimatorListenerAdapter() {

                                private boolean mCanceled;

                                @Override
                                public void onAnimationStart(Animator animation) {
                                    mCanceled = false;
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    mCanceled = true;
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (!mCanceled) {
                                        animation.start();
                                    }
                                }

                            });
        animatorSet.start();
    }

    public void stop(){
        animatorSet.cancel();
    }
}
