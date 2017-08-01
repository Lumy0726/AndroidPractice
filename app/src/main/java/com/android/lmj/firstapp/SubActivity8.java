package com.android.lmj.firstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    //game state, option value
    int gameState;
    int controlOption;
    final int CONTROL_NORMAL = 0;
    final int CONTROL_JOYSTICK = 1;
    //CONTROL_NORMAL value
    int touchX, touchY;
    //CONTROL_JOYSTICK value
    JoyStick joyStick;
    int joyInputID;
    //bitmap value
    Bitmap bitmap;
    Canvas canvas;
    Bitmap bufBitmap_Circle, bufBitmap_Ref, bufBitmap_RefLine;
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
    int refX, refY, refSize, refConstraintTopY, refConstraintBottomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);
        onCreateFlag = true;
        //make viewBitmap, get drawView, set Timer.
        canvas = new Canvas();
        viewBitmap = Bitmap.createBitmap(600, 900, Bitmap.Config.ARGB_8888);
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
            //circle value
            circleSize = viewBitmapW / 20;
            constraintX = viewBitmapW - circleSize;
            //ref value
            refConstraintBottomY = viewBitmapH * 8 / 9;
            refConstraintTopY = viewBitmapH * 7 / 9;
            refSize = viewBitmapW / 3;
            //joyStick initialize.
            joyStick = new JoyStick(viewRatio);
            joyStick.setMoveOff(viewBitmapW / 2, refConstraintBottomY);
            //butBitmapDraw
            bufBitmapDraw();
            //initialize.
            gameState = 0;//game is stop(wait for user input).
            //controlOption_layout setting
            int buttonW = findViewById(R.id.controlOption_Layout).getWidth();
            Button button = (Button) findViewById(R.id.controlOption_Button0);
            button.setWidth(buttonW);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    setControl(CONTROL_NORMAL);
                }
            });
            button = (Button) findViewById(R.id.controlOption_Button1);
            button.setWidth(buttonW);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    setControl(CONTROL_JOYSTICK);
                }
            });
            //drawView update.
            Tools.resetBitmap(viewCanvas, 0xffffffff);
            drawView.update();
            //Timer start.
            multimediaTimer.add(TIMERID_MAIN, 16);
        }
    }
    void reset(){
        //circle value.
        circleX = viewBitmapW / 2; circleY = circleSize;
        circleSpeed = 20;
        circleSpeedStr = String.format("SPEED: %4.1f", circleSpeed);
        circleVX = 0; circleVY = (int)circleSpeed;
        //ref value
        refX = (viewBitmapW + refSize) / 2; refY = (refConstraintTopY + refConstraintBottomY) / 2;
        //bitmap reset.
        viewCanvas.drawColor(0xffffffff);
        bitmap = Bitmap.createBitmap(viewBitmap);
        canvas.setBitmap(bitmap);
        gameState = 1;
        //control reset.
        touchX = refX; touchY = refY;
        joyStick.setMoveOff();
    }
    void setControl(int control){
        controlOption = control;
        ConstraintLayout rootLayout = (ConstraintLayout) findViewById(R.id.drawViewContainer);
        LinearLayout controlLayout = (LinearLayout) rootLayout.findViewById(R.id.controlOption_Layout);
        rootLayout.removeView(controlLayout);
        reset();
    }
    void bufBitmapDraw(){
        Canvas bufCanvas = new Canvas();
        bufCanvas.setBitmap(bufBitmap_Circle = Bitmap.createBitmap(circleSize * 2, circleSize * 2, Bitmap.Config.ARGB_8888));
        bufCanvas.drawCircle(circleSize, circleSize, circleSize, Tools.colorPaint(0xff00ddff));
        bufCanvas.setBitmap(bufBitmap_RefLine = Bitmap.createBitmap(viewBitmapW, viewBitmapH / 9 / 6, Bitmap.Config.ARGB_8888));
        bufCanvas.drawColor(0x227f00ff);
        bufCanvas.setBitmap(bufBitmap_Ref = Bitmap.createBitmap(refSize, (refSize / 16) * 2, Bitmap.Config.ARGB_8888));
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
        switch (input.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                joyInputID = input.getPointerId(0);
                if (controlOption == CONTROL_NORMAL){
                    touchX = (int)((input.getX() - viewMarginX) * viewRatio);
                    touchY = (int)((input.getY() - viewMarginY) * viewRatio);
                }
                else if (controlOption == CONTROL_JOYSTICK){
                    joyStick.setMove((input.getX() - viewMarginX) * viewRatio, (input.getY() - viewMarginY) * viewRatio);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                index = input.findPointerIndex(joyInputID);
                if (index != -1) {
                    if (controlOption == CONTROL_NORMAL) {
                        touchX = (int) ((input.getX() - viewMarginX) * viewRatio);
                        touchY = (int) ((input.getY() - viewMarginY) * viewRatio);
                    } else if (controlOption == CONTROL_JOYSTICK) {
                        joyStick.setMove((input.getX() - viewMarginX) * viewRatio, (input.getY() - viewMarginY) * viewRatio);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                index = input.findPointerIndex(joyInputID);
                if (input.getActionIndex() == index) {
                    if (controlOption == CONTROL_NORMAL) {
                        touchX = refX; touchY = refY;
                    } else if (controlOption == CONTROL_JOYSTICK) {
                        joyStick.setMoveOff();
                    }
                }
                break;
            default:
                break;
        }
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
                if (controlOption == CONTROL_NORMAL){
                    int dx = touchX - refX, dy = touchY - refY;
                    if (dx != 0 || dy != 0){
                        double distance = Math.sqrt(dx*dx + dy*dy);
                        if (distance > circleSpeed){
                            dx = (int)(dx * circleSpeed / distance);
                            dy = (int)(dy * circleSpeed / distance);
                            refX += dx; refY += dy;
                        }
                        else {
                            refX = touchX; refY = touchY;
                        }
                    }
                }
                else if (controlOption == CONTROL_JOYSTICK){
                    int refMoveX = (int) (joyStick.getMoveX() * circleSpeed * sendNum), refMoveY = (int) (joyStick.getMoveY() * circleSpeed * sendNum);
                    refX += refMoveX;
                    refY += refMoveY;
                }
                if (refX < refSize / 2) refX = refSize / 2;
                if (viewBitmapW - refSize / 2 < refX) refX = viewBitmapW - refSize / 2;
                if (refY < refConstraintTopY) refY = refConstraintTopY;
                if (refConstraintBottomY <= refY + bufBitmap_Ref.getHeight()) refY = refConstraintBottomY - bufBitmap_Ref.getHeight();
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
                    } else if (circleY + circleSize > refY && circleVY > 0) {
                        int refHitSize = refSize / 2 + circleSize;
                        if (refX - refHitSize < circleX && circleX < refX + refHitSize) {
                            int hitPos = circleX - refX;
                            if (circleSpeed < 60) {
                                circleSpeed += 0.1;
                                circleSpeedStr = String.format("SPEED: %4.1f", circleSpeed);
                            }
                            circleVX *= -1;//default reflect.
                            if (hitPos != 0) {//angle change reflect.
                                int maxX = (int) (circleSpeed * 8 / 9);
                                if (hitPos > 0) {
                                    circleVX += (maxX - circleVX) * hitPos / refHitSize;
                                } else {
                                    circleVX += (maxX + circleVX) * hitPos / refHitSize;
                                }
                            }
                            circleVX += (int)(Math.random() * 3) - 1;//random angle change(for preventing infinity loop).
                            circleVY = -1 * (int) Math.round(Math.sqrt((double) (circleSpeed * circleSpeed - circleVX * circleVX)));
                        } else if (circleY > refY) {
                            gameState = 2;
                        }
                    }
                }
                //refLine draw.
                canvas.drawBitmap(bufBitmap_RefLine, 0, refConstraintTopY - bufBitmap_RefLine.getHeight(), null);
                canvas.drawBitmap(bufBitmap_RefLine, 0, refConstraintBottomY, null);
                //circleSpeed Draw.
                canvas.drawText(circleSpeedStr, 0, Tools.dipToPix(30) * viewRatio, Tools.textPaint(0xffff7777, Tools.dipToPix(30) * viewRatio));
                //circle draw.
                canvas.drawBitmap(bufBitmap_Circle, circleX - circleSize, circleY - circleSize, null);
                //ref draw.
                canvas.drawBitmap(bufBitmap_Ref, refX - refSize / 2, refY, null);
                //drawView draw.
                viewCanvas.drawBitmap(bitmap, 0, 0, Tools.forcePaint());
                //Joystick draw.
                if (controlOption == CONTROL_JOYSTICK){
                    joyStick.drawJoyStick(viewCanvas);
                }
                //drawView update.
                drawView.update();
            }
            else if (gameState == 2){
                Paint paint = Tools.textPaint(0xffff7777, Tools.dipToPix(30) * viewRatio);
                paint.setTextAlign(Paint.Align.CENTER);
                viewCanvas.drawText("Game Over", viewBitmapW / 2, viewBitmapH / 2, paint);
                drawView.update();
                gameState = 0;
            }
        }
    }
}
