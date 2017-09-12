package com.runningzou.leptanimator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.runningzou.library.AnimatorBuilder;
import com.runningzou.library.LeptAnimator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by runningzou on 2017/9/8.
 */

public class ScrollActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CircleImageView mImageView;
    private Button mButton;
    private FrameLayout mFrameLayout;
    private TextView mTextView;

    private LinearLayoutManager mLayoutManager;
    private MyAdapter mMyAdapter = new MyAdapter();

    private LeptAnimator mAnimators;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mFrameLayout = (FrameLayout) findViewById(R.id.framelayout);
        mButton = (Button) findViewById(R.id.follow);
        mImageView = (CircleImageView) findViewById(R.id.avator);
        mTextView = (TextView) findViewById(R.id.username);

        mAnimators = new LeptAnimator(mImageView, mTextView, mButton,mFrameLayout) {

            @Override
            public AnimatorBuilder prepare(View... targets) {
                return AnimatorBuilder.animate(targets[0])
                        .parentTop(16)
                        .parentLeft(16)
                        .pivotY(0)
                        .pivotX(0)
                        .scaleX(0.60f)
                        .scaleY(0.60f)

                        .with(targets[1])
                        .parentTop(30)
                        .parentLeft(120)
                        .with(targets[2])
                        .parentRignt(30)
                        .parentTop(28)

                        .with(targets[3])
                        .pivotX(0)
                        .pivotY(0)
                        .scaleY(0.4f);
            }
        };

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int scrolledY = getScrolledY();
                if (scrolledY < 5) {
                    mAnimators.setPercent(0);
                } else if (scrolledY > dp2px(240)) {
                    mAnimators.setPercent(1f);
                } else if (scrolledY < dp2px(240)) {
                    float percent = (scrolledY * 1.0f) / (dp2px(240) * 1.0f);
                    mAnimators.setPercent(percent);
                }


            }
        });

    }

    public int getScrolledY() {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        View view = mLayoutManager.findViewByPosition(position);
        if (view != null) {
            int scrollY = dp2px(240) + position * dp2px(136) - view.getBottom();
            return scrollY;
        }

        return 0;
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first, parent, false);
                return new MyViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal, parent, false);
                return new MyViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAnimators.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAnimators.resume();
    }
}






