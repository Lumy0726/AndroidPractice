package com.android.lmj.firstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.lmj.firstapp.joystick.JoyStick;
import com.android.lmj.firstapp.timer.TimerAble;
import com.android.lmj.firstapp.timer.Timer;
import com.android.lmj.firstapp.view.SurfaceDrawView;

import com.android.lmj.firstapp.tools.Tools;

import java.util.Vector;

import static com.android.lmj.firstapp.log.LogSystem.androidLog;


public class SubActivity8 extends AppCompatActivity implements TimerAble, SurfaceDrawView.TouchEvent{
    //system value
    long backKeyTime = 0;
    boolean onCreateFlag;
    //Timer value
    Timer multimediaTimer;
    static final int TIMERID_MAIN = 0;
    //game state value
    int gameState;
    //JoyStick value
    JoyStick joyStick;
    int joyInputID;
    //bitmap value
    Bitmap bitmap;
    Canvas canvas;
    Bitmap bufBitmap_Circle, bufBitmap_Ref, bufBitmap_BottomLine;
    //drawView value
    Bitmap viewBitmap;
    Canvas viewCanvas;
    int viewBitmapW, viewBitmapH;
    SurfaceDrawView drawView;
    float viewRatio;
    int viewMarginX, viewMarginY;
    //circle value
    int circleX, circleY;
    int circleVX, circleVY;
    float circleSpeed;
    String circleSpeedStr;
    int circleSize, constraintX;
    //reflector value
    int refX, refY, refSize, refConstraintY;

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
            viewRatio = drawView.getRatio();
            viewMarginX = drawView.getMarginX(); viewMarginY = drawView.getMarginY();
            //joyStick initialize.
            joyStick = new JoyStick(viewRatio);
            //circle value
            circleSize = viewBitmapW / 20;
            constraintX = viewBitmapW - circleSize;
            //ref value
            refConstraintY = viewBitmapH * 8 / 9;
            refSize = viewBitmapW / 3;
            bufBitmapDraw();
            //initialize.
            reset();
            drawView.update();
            multimediaTimer.add(TIMERID_MAIN, 16);
        }
    }
    void reset(){
        //circle value.
        circleX = viewBitmapW / 2; circleY = viewBitmapH / 2;
        circleSpeed = 25;
        circleSpeedStr = String.format("SPEED: %4.1f", circleSpeed);
        circleVX = 0; circleVY = (int)circleSpeed;
        //ref value
        refX = (viewBitmapW + refSize) / 2; refY = (viewBitmapH + refConstraintY) / 2;
        //bitmap reset.
        viewCanvas.drawColor(0xffffffff);
        bitmap = Bitmap.createBitmap(viewBitmap);
        canvas.setBitmap(bitmap);
        gameState = 1;
    }
    void bufBitmapDraw(){
        Canvas bufCanvas = new Canvas();
        bufCanvas.setBitmap(bufBitmap_Circle = Bitmap.createBitmap(circleSize * 2, circleSize * 2, Bitmap.Config.ARGB_8888));
        bufCanvas.drawCircle(circleSize, circleSize, circleSize, Tools.colorPaint(0xff00ddff));
        bufCanvas.setBitmap(bufBitmap_BottomLine = Bitmap.createBitmap(viewBitmapW, (int) Tools.dipToPix(5), Bitmap.Config.ARGB_8888));
        bufCanvas.drawColor(0x227f00ff);
        bufCanvas.setBitmap(bufBitmap_Ref = Bitmap.createBitmap(refSize, (int) Tools.dipToPix(10), Bitmap.Config.ARGB_8888));
        int radius = bufCanvas.getHeight() / 2;
        bufCanvas.drawCircle(radius, radius, radius, Tools.colorPaint(0xffff0000));
        bufCanvas.drawCircle(bufCanvas.getWidth() - radius, radius, radius, Tools.colorPaint(0xffff0000));
        bufCanvas.drawRect(radius, 0, bufCanvas.getWidth() - radius, bufCanvas.getHeight(), Tools.colorPaint(0xffff0000));
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
    public void onButton_Reset(View v){ reset(); }
    @Override
    public boolean touchEvent(MotionEvent input) {
        int index;
        switch(input.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                joyInputID = input.getPointerId(0);
                joyStick.setMove((input.getX() - viewMarginX) * viewRatio, (input.getY() - viewMarginY) * viewRatio);
                break;
            case MotionEvent.ACTION_MOVE:
                index = input.findPointerIndex(joyInputID);
                if (index != -1){
                    joyStick.setMove((input.getX() - viewMarginX) * viewRatio, (input.getY() - viewMarginY) * viewRatio);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                index = input.findPointerIndex(joyInputID);
                if (input.getActionIndex() == index){
                    joyStick.setMoveOff();
                }
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
            if (gameState == 1) {
                //circle delete.
                //canvas.drawRect(Tools.rectWH(circleX - circleSize, circleY - circleSize, bufBitmap_Circle.getWidth(), bufBitmap_Circle.getHeight()), Tools.colorPaint(0xffffffff, true));
                //ref delete.
                //canvas.drawRect(Tools.rectWH(refX - refSize / 2, refY, bufBitmap_Ref.getWidth(), bufBitmap_Ref.getHeight()), Tools.colorPaint(0xffffffff, true));

                //delete all.
                Tools.resetBitmap(canvas, 0xffffffff);
                //ref moving.
                int refMoveX = (int) (joyStick.getMoveX() * circleSpeed * sendNum), refMoveY = (int) (joyStick.getMoveY() * circleSpeed * sendNum);
                refX += refMoveX;
                refY += refMoveY;
                if (refX < refSize / 2) refX = refSize / 2;
                if (viewBitmapW - refSize / 2 < refX) refX = viewBitmapW - refSize / 2;
                if (refY < refConstraintY) refY = refConstraintY;
                if (viewBitmapH - bufBitmap_Ref.getHeight() <= refY)
                    refY = viewBitmapH - bufBitmap_Ref.getHeight();
                //circle moving.
                for (int loop1 = 0; loop1 < sendNum; loop1++) {
                    circleX += circleVX;
                    circleY += circleVY;
                    if (circleX < circleSize) {
                        circleX = 2 * circleSize - circleX;
                        circleVX *= -1;
                    } else if (circleX > constraintX) {
                        circleX = 2 * constraintX - circleX;
                        circleVX *= -1;
                    }
                    if (circleY < circleSize) {
                        circleY = 2 * circleSize - circleY;
                        circleVY *= -1;
                    } else if (circleY > refY && circleVY > 0) {
                        int refHitSize = (refSize + circleSize) / 2;
                        if (refX - refHitSize < circleX && circleX < refX + refHitSize) {//circle hits reflector(Not perfact).
                            int hitPos = circleX - refX;
                            circleVX *= -1;//default reflect.
                            if (hitPos != 0) {
                                int maxX = (int) (circleSpeed * 8 / 9);
                                if (hitPos > 0) {
                                    circleVX += (maxX - circleVX) * hitPos / refHitSize;
                                } else {
                                    circleVX += (maxX + circleVX) * hitPos / refHitSize;
                                }
                                circleVY = (int) Math.sqrt((double) (circleSpeed * circleSpeed - circleVX * circleVX));
                            }
                            circleVY *= -1;
                            if (circleSpeed < 40) {
                                circleSpeed += 0.1;
                                circleSpeedStr = String.format("SPEED: %4.1f", circleSpeed);
                            }
                        } else {
                            gameState = 2;
                        }
                    }
                }
                //bottomLine draw.
                canvas.drawBitmap(bufBitmap_BottomLine, 0, refConstraintY, null);
                //circleSpeed Draw.
                canvas.drawText(circleSpeedStr, 0, Tools.dipToPix(30) * viewRatio, Tools.textPaint(0xffff7777, Tools.dipToPix(30) * viewRatio));
                //circle draw.
                canvas.drawBitmap(bufBitmap_Circle, circleX - circleSize, circleY - circleSize, null);
                //ref draw.
                canvas.drawBitmap(bufBitmap_Ref, refX - refSize / 2, refY, null);
                //drawView draw.
                viewCanvas.drawBitmap(bitmap, 0, 0, Tools.forcePaint());
                //Joystick draw.
                joyStick.drawJoyStick(viewCanvas);
                //drawView update.
                drawView.update();
            }
            else if (gameState == 2){
                Paint paint = Tools.textPaint(0xffff7777, Tools.dipToPix(30) * viewRatio);
                paint.setTextAlign(Paint.Align.CENTER);
                viewCanvas.drawText("Game Over", viewBitmapW / 2, viewBitmapH / 2 + Tools.dipToPix(15) * viewRatio, paint);
                drawView.update();
                gameState = 0;
            }
        }
    }
}
