package com.android.lmj.firstapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LMJ on 2017-07-25.
 */

public class DrawView extends View {
    Bitmap bitmap;
    Rect rect;
    int test = 0;
    public DrawView(Context context){ super(context); }
    public DrawView(Context context, AttributeSet att){super(context, att);}
    public Canvas setBitmap(Bitmap input) {
        if (input != null){
            bitmap = input;
            int w = getWidth();
            int h = getHeight();
            int bitW = bitmap.getWidth();
            int bitH = bitmap.getHeight();
            double margin = w - (double)bitW * h / bitH;
            if (margin > 0){
                rect = new Rect((int)margin / 2, 0, w - (int)margin / 2, h);
            }
            else {
                margin = h - (double)bitH * w / bitW;
                rect = new Rect(0, (int)margin / 2, w, h - (int)margin / 2);
            }
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            return canvas;
        }
        return null;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null){
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }
}
