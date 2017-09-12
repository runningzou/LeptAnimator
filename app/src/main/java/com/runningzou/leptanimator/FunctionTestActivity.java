package com.runningzou.leptanimator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.runningzou.library.AnimatorBuilder;
import com.runningzou.library.LeptAnimator;

/**
 * Created by runningzou on 2017/9/12.
 */

public class FunctionTestActivity extends AppCompatActivity {
    private TextView mTxt1;
    private TextView mTxt2;
    private TextView mTxtRefer;

    private float percent = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_test);
        mTxt1 = (TextView) findViewById(R.id.txt_test1);
        mTxt2 = (TextView) findViewById(R.id.txt_test2);
        mTxtRefer = (TextView) findViewById(R.id.txt_refer);

        //单个动画
        final SingleAnimator singleAnimator = new SingleAnimator(mTxt1);

        findViewById(R.id.btn_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleAnimator.start();
            }
        });


        //多个同步动画
        final LeptAnimator mutilSyncAnimator = new LeptAnimator(mTxt1, mTxt2) {
            @Override
            public AnimatorBuilder prepare(View... targets) {
                return new AnimatorBuilder()
                        .animate(mTxt1)
                        .translationY(100)
                        .translationX(150)
                        .alpha(0.5f, 1f)
                        .duration(2000)
                        .with(mTxt2)
                        .rightof(mTxtRefer, 40);

            }
        };

        findViewById(R.id.btn_mutil_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mutilSyncAnimator.start();
            }
        });


        //多个连续动画
        final MutilAsynAnimator mutilAsynAnimator = new MutilAsynAnimator(mTxt1, mTxt2, mTxtRefer);

        findViewById(R.id.btn_mutil_asyn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mutilAsynAnimator.start();
            }
        });


        findViewById(R.id.btn_percent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                percent += 0.1;
                if (percent > 1) {
                    percent = 0;
                }
                mutilSyncAnimator.setPercent(percent);
            }
        });

    }


    public static class SingleAnimator extends LeptAnimator {

        public SingleAnimator(View... views) {
            super(views);
        }

        @Override
        public AnimatorBuilder prepare(View... targets) {
            return AnimatorBuilder.animate(targets[0])
                    .parentRignt(10)
                    .duration(2000);
        }
    }

    public static class MutilAsynAnimator extends LeptAnimator {
        public MutilAsynAnimator(View... views) {
            super(views);
        }

        @Override
        public AnimatorBuilder prepare(View... targets) {
            return AnimatorBuilder.animate(targets[0])
                    .parentRignt(10)
                    .duration(2000)
                    .after(targets[1])
                    .translationY(200)
                    .duration(2000)
                    .after(targets[0])
                    .rightof(targets[2], 10)
                    .after(targets[0])
                    .leftof(targets[2], 10)
                    .with(targets[1])
                    .sameHorizontalCenterLineaAs(targets[2]);
        }
    }

}
