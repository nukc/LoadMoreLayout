package com.nukc.loadmorelayout.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nukc.loadmorelayout.R;
import com.nukc.loadmorelayout.util.Utils;

/**
 * Created by C on 2015/9/27.
 */
public class RainRefreshView extends BaseRefreshView {
    private int mHeight;
    private int mWaveAnimeHeight;

    private RainView mRainView;
    private WaveView mWaveView;
    private TextView tvTip;

    public RainRefreshView(Context context) {
        this(context, null);
    }

    public RainRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int rainViewHeight = 0;
        if (!isInEditMode()) {
            mHeight = getResources().getDimensionPixelSize(R.dimen.view_footer_height);
            rainViewHeight = getResources().getDimensionPixelSize(R.dimen.bounded_load_more);
            mWaveAnimeHeight = -mHeight >> 2;
        }

        mWaveView = new WaveView(context);
        mWaveView.setmViewHeight(mHeight);
        mWaveView.setmMinHeight(mHeight - rainViewHeight);
        addView(mWaveView);

        mRainView = new RainView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, rainViewHeight);
        layoutParams.gravity = Gravity.BOTTOM;
        mRainView.setLayoutParams(layoutParams);
        addView(mRainView);

        tvTip = new TextView(context);
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, Utils.convertDpToPixel(context, 10));
        layoutParams.gravity = Gravity.BOTTOM;
        tvTip.setLayoutParams(layoutParams);
        tvTip.setGravity(Gravity.CENTER);
        tvTip.setTextColor(getResources().getColor(android.R.color.white));
        addView(tvTip);

        mRainView.setVisibility(GONE);
    }

    public void onPulling(float fraction) {
        mWaveView.setmWaveHeight((int) (mHeight * Math.max(0, fraction - 1)));

        mWaveView.invalidate();

        if (Utils.convertDpToPixel(getContext(), mHeight / 2) > (Utils.convertDpToPixel(getContext(), mHeight / 2) * Utils.limitValue(1, fraction))) {
            tvTip.setText(getContext().getString(R.string.pull_up_to_load));
        } else {
            tvTip.setText(getContext().getString(R.string.release_to_load));
        }
    }

    public void onLoadMore() {
        tvTip.setText(getContext().getString(R.string.loading));
        mRainView.setVisibility(View.VISIBLE);
        mRainView.StartRain();
        ValueAnimator animator = ValueAnimator.ofInt(-mWaveView.getmWaveHeight(), 0, mWaveAnimeHeight, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.d("anim", "value--->" + (int) animation.getAnimatedValue());
                mWaveView.setmWaveHeight((int) animation.getAnimatedValue());
                mWaveView.invalidate();
            }
        });
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000);
        animator.start();

        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    public void stop() {
        mRainView.stopRain();
        tvTip.setText(null);
    }

}
