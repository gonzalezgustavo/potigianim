package com.example.potigianim.fragments.decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestMarginDecorator extends RecyclerView.ItemDecoration {
    private int spaceBetween;

    public RequestMarginDecorator(int spaceBetween) {
        this.spaceBetween = spaceBetween;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceBetween;
        }
        outRect.bottom = spaceBetween;
        outRect.left = 0;
        outRect.right = 0;
    }
}
