package com.android.lmj.firstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.lmj.firstapp.timer.TimerAble;
import com.android.lmj.firstapp.view.DrawView;
import com.android.lmj.firstapp.timer.Timer;
import com.android.lmj.firstapp.view.SurfaceDrawView;

import com.android.lmj.firstapp.tools.Tools;
import static com.android.lmj.firstapp.log.LogSystem.androidLog;


public class SubActivity8 extends AppCompatActivity implements TimerAble, SurfaceDrawView.TouchEvent{
    //system value
    long backKeyTime = 0;
    boolean onCreateFlag;
    //Timer value
    Timer multimediaTimer;
    static final int TIMERID_MAIN = 0;
    //bitmap value
    Bitmap bitmap;
    Canvas canvas;
    //joystickBitmap value
    Bitmap jBitmap;
    float joyX, joyY;
    int size;
    //drawView value
    Bitmap viewBitmap;
    Canvas viewCanvas;
    int viewBitmapW, viewBitmapH;
    SurfaceDrawView drawView;
    float ratio;
    //circle value
    int circleX, circleY;
    int circleVX, circleVY;
    int circleSize, constraintX, constraintY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);
        onCreateFlag = true;
        //make viewBitmap, get drawView, set Timer.
        canvas = new Canvas();
        viewBitmap = Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888);
        viewBitmapW = viewBitmap.getWidth(); viewBitmapH = viewBitmap.getHeight();
        drawView = (SurfaceDrawView) findViewById(R.id.drawOutput);
        drawView.setFpsOutput(true);
        drawView.setTouchEventClass(this);
        multimediaTimer = new Timer(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (onCreateFlag){
            onCreateFlag = false;
            //drawView layout size is confirmed.
            viewCanvas = drawView.setBitmap(viewBitmap);
            ratio = drawView.getRatio();
            //joyStickBitmap draw.
            size = Math.round(Tools.dipToPix(80) * ratio);
            int wide = Math.round(Tools.dipToPix(20) * ratio);
            jBitmap = Bitmap.createBitmap(2 * size, 2 * size, Bitmap.Config.ARGB_8888);
            Canvas jCanvas = new Canvas();
            jCanvas.setBitmap(jBitmap);
            jCanvas.drawCircle(size, size, size, Tools.colorPaint(0x337f7f7f));
            jCanvas.drawCircle(size, size, size - wide, Tools.alphaPaint(0));
            jCanvas.drawCircle(size, size, wide / 2, Tools.colorPaint(0x337f7f7f));
            //initialize.
            initialize();
            drawView.update();
            multimediaTimer.add(TIMERID_MAIN, 16);
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

    public void onButton_Initialize(View v){
        initialize();
    }
    void initialize(){

        //circle value.
        circleX = viewBitmapW / 2; circleY = viewBitmapH / 2;
        circleVX = 0; circleVY = 20;
        circleSize = viewBitmapW / 20;
        constraintX = viewBitmapW - circleSize;
        constraintY = viewBitmapH - circleSize;
        //bitmap reset.
        viewCanvas.drawColor(0xffffffff);
        bitmap = Bitmap.createBitmap(viewBitmap);
        canvas.setBitmap(bitmap);
        //Joystick reset.
        joyX = viewBitmapW / (float)2 - size; joyY = viewBitmapH / (float)2 - size;
    }
    @Override
    public boolean touchEvent(MotionEvent input) {
        switch(input.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                joyX = (input.getX() - drawView.getMarginX()) * ratio - size;
                joyY = (input.getY() - drawView.getMarginY()) * ratio - size;
                break;
            default:
                break;
        }
        //androidLog("X:" + input.getX() + " Y:" + input.getY());
        return true;
    }
    @Override
    public void onTimer(int id, int sendNum) {
        if (id == TIMERID_MAIN){
            //androidLog(sendNum + "");
            //circle delete.
            canvas.drawCircle(circleX, circleY, circleSize, Tools.colorPaint(0xffffffff));
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
                    circleVX = (int)(Math.random() * 81) - 40;
                }
            }
            //circle draw.
            canvas.drawCircle(circleX, circleY, circleSize, Tools.colorPaint(0xff00ddff));
            //Joystick draw.
            //drawView draw.
            viewCanvas.drawBitmap(bitmap, 0, 0, null);
            viewCanvas.drawBitmap(jBitmap, joyX, joyY, null);
            drawView.update();
        }
    }
}
