package com.android.lmj.firstapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    Bitmap saveBitmap;
    Rect rect;

    float framePerSec;
    boolean fpsOutput = false;
    Paint fpsPaint;

    public SurfaceDrawView(Context context){
        super(context);
        init();
    }
    public SurfaceDrawView(Context context, AttributeSet att){
        super(context, att);
        init();
    }
    void init(){
        (mHolder = getHolder()).addCallback(this);
        fpsPaint = new Paint(){
          Paint mySet(){
              setColor(0xffff7777);
              setStyle(Style.FILL);
              setTextSize(50);
              return this;
          }
        }.mySet();
    }
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
    public void update() {
        if (bitmap != null) {
            //long time = SystemClock.elapsedRealtime();
            saveBitmap = Bitmap.createBitmap(bitmap);
            //androidLog("bitmapCopyTime: " + (int)(SystemClock.elapsedRealtime() - time));
        }
    }
    public void setFpsOutput(boolean input){ fpsOutput = input; }

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
    //Call in another thread.
    void reDraw(){
        Canvas viewCanvas = null;
        long time = SystemClock.elapsedRealtime();
        try{
            viewCanvas  = mHolder.lockCanvas();
            if (viewCanvas != null){
                synchronized(mHolder){
                    viewCanvas.drawColor(0xff000000);
                    if (saveBitmap != null){
                        viewCanvas.drawBitmap(saveBitmap, null, rect, null);
                    }
                    if (fpsOutput){
                        viewCanvas.drawText(String.format("%5.2f", framePerSec), 0, 50, fpsPaint);
                    }
                }
            }
        } finally {
            if (viewCanvas  != null){
                mHolder.unlockCanvasAndPost(viewCanvas);
            }
        }
        framePerSec = (float)1000 / (int)(SystemClock.elapsedRealtime() - time);
        //androidLog("bitmapDrawTime: " + (int)(SystemClock.elapsedRealtime() - time));
    }
}

class ViewThread extends Thread{
    volatile boolean state = true;
    SurfaceDrawView sdView;
    ViewThread(SurfaceDrawView input){ sdView = input; }
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
