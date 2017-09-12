package com.runningzou.library;

import android.view.View;

/**
 * Created by runningzou on 2017/9/8.
 */

public abstract class LeptAnimator {

    private AnimatorBuilder mBuilder;

    private View[] mViews;

    private ViewAnimatorListener.startListener mStartListener;
    private ViewAnimatorListener.endListener mEndListener;

    public LeptAnimator(View... views) {
        mViews = views;
    }

    public abstract AnimatorBuilder prepare(View... targets);


    public void start() {
        if (mViews.length >= 1) {
            mViews[0].post(new Runnable() {
                @Override
                public void run() {
                    if (mBuilder == null) {
                        mBuilder = prepare(mViews);
                        mBuilder.setStartListener(mStartListener);
                        mBuilder.setEndListener(mEndListener);
                    }
                    mBuilder.start();
                }
            });
        }
    }

    public void setPercent(final float percent) {
        if (mViews.length >= 1) {
            mViews[0].post(new Runnable() {
                @Override
                public void run() {
                    if (mBuilder == null) {
                        mBuilder = prepare(mViews);
                        mBuilder.setStartListener(mStartListener);
                        mBuilder.setEndListener(mEndListener);
                        mBuilder.setPercent(percent);
                    } else {
                        mBuilder.setPercent(percent);
                    }
                }
            });
        }
    }

    public void pause() {
        if (mBuilder != null) {
            mBuilder.pause();
        }
    }

    public void resume() {
        if (mBuilder != null) {
            mBuilder.resume();
        }
    }

    public void setStartListener(ViewAnimatorListener.startListener startListener) {
        mStartListener = startListener;
    }

    public void setEndListener(ViewAnimatorListener.endListener endListener) {
        mEndListener = endListener;
    }
}
