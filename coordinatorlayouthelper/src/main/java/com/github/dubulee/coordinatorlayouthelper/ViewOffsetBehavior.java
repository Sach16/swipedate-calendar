package com.github.dubulee.coordinatorlayouthelper;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
/**
 * ViewOffsetBehavior.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private ViewOffsetHelper mViewOffsetHelper;
    private int mTempTopBottomOffset = 0;

    public ViewOffsetBehavior() {
    }

    public ViewOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (mViewOffsetHelper == null) {
            mViewOffsetHelper = new ViewOffsetHelper(child);
        }

        mViewOffsetHelper.onViewLayout();
        if (mTempTopBottomOffset != 0) {
            mViewOffsetHelper.setTopAndBottomOffset(mTempTopBottomOffset);
            mTempTopBottomOffset = 0;
        }

        return true;
    }

    public boolean setTopAndBottomOffset(int offset) {
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.setTopAndBottomOffset(offset);
        } else {
            mTempTopBottomOffset = offset;
            return false;
        }
    }

    public int getTopAndBottomOffset() {
        return mViewOffsetHelper != null ? mViewOffsetHelper.getTopAndBottomOffset() : 0;
    }
}
