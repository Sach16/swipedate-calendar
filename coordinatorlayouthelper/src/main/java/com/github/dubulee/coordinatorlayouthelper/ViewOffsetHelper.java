package com.github.dubulee.coordinatorlayouthelper;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
/**
 * ViewOffsetHelper.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class ViewOffsetHelper {
    private final View mView;
    private int mLayoutTop;
    private int mOffsetTop;

    public ViewOffsetHelper(View view) {
        mView = view;
    }

    public void onViewLayout() {
        mLayoutTop = mView.getTop();
        updateOffsets();
    }

    private void updateOffsets() {
        if (Build.VERSION.SDK_INT == 22) {
            ViewCompat.setTranslationY(mView, (float) mOffsetTop);
        } else {
            ViewCompat.offsetTopAndBottom(mView, mOffsetTop - mView.getTop() - mLayoutTop);
        }
    }

    public boolean setTopAndBottomOffset(int offset) {
        if (mOffsetTop != offset) {
            mOffsetTop = offset;
            updateOffsets();
            return true;
        } else {
            return false;
        }
    }

    public int getTopAndBottomOffset() {
        return mOffsetTop;
    }
}
