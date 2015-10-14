package com.nukc.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.nukc.library.R;

/**
 * Created by C on 2015/9/27.
 */
public class WaveView extends View {

    private int mViewHeight;
    private int mWaveHeight;
    private int mMinHeight;

    Path path;
    Paint paint;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        path = new Path();
        paint = new Paint();
        paint.setColor(Color.argb(150, 43, 43, 43));
        paint.setAntiAlias(true);
        setBackgroundResource(R.drawable.bg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();
        path.moveTo(0, mViewHeight);
        path.lineTo(0, mMinHeight);
        path.quadTo(getMeasuredWidth() / 2, mMinHeight - mWaveHeight, getMeasuredWidth(), mMinHeight);
        path.lineTo(getMeasuredWidth(), mViewHeight);

        canvas.drawPath(path, paint);
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
