package com.android.lmj.firstapp.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by LMJ on 2017-07-28.
 */

public class Tools {

    static DisplayMetrics displayMetrics;
    //Tools_initial
    public static void tools_initial(AppCompatActivity input){
        dipToPixInit(input.getResources().getDisplayMetrics());
    }
    //dipToPix
    public static void dipToPixInit(DisplayMetrics input){
        displayMetrics = input;
    }
    public static float dipToPix(int dip){
        if (displayMetrics != null){
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
        }
        return (float)0;
    }
    //Canvas, Bitmap, Paint.
    public static Paint colorPaint(int color){
        Paint colorPaint = new Paint();
        colorPaint.setColor(color);
        return colorPaint;
    }
    public static Paint linePaint(int color, int width){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width);
        return paint;
    }
    public static Paint alphaPaint(int alpha){
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setColor(alpha << 24);
        return paint;
    }
    public static Bitmap reverseAlpha(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int loop1, loop2, color;
        for (loop1 = 0; loop1 < w; loop1++){
            for (loop2 = 0; loop2 < h; loop2++){
                color = bitmap.getPixel(loop1, loop2);
                bitmap.setPixel(loop1, loop2, color ^ 0xff000000);
            }
        }
        return bitmap;
    }
    public static Canvas setAlpha(Canvas canvas, int alpha){
        canvas.drawColor(alpha << 24, PorterDuff.Mode.DST_IN);
        return canvas;
    }
    public static Canvas resetCanvas(Canvas canvas){ return setAlpha(canvas, 0); }
}
