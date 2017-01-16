package com.afc.android.news.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hp on 1/2/2017.
 */

public class RVVerticalSpace extends RecyclerView.ItemDecoration {
    private int mSpace;

    public RVVerticalSpace(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.right = mSpace;
//        outRect.left = mSpace;
        outRect.bottom = mSpace;

        if(parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }
}
