package com.runningzou.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

/**
 * <p>
 * Created by runningzou on 2017/9/7.
 */

public class AnimatorBuilder {

    private AnimatorItem mHeadItem = new AnimatorItem();

    private AnimatorItem mCurItem = mHeadItem;

    private ObjectAnimator mCurAnimator;

    private View mCurTarget;

    private ViewAnimatorListener.startListener mStartListener;
    private ViewAnimatorListener.endListener mEndListener;


    public static AnimatorBuilder animate(View view) {
        AnimatorBuilder animatorBuilder = new AnimatorBuilder();
        animatorBuilder.mCurTarget = view;
        return animatorBuilder;
    }

    public AnimatorBuilder keyFrame(String property, Keyframe... keyframe) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe(property, keyframe);
        mCurAnimator = ObjectAnimator.ofPropertyValuesHolder(mCurTarget, holder);
        mCurItem.addAnimator(mCurAnimator);
        return this;
    }

    public AnimatorBuilder property(Property<View, Float> property, float... values) {
        mCurAnimator = ObjectAnimator.ofFloat(mCurTarget, property, values);
        mCurItem.addAnimator(mCurAnimator);
        return this;
    }

    public AnimatorBuilder property(String property, float... values) {
        mCurAnimator = ObjectAnimator.ofFloat(mCurTarget, property, values);
        mCurItem.addAnimator(mCurAnimator);
        return this;
    }


    public AnimatorBuilder property(String property, int... values) {
        mCurAnimator = ObjectAnimator.ofInt(mCurTarget, property, values);
        mCurItem.addAnimator(mCurAnimator);
        return this;
    }

    public AnimatorBuilder property(String property, TypeEvaluator evaluator, Object... values) {
        mCurAnimator = ObjectAnimator.ofObject(mCurTarget, property, evaluator, values);
        mCurItem.addAnimator(mCurAnimator);
        return this;
    }

    public AnimatorBuilder translationX(float... values) {
        dp2px(values);
        return property(View.TRANSLATION_X, values);
    }

    public AnimatorBuilder translationY(float... values) {
        dp2px(values);
        return property(View.TRANSLATION_Y, values);
    }

    public AnimatorBuilder x(float... values) {
        dp2px(values);
        return property(View.X, values);
    }

    public AnimatorBuilder y(float... values) {
        dp2px(values);
        return property(View.Y, values);
    }

    public AnimatorBuilder alpha(float... values) {
        return property(View.ALPHA, values);
    }

    public AnimatorBuilder scaleX(float... values) {
        return property(View.SCALE_X, values);
    }

    public AnimatorBuilder scaleY(float... values) {
        return property(View.SCALE_Y, values);
    }

    public AnimatorBuilder rotationX(float... values) {
        return property(View.ROTATION_X, values);
    }

    public AnimatorBuilder rotationY(float... values) {
        return property(View.ROTATION_Y, values);
    }

    public AnimatorBuilder rotation(float... values) {
        return property(View.ROTATION, values);
    }


    public AnimatorBuilder backgroundColor(int... colors) {
        AnimatorBuilder factory = property("backgroundColor", colors);
        mCurAnimator.setEvaluator(new ArgbEvaluator());
        return factory;
    }

    public AnimatorBuilder textColor(int... colors) {

        if (mCurTarget instanceof TextView) {
            AnimatorBuilder factory = null;
            factory = property("textColor", colors);
            mCurAnimator.setEvaluator(new ArgbEvaluator());
            return factory;
        } else {
            throw new RuntimeException("only textview can set textColor");
        }

    }

    public AnimatorBuilder parentTop(int topMargin) {
        topMargin = DistanceUtil.dp2px(topMargin);
        return property(View.Y, topMargin);
    }

    public AnimatorBuilder parentLeft(int leftMargin) {
        leftMargin = DistanceUtil.dp2px(leftMargin);
        return property(View.X, leftMargin);
    }

    public AnimatorBuilder parentRignt(int rightMargin) {
        rightMargin = DistanceUtil.dp2px(rightMargin);
        View view = (View) mCurTarget.getParent();
        return property(View.X, view.getWidth() - rightMargin - mCurTarget.getWidth());
    }

    public AnimatorBuilder parentBottom(int bottomMargin) {
        bottomMargin = DistanceUtil.dp2px(bottomMargin);
        View view = (View) mCurTarget.getParent();
        return property(View.Y, view.getHeight() - bottomMargin - mCurTarget.getBottom());
    }

    public AnimatorBuilder leftof(View view, int rightMargin) {
        rightMargin = DistanceUtil.dp2px(rightMargin);
        return property(View.X, view.getLeft() - rightMargin - mCurTarget.getWidth());
    }

    public AnimatorBuilder rightof(View view, int leftMargin) {
        leftMargin = DistanceUtil.dp2px(leftMargin);
        return property(View.X, view.getRight() + leftMargin);
    }

    public AnimatorBuilder bottomof(View view, int topMargin) {
        topMargin = DistanceUtil.dp2px(topMargin);
        return property(View.Y, view.getBottom() + topMargin);
    }

    public AnimatorBuilder topOf(View view, int bottomMargin) {
        bottomMargin = DistanceUtil.dp2px(bottomMargin);
        return property(View.Y, view.getTop() - bottomMargin - mCurTarget.getHeight());
    }


    public AnimatorBuilder setStartListener(ViewAnimatorListener.startListener startListener) {
        mStartListener = startListener;
        return this;
    }

    public AnimatorBuilder setEndListener(ViewAnimatorListener.endListener endListener) {
        mEndListener = endListener;
        return this;
    }

    /**
     * 移动到和 view 水平对齐
     *
     * @param view
     * @return
     */
    public AnimatorBuilder sameHorizontalCenterLineaAs(View view) {

        return property(View.Y, ((view.getTop() + view.getBottom()) / 2) - mCurTarget.getHeight() / 2);
    }

    /**
     * 移动到和 view 垂直对齐
     *
     * @param view
     * @return
     */
    public AnimatorBuilder sameVerticalCenterLineAs(View view) {
        return property(View.X, ((view.getLeft() + view.getRight()) / 2) - mCurTarget.getWidth() / 2);
    }

    public AnimatorBuilder interpolator(Interpolator interpolator) {
        mCurAnimator.setInterpolator(interpolator);
        return this;
    }

    public AnimatorBuilder pivotX(float pivotX) {
        pivotX = DistanceUtil.dp2px(pivotX);
        this.mCurTarget.setPivotX(pivotX);
        return this;
    }

    public AnimatorBuilder pivotY(float pivotY) {
        pivotY = DistanceUtil.dp2px(pivotY);
        this.mCurTarget.setPivotY(pivotY);
        return this;
    }

    public AnimatorBuilder delay(long delay) {
        this.mCurAnimator.setStartDelay(delay);
        return this;
    }


    public AnimatorBuilder duration(long duration) {
        mCurItem.setDuration(duration);
        return this;
    }


    public AnimatorBuilder with(View view) {
        mCurTarget = view;
        return this;
    }

    public AnimatorBuilder after(View view) {

        this.mCurTarget = view;

        AnimatorItem item = new AnimatorItem();
        mCurItem.setNext(item);
        item.setPre(mCurItem);

        mCurItem = item;
        return this;
    }

    /**
     * 仅支持多个同步动画
     * 不支持前后多个顺序执行的动画
     *
     * @param percent
     */
    public void setPercent(float percent) {
        setPercent(percent, mHeadItem);
    }

    private void setPercent(final float percent, AnimatorItem item) {


        for (Animator animator : item.createAnimatorset().getChildAnimations()) {
            if (animator instanceof ValueAnimator) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                valueAnimator.setCurrentPlayTime((long) (percent * valueAnimator.getDuration()));
            }
        }
    }

    public void start(final AnimatorItem item) {

        AnimatorSet animatorSet = item.createAnimatorset();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (item == mHeadItem) {
                    if (mStartListener != null) {
                        mStartListener.onAnimatorStart();
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (item.getNext() != null) {
                    start(item.getNext());
                } else {
                    if (mEndListener != null) {
                        mEndListener.onAnimatorEnd();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animatorSet.start();

    }

    public void start() {
        start(mHeadItem);
    }

    private void dp2px(float... values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = DistanceUtil.dp2px(values[i]);
        }
    }

    private void dp2px(int... values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = DistanceUtil.dp2px(values[i]);
        }
    }

    public void pause() {
        AnimatorItem item = mHeadItem;
        while (item != null && item.createAnimatorset().isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                item.createAnimatorset().pause();

            } else {
                item.createAnimatorset().end();
            }
            item = item.getNext();
        }
    }

    public void resume() {
        AnimatorItem item = mHeadItem;
        while (item != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                item.createAnimatorset().resume();
            }
            item = item.getNext();
        }
    }

}
