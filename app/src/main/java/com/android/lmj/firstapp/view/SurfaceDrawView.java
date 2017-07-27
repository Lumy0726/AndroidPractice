package com.android.lmj.firstapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.android.lmj.firstapp.log.LogSystem.androidLog;

/**
 * Created by LMJ on 2017-07-27.
 */

public class SurfaceDrawView extends SurfaceView implements SurfaceHolder.Callback{
    SurfaceHolder mHolder;
    Bitmap bitmap;
    Rect rect;

    public SurfaceDrawView(Context context){
        super(context);
        init();
    }
    public SurfaceDrawView(Context context, AttributeSet att){
        super(context, att);
        init();
    }
    void init(){ (mHolder = getHolder()).addCallback(this); }
    public Canvas setBitmap(Bitmap input) {
        if (input != null){
            bitmap = input;
            int w = getWidth();
            int h = getHeight();
            int bitW = bitmap.getWidth();
            int bitH = bitmap.getHeight();
            float margin = w - (float)bitW * h / bitH;
            if (margin > 0){
                rect = new Rect((int)margin / 2, 0, w - (int)margin / 2, h);
            }
            else {
                margin = h - (float)bitH * w / bitW;
                rect = new Rect(0, (int)margin / 2, w, h - (int)margin / 2);
            }
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            return canvas;
        }
        return null;
    }
    public void reDraw(){
        Canvas canvas = null;
        try{
            canvas  = mHolder.lockCanvas();
            canvas.drawColor(0xff000000);
            if (bitmap != null){
                canvas.drawBitmap(bitmap, null, rect, null);
            }
        } finally {
            if (canvas  != null){
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        reDraw();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
