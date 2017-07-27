package com.android.lmj.firstapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.android.lmj.firstapp.log.LogSystem.androidLog;

/**
 * Created by LMJ on 2017-07-27.
 */

public class SurfaceDrawView extends SurfaceView implements SurfaceHolder.Callback{
    ViewThread thread;
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
            Canvas canvas;
            synchronized(this){
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
                canvas = new Canvas();
                canvas.setBitmap(bitmap);
            }
            return canvas;
        }
        return null;
    }
    void reDraw(){
        Canvas canvas = null;
        Bitmap saveBitmap = null;
        if (bitmap != null){
            synchronized(bitmap){
                //long time = SystemClock.elapsedRealtime();
                saveBitmap = Bitmap.createBitmap(bitmap);
                //androidLog("bitmapCopyTime: " + (int)(SystemClock.elapsedRealtime() - time));
            }
        }
        //long time = SystemClock.elapsedRealtime();
        try{
            canvas  = mHolder.lockCanvas();
            if (canvas != null){
                synchronized(mHolder){
                    canvas.drawColor(0xff000000);
                    if (saveBitmap != null){
                        canvas.drawBitmap(saveBitmap, null, rect, null);
                    }
                }
            }
        } finally {
            if (canvas  != null){
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        //androidLog("bitmapDrawTime: " + (int)(SystemClock.elapsedRealtime() - time));
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new ViewThread(this);
        thread.start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.off();
    }
}

class ViewThread extends Thread{
    volatile boolean state = true;
    SurfaceDrawView sdView;
    ViewThread(SurfaceDrawView input){
        sdView = input;
    }
    void off() {
        state = false;
        boolean retry = true;
        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) { }
        }
    }
    @Override
    public void run() {
        while (state){
            sdView.reDraw();
        }
    }
}
