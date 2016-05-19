package com.github.dubulee.coordinatorlayouthelper;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * CoordinatorLayoutHelperNestedScrollView.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class CoordinatorLayoutHelperNestedScrollView extends NestedScrollView {
    public CoordinatorLayoutHelperNestedScrollView(Context context) {
        super(context);
    }

    public CoordinatorLayoutHelperNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorLayoutHelperNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    public int getHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }
}
