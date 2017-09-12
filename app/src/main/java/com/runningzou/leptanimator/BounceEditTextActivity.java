package com.runningzou.leptanimator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.runningzou.leptanimator.animators.BounceAnimator;
import com.runningzou.library.ViewAnimatorListener;

/**
 * Created by runningzou on 2017/9/8.
 */

public class BounceEditTextActivity extends AppCompatActivity {

    private EditText mEditText1;
    private EditText mEditText2;

    private Button mButton;

    private BounceAnimator mAnimator1;
    private BounceAnimator mAnimator2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bounce_edittext);

        mEditText1 = (EditText) findViewById(R.id.edittxt1);
        mEditText2 = (EditText) findViewById(R.id.edittxt2);
        mButton = (Button) findViewById(R.id.btn);

        mAnimator1 = new BounceAnimator(mEditText1);
        mAnimator2 = new BounceAnimator(mEditText2);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimator1.start();
                mAnimator2.start();
            }
        });

        mAnimator1.setStartListener(new ViewAnimatorListener.startListener() {
            @Override
            public void onAnimatorStart() {
                Log.d("tag","动画开始");
            }
        });

        mAnimator1.setEndListener(new ViewAnimatorListener.endListener() {
            @Override
            public void onAnimatorEnd() {
                Log.d("tag","动画结束");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimator1.resume();
        mAnimator2.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAnimator1.pause();
        mAnimator2.pause();
    }
}
