package com.runningzou.leptanimator.animators;

import android.view.View;

import com.runningzou.library.AnimatorBuilder;
import com.runningzou.library.LeptAnimator;

/**
 * Created by runningzou on 2017/9/8.
 */

public class BounceAnimator extends LeptAnimator {


    public BounceAnimator(View... view) {
        super(view);

    }

    @Override
    public AnimatorBuilder prepare(View... target) {
        if (target.length >= 1) {
            return AnimatorBuilder
                    .animate(target[0])
                    .translationX(0, 12, -12, 11, -11, 7, -7, 3, -3, 0)
                    .duration(1000);
        } else {
            throw new RuntimeException("BounceAnimator needs at least one view");
        }

    }
}
