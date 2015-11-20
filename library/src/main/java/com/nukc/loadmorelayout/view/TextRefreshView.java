package com.nukc.loadmorelayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nukc.loadmorelayout.R;
import com.nukc.loadmorelayout.util.Utils;

/**
 * Created by C on 19/11/2015.
 */
public class TextRefreshView extends BaseRefreshView{

    private int mHeight;

    private TextView mTextView;

    public TextRefreshView(Context context) {
        this(context, null);
    }

    public TextRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            mHeight = getResources().getDimensionPixelSize(R.dimen.view_footer_height);
        }

        mTextView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, Utils.convertDpToPixel(context, 20));
        layoutParams.gravity = Gravity.BOTTOM;
        mTextView.setLayoutParams(layoutParams);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(getResources().getColor(android.R.color.black));
        addView(mTextView);
    }

    @Override
    public void onPulling(float fraction) {
        if (Utils.convertDpToPixel(getContext(), mHeight / 2) > (Utils.convertDpToPixel(getContext(), mHeight / 2) * Utils.limitValue(1, fraction))) {
            mTextView.setText(getContext().getString(R.string.pull_up_to_load));
        } else {
            mTextView.setText(getContext().getString(R.string.release_to_load));
        }
    }

    @Override
    public void onLoadMore() {
        mTextView.setText(getContext().getString(R.string.loading));
        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    @Override
    public void stop() {

    }



}
