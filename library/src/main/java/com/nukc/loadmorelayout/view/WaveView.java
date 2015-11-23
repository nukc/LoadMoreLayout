package com.nukc.loadmorelayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.nukc.loadmorelayout.R;

/**
 * Created by C on 2015/9/27.
 */
public class WaveView extends View {

    private int mViewHeight;
    private int mWaveHeight;
    private int mMinHeight;

    private Path mPath;
    private Paint mPaint;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.argb(150, 43, 43, 43));
        mPaint.setAntiAlias(true);
        setBackgroundResource(R.drawable.bg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.moveTo(0, mViewHeight);
        mPath.lineTo(0, mMinHeight);
        mPath.quadTo(getMeasuredWidth() / 2, mMinHeight - mWaveHeight, getMeasuredWidth(), mMinHeight);
        mPath.lineTo(getMeasuredWidth(), mViewHeight);

        canvas.drawPath(mPath, mPaint);
    }

    public int getmWaveHeight() {
        return mWaveHeight;
    }

    public void setmWaveHeight(int mWaveHeight) {
        this.mWaveHeight = mWaveHeight;
    }

    public int getmViewHeight() {
        return mViewHeight;
    }

    public void setmViewHeight(int mViewHeight) {
        this.mViewHeight = mViewHeight;
    }

    public int getmMinHeight() {
        return mMinHeight;
    }

    public void setmMinHeight(int mMinHeight) {
        this.mMinHeight = mMinHeight;
    }
}
