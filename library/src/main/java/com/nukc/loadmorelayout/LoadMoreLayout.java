package com.nukc.loadmorelayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

import com.nukc.loadmorelayout.view.BaseRefreshView;
import com.nukc.loadmorelayout.view.FooterView;

/**
 * Created by C on 2015/9/27.
 */
public class LoadMoreLayout extends ViewGroup {
    private static final int INVALID_POINTER = -1;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private int mFooterHeight;
    private int mboundedLoadMore;

    private Interpolator mDecelerateInterpolator;
    private View mTarget;
    private boolean mRefreshing;
    private int mTouchSlop;

    private BaseRefreshView mFooterView;

    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;


    public LoadMoreLayout(Context context) {
        this(context, null);
    }

    public LoadMoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFooterView = new FooterView(context);
        addView(mFooterView);

        mRefreshing = false;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        if (!isInEditMode()) {
            mFooterHeight = getResources().getDimensionPixelSize(R.dimen.view_footer_height);
            mboundedLoadMore = getResources().getDimensionPixelSize(R.dimen.bounded_load_more);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mFooterView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mFooterView)
                    mTarget = child;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        mTarget.layout(left, top, left + width - right, top + height - bottom);
        mFooterView.layout(left, top + height - bottom - mFooterHeight, left + width - right, top + height - bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || mRefreshing) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTarget.offsetTopAndBottom(0);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }

                final float yDiff = y - mInitialMotionY;

                if (yDiff < 0 && !canChildScrollDown() && Math.abs(yDiff) > mTouchSlop){
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = Math.abs(y - mInitialMotionY);

                float offsetY = mDecelerateInterpolator.getInterpolation(yDiff / mFooterHeight / 2) * yDiff / 2;

                if (offsetY < mFooterHeight) {
                    Log.d("offsetY", offsetY + "---" + yDiff);
                    ViewCompat.setTranslationY(mTarget, -offsetY);
                    if (mFooterView != null) {
                        mFooterView.onPulling(offsetY / mboundedLoadMore);
                    }
                }

                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                mIsBeingDragged = false;

                if (ViewCompat.getTranslationY(mTarget) <= -mboundedLoadMore) {
                    createAnimatorTranslationY(mTarget, -mboundedLoadMore);
                    setRefreshing(true);
                } else {
                    mRefreshing = false;
                    createAnimatorTranslationY(mTarget, 0);
                }

                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mFooterView.onLoadMore();
            }else {
                mFooterView.stop();
                createAnimatorTranslationY(mTarget, 0);
            }
        }
    }

    public void createAnimatorTranslationY(final View v, final float h) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(200);
        viewPropertyAnimatorCompat.setInterpolator(mDecelerateInterpolator);
        viewPropertyAnimatorCompat.translationY(h);
        viewPropertyAnimatorCompat.start();
    }

    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                if (absListView.getChildCount()>0)
                {
                    int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
                    return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1 && lastChildBottom <= absListView.getMeasuredHeight();
                }else
                {
                    return false;
                }

            } else {
                return ViewCompat.canScrollVertically(mTarget, 1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        mFooterView.setListener(listener);
    }

    public static interface OnLoadMoreListener{
        public void onLoadMore();
    }
}
