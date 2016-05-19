package com.github.dubulee.coordinatorlayouthelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/**
 * HeaderLayout.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class HeaderLayout extends LinearLayout {
    private int mFixedRange = 0;

    public HeaderLayout(Context context) {
        super(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setFixedScrollRange(int range) {
        mFixedRange = range;
    }

    public int getFinxedRange() {
        return mFixedRange;
    }

    public int getScrollRange() {
        return getHeight() - mFixedRange;
    }
}
