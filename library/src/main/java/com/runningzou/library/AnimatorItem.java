package com.runningzou.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runningzou on 2017/9/7.
 */

public class AnimatorItem {

    private List<Animator> mAnimators = new ArrayList<>();

    private AnimatorSet mAnimatorSet;

    private AnimatorItem mPre = null;
    private AnimatorItem mNext = null;

    private long mDuration = 500;

    public AnimatorItem() {

    }

    public void addAnimator(ObjectAnimator animator) {
        mAnimators.add(animator);
    }

    public void setPre(AnimatorItem animatorItem) {
        this.mPre = animatorItem;
    }

    public AnimatorItem getPre() {
        return mPre;
    }

    public void setNext(AnimatorItem animatorItem) {
        this.mNext = animatorItem;
    }

    public AnimatorItem getNext() {
        return mNext;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public AnimatorSet createAnimatorset() {
        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mAnimators);
            mAnimatorSet.setDuration(mDuration);
        }
        return mAnimatorSet;
    }


}
