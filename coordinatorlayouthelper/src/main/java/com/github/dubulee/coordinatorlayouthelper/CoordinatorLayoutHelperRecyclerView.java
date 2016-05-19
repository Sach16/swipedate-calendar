package com.github.dubulee.coordinatorlayouthelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * CoordinatorLayoutHelperRecyclerView.java
 * Created by DUBULEE on 2015/12/27.
 * Copyright (c) DUBULEE. All rights reserved.
 */
public class CoordinatorLayoutHelperRecyclerView extends RecyclerView {
    public CoordinatorLayoutHelperRecyclerView(Context context) {
        super(context);
    }

    public CoordinatorLayoutHelperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorLayoutHelperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    public int getHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }
}
