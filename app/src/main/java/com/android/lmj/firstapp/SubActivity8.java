package com.android.lmj.firstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.lmj.firstapp.timer.TimerAble;
import com.android.lmj.firstapp.view.DrawView;
import com.android.lmj.firstapp.timer.Timer;
import com.android.lmj.firstapp.view.SurfaceDrawView;

import static com.android.lmj.firstapp.log.LogSystem.androidLog;


public class SubActivity8 extends AppCompatActivity implements TimerAble {
    //system value
    long backKeyTime = 0;
    boolean onCreateFlag;
    //Timer value
    Timer multimediaTimer;
    static final int TIMERID_MAIN = 0;
    //bitmap value
    Bitmap bitmapMain;
    Canvas drawCanvas;
    int bitmapW, bitmapH;
    SurfaceDrawView drawView;
    //circle value
    int circleX, circleY;
    int circleVX, circleVY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);
        bitmapMain = Bitmap.createBitmap(1500, 1500, Bitmap.Config.ARGB_8888);
        bitmapW = bitmapMain.getWidth(); bitmapH = bitmapMain.getHeight();
        circleX = bitmapW / 2; circleY = bitmapH / 2;
        circleVX = 0; circleVY = 30;
        onCreateFlag = true;
        multimediaTimer = new Timer(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (onCreateFlag){
            onCreateFlag = false;
            drawView = (SurfaceDrawView) findViewById(R.id.drawOutput);
            drawCanvas = drawView.setBitmap(bitmapMain);
            //drawCanvas = drawView.testDraw();
            int w = drawCanvas.getWidth();
            int h = drawCanvas.getHeight();
            drawCanvas.drawRect(0, 0, w, h, colorPaint(0xffffffff));
            //drawView.invalidate();
            multimediaTimer.add(TIMERID_MAIN, 30);
        }
    }
    @Override
    protected void onPause() {
        multimediaTimer.stop();
        super.onPause();
    }
    @Override
    protected void onResume() {
        multimediaTimer.start();
        super.onResume();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            long pressTime = SystemClock.elapsedRealtime();
            int timeDiff = (int)(pressTime - backKeyTime);
            if (!(0 < timeDiff && timeDiff < 1500)){
                backKeyTime = pressTime;
                Toast.makeText(getApplicationContext(), "돌아가려면 한번더 누르십시오", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //Primary code.
    @Override
    public void onTimer(int id, int sendNum) {
        if (id == TIMERID_MAIN){
            androidLog(sendNum + "");
            int circleSize = bitmapW / 20;
            int constraintX = bitmapW - circleSize;
            int constraintY = bitmapH - circleSize;
            //circle delete.
            drawCanvas.drawCircle(circleX, circleY, circleSize, colorPaint(0xffffffff));
            //circle moving.
            for (int loop1 = 0; loop1 < sendNum; loop1++){
                circleX += circleVX; circleY += circleVY;
                if (circleX < circleSize){
                    circleX = 2 * circleSize - circleX;
                    circleVX *= -1;
                }
                else if(circleX > constraintX){
                    circleX = 2 * constraintX - circleX;
                    circleVX *= -1;
                }
                if (circleY < circleSize){
                    circleY = 2 * circleSize - circleY;
                    circleVY *= -1;
                }
                else if(circleY > constraintY){
                    circleY = 2 * constraintY - circleY;
                    circleVY *= -1;
                    circleVX = (int)(Math.random() * 101) - 50;
                }
            }
            //circle draw.
            drawCanvas.drawCircle(circleX, circleY, circleSize, colorPaint(0xff00ddff));
            drawView.reDraw();
        }
    }
    Paint colorPaint(int color){
        Paint colorPaint = new Paint();
        colorPaint.setColor(color);
        return colorPaint;
    }
}
