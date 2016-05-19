package com.github.dubulee.coordinatorlayouthelper;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
/**
 * HeaderLayoutBehavior.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class HeaderLayoutBehavior<V extends View> extends ViewOffsetBehavior<HeaderLayout> {
    private static final String TAG = HeaderLayoutBehavior.class.getSimpleName();

    private int mTouchSlop;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;

    private boolean mIsScrolling;

    private int mMinOffset;
    private int mMaxOffset;

    private View mTargetView;

    private int mSkippedOffset;

    private ViewFlinger mViewFlinger;

    private int mScrollRootViewPosition = -1;

    @Deprecated
    private int mScrollViewPosition = -1;

    private CoordinatorLayoutHelperViewPager mViewPager = null;

    private CoordinatorLayoutHelperRecyclerView mScrollView = null;

    private ArrayList<View> mScrollViewList = new ArrayList<>();

    public HeaderLayoutBehavior() {
    }

    public HeaderLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, HeaderLayout child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, HeaderLayout child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec,
                heightUsed);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderLayout child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderLayout child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, HeaderLayout child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        if (null != mViewFlinger) {
            mViewFlinger.cancel();
        }

        if ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            mTargetView = target;

            mIsScrolling = false;
            mSkippedOffset = 0;

            mMinOffset = -(child.getHeight() - child.getFinxedRange());
            mMaxOffset = 0;

            return true;
        }
        return false;
    }

    //TODO Improve Detect Scrollview (like. Recycler View)
    public void setScrollRootViewPosition(int scrollRootViewPosition) {
        mScrollRootViewPosition = scrollRootViewPosition;
    }

    @Deprecated
    //TODO Improve Detect Scrollview (like. Recycler View)
    public void setScrollViewPosition(int scrollViewPosition) {
        mScrollViewPosition = scrollViewPosition;
    }

    //TODO Improve
    private void setScrollView(CoordinatorLayout coordinatorLayout) {
        ViewGroup rootViewGroup = (ViewGroup) coordinatorLayout.getChildAt(mScrollRootViewPosition);

        if (coordinatorLayout.getChildAt(mScrollRootViewPosition) instanceof CoordinatorLayoutHelperViewPager) {
            mViewPager = (CoordinatorLayoutHelperViewPager) coordinatorLayout.getChildAt(mScrollRootViewPosition);
        }


        findHeaderLayout(mViewPager);


//        if (null != rootViewGroup) {
//            if (rootViewGroup.getChildAt(mScrollViewPosition) instanceof CoordinatorLayoutHelperRecyclerView) {
//                mScrollView = (CoordinatorLayoutHelperRecyclerView) rootViewGroup.getChildAt(mScrollViewPosition);
//            } else {
//                mScrollView = null;
//            }
//        } else {
//            mScrollView = null;
//        }
    }

    private void findHeaderLayout(ViewPager rootViewGroup) {
        //TODO
        if (null == rootViewGroup) {
            throw new NullPointerException();
        }

        if (null != mScrollViewList && mScrollViewList.size() > 0) {
            mScrollViewList.clear();
        }

        for (int z = 0; z < rootViewGroup.getAdapter().getCount(); z++) {
            FragmentPagerAdapter pa = (FragmentPagerAdapter) rootViewGroup.getAdapter();
            android.support.v4.app.Fragment fragment = pa.getItem((rootViewGroup.getCurrentItem()));
            View view = fragment.getView();
            if (view instanceof CoordinatorLayoutHelperRecyclerView) {
                mScrollViewList.add(((CoordinatorLayoutHelperRecyclerView) rootViewGroup.getChildAt(z)));
            }
            else if (view instanceof NestedScrollView) {
                mScrollViewList.add(((NestedScrollView) rootViewGroup.getChildAt(z)));
            }
        }

    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, HeaderLayout child, View target, int dx, int dy,
                                  int[] consumed) {
        setScrollView(coordinatorLayout);
        if (!mIsScrolling) {
            mSkippedOffset += dy;

            if (Math.abs(mSkippedOffset) >= mTouchSlop) {
                mIsScrolling = true;
                target.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        if (mIsScrolling) {
            int min = -child.getScrollRange();
            int max = 0;
            int currentOffset = getTopAndBottomOffset();
            int newOffset = Math.min(Math.max(min, currentOffset - dy), max);
            consumed[1] = newOffset - currentOffset;

            View scrollView = mScrollViewList.get(mViewPager.getCurrentItem());
            CoordinatorLayoutHelperRecyclerView recyclerView = null;
            CoordinatorLayoutHelperNestedScrollView nestedScrollView = null;


            if (null != scrollView) {
                if (dy > 0) {
                    setTopAndBottomOffset(newOffset);
                }
                else {
                    if (scrollView instanceof CoordinatorLayoutHelperRecyclerView) {
                        recyclerView = (CoordinatorLayoutHelperRecyclerView) scrollView;
                        if (recyclerView.getVerticalScrollOffset() == 0) {
                            setTopAndBottomOffset(newOffset);
                        }
                    }
                    else if (scrollView instanceof CoordinatorLayoutHelperNestedScrollView) {
                        nestedScrollView = (CoordinatorLayoutHelperNestedScrollView) scrollView;
                        if (nestedScrollView.getVerticalScrollOffset() == 0) {
                            setTopAndBottomOffset(newOffset);
                        }

                    }
                }

            } else {
                if (dy != 0) {
                    setTopAndBottomOffset(newOffset);
                }
            }

        }
    }


    //TODO Improve Fling
    /*
     ** If you want fling, Can vitalize a this method.
     */
//    @Override
//    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, HeaderLayout child, View target,
//                                    float velocityX, float velocityY) {
//        if (mViewFlinger != null) {
//            mViewFlinger.cancel();
//        } else {
//            mViewFlinger = new ViewFlinger(coordinatorLayout);
//        }
//
//        final int targetOffsetRange;
//        final int targetOffset;
//        if (target instanceof ScrollingView) {
//            targetOffsetRange = ((ScrollingView) target).computeVerticalScrollRange() + target.getPaddingTop()
//                    + target.getPaddingBottom();
//            targetOffset = ((ScrollingView) target).computeVerticalScrollOffset();
//        }
//        else {
//            targetOffsetRange = Math.max(0, target.getHeight() - coordinatorLayout.getHeight());
//            targetOffset = target.getScrollY();
//        }
//
//        mViewFlinger.fling((int) velocityY, targetOffset, targetOffsetRange);
//
//        return true;
//    }

    private class ViewFlinger implements Runnable {
        private final ScrollerCompat mScroller;
        private final CoordinatorLayout mCoordinatorLayout;
        private int mLastFlingY;

        public ViewFlinger(CoordinatorLayout coordinatorLayout) {
            mScroller = ScrollerCompat.create(coordinatorLayout.getContext());
            mCoordinatorLayout = coordinatorLayout;
        }

        public void fling(int velocity, int targetOffset, int targetOffsetRange) {
            if (Math.abs(velocity) < mMinFlingVelocity) {
                return;
            }

            velocity = Math.max(-mMaxFlingVelocity, Math.min(velocity, mMaxFlingVelocity));

            mScroller.fling(0, 0, 0, velocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            mCoordinatorLayout.postOnAnimation(this);
            mLastFlingY = 0;
        }

        public void cancel() {
            mScroller.abortAnimation();
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int dy = mScroller.getCurrY() - mLastFlingY;

                final int selfOffset = getTopAndBottomOffset();
                final int newSelfOffset = Math.max(mMinOffset, Math.min(mMaxOffset, selfOffset - dy));
                final int skipped = newSelfOffset - selfOffset + dy;

                final boolean selfFinished = !setTopAndBottomOffset(newSelfOffset);

                final int targetOffset;
                final boolean targetFinished;
                if (mTargetView instanceof ScrollingView) {
                    targetOffset = ((ScrollingView) mTargetView).computeVerticalScrollOffset();
                    mTargetView.scrollBy(0, skipped);
                    targetFinished = (targetOffset == ((ScrollingView) mTargetView).computeVerticalScrollOffset());
                } else {
                    targetOffset = mTargetView.getScrollY();
                    mTargetView.scrollBy(0, skipped);
                    targetFinished = (targetOffset == mTargetView.getScrollY());
                }

                final boolean scrollerFinished = mScroller.isFinished();

                if (scrollerFinished || (selfFinished && targetFinished)) {
                    return;
                }

                mCoordinatorLayout.postOnAnimation(this);

                mLastFlingY = mScroller.getCurrY();
            }
        }
    }
}
