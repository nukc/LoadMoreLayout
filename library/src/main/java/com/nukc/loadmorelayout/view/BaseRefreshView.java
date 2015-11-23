package com.nukc.loadmorelayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.nukc.loadmorelayout.LoadMoreLayout;

/**
 * Created by C on 2015/9/27.
 */
public abstract class BaseRefreshView extends FrameLayout {
    protected LoadMoreLayout.OnLoadMoreListener mListener;

    public BaseRefreshView(Context context) {
        super(context);
    }

    public BaseRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 上拉比例(offsetY / mBoundedLoadMoreHeight)
     * @param fraction
     */
    public abstract void onPulling(float fraction);

    /**
     * 正在加载
     */
    public abstract void onLoadMore();

    /**
     * 停止刷新
     */
    public abstract void stop();

    public void setListener(LoadMoreLayout.OnLoadMoreListener Listener) {
        this.mListener = Listener;
    }

}
