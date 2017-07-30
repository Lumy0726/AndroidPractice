package com.android.lmj.firstapp.joystick;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.android.lmj.firstapp.tools.Tools;


/**
 * Created by LMJ on 2017-07-27.
 */

//TODO
public class JoyStick {
    //JoyStick`s normal size value.
    static float normalRadius = Tools.dipToPix(80), normalWide = Tools.dipToPix(20);
    public static void resetNormalRadius(DisplayMetrics input){
        Tools.dipToPixInit(input); normalRadius = Tools.dipToPix(80); normalWide = Tools.dipToPix(20);
    }
    //JoyStick size value.
    int radius, wide;
    //JoyStick position value.
    float centerX, centerY;
    float nowX, nowY;
    boolean state;
    //JoyStick Bitmap value.
    Bitmap joyBitmap;
    Canvas joyBitmapCanvas;
    int backColor = 0x337f7f7f;
    int joyColor = 0x3300aaff;

    //constructor
    public JoyStick(){
        super();
        radius = (int)normalRadius;
        wide = (int)normalWide;
        init();
    }
    public JoyStick(float ratio){
        radius = (int)(normalRadius * ratio);
        wide = (int)(normalWide * ratio);
        init();
    }
    public JoyStick(float ratio, int backColor, int joyColor){
        radius = (int)(normalRadius * ratio);
        wide = (int)(normalWide * ratio);
        this.backColor = backColor; this.joyColor = joyColor;
        init();
    }
    public JoyStick(int radius, int wide){
        this.radius = radius;
        this.wide = wide;
        init();
    }
    public JoyStick(int radius, int wide, int backColor, int joyColor){
        this.radius = radius;
        this.wide = wide;
        this.backColor = backColor; this.joyColor = joyColor;
        init();
    }

    //initial, reset.
    protected void init(){
        //prevent worng(small) value.
        if (radius <= 20){ radius = 20;}
        if (wide > radius){ wide = radius / 4; }
        //JoyBitmap create.
        joyBitmap = Bitmap.createBitmap((radius + wide) * 2, (radius + wide) * 2, Bitmap.Config.ARGB_8888);
        joyBitmapCanvas = new Canvas();
        joyBitmapCanvas.setBitmap(joyBitmap);
        drawJoy();
        //
        reset();
    }
    public void reset(){
        state = false;
        centerY = centerX = (radius + wide) / 2;
    }

    //---? method
    protected void drawJoy(){
        int middle = radius + wide;
        Tools.resetCanvas(joyBitmapCanvas).drawCircle(middle, middle, radius, Tools.colorPaint(backColor));
        joyBitmapCanvas.drawCircle(middle, middle, radius - wide, Tools.alphaPaint(0));
        joyBitmapCanvas.drawCircle(middle, middle, wide / 2, Tools.colorPaint(backColor));
    }

    //get, set method
    public void setBackColor(int color){ setColor(color, joyColor); }
    public void setJoyColor(int color){ setColor(backColor, color); }
    public void setAlpha(int alpha){ setColor( (alpha << 24 ) | (0x00ffffff & backColor), (alpha << 24 ) | (0x00ffffff & joyColor)); }
    public void setColor(int backC, int joyC){
        backColor = backC; joyColor = joyC;
        drawJoy();
    }
    public void setMove(float x, float y){
        if (state){
            nowX = x; nowY = y;
            double disX = nowX - centerX, disY = nowY - centerY;
            double distance = Math.sqrt(disX*disX + disY*disY);
            if (distance > radius){
                disX = disX * radius / distance; disY = disY * radius / distance;
                centerX = nowX - (float)disX; centerY = nowY - (float)disY;
            }
            else if (distance < radius / 10){//too small distance --> no move.
                nowX = centerX; nowY = centerY;
            }
        }
        else {
            centerX = nowX = x; centerY = nowY = y;
            state = true;
        }
    }
    public void setMoveOff(){ state = false; }
    public void setMoveOff(float x, float y){
        state = false;
        centerX = x; centerY = y;
    }
    public float getMoveX(){
        if (state) return (nowX - centerX) / radius;
        return (float)0;
    }
    public float getMoveY(){
        if (state) return (nowY - centerY) / radius;
        return (float)0;
    }
    public float getCenterX(){ return centerX; }
    public float getCenterY(){ return centerY; }
    public Bitmap getJoyStickBitmap(){
        Bitmap outputBitmap = Bitmap.createBitmap(joyBitmap);
        if (state){
            Canvas canvas = new Canvas();
            canvas.setBitmap(outputBitmap);
            int middle = radius + wide;
            float disX = nowX - centerX, disY = nowY - centerY;
            canvas.drawLine(middle, middle, middle + disX, middle + disY, Tools.linePaint(joyColor, wide / 2));
            canvas.drawCircle(middle + disX, middle + disY, wide, Tools.colorPaint(joyColor));
        }
        return outputBitmap;
    }
    public void drawJoyStick(Canvas canvas){
        if (canvas != null) {
            int drawX = (int)centerX - radius - wide;
            int drawY = (int)centerY - radius - wide;
            canvas.drawBitmap(getJoyStickBitmap(), drawX, drawY, null);
        }
    }
}
