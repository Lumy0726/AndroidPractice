package com.android.lmj.firstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.lmj.firstapp.view.DrawView;

public class SubActivity8 extends AppCompatActivity {
    long backKeyTime = 0;
    Bitmap bitmapMain;
    Canvas drawCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);
        bitmapMain = Bitmap.createBitmap(1500, 1500, Bitmap.Config.ARGB_8888);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DrawView drawView = (DrawView) findViewById(R.id.drawOutput);
        drawCanvas = drawView.setBitmap(bitmapMain);
        int w = drawCanvas.getWidth();
        int h = drawCanvas.getHeight();
        drawCanvas.drawRect(0, 0, w, h, colorPaint(0xffddff00));
        drawCanvas.drawCircle(w / (float)2, h / (float)2, w / (float)4, colorPaint(0xff00ddff));
        drawView.invalidate();
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
    Paint colorPaint(int color){
        Paint colorPaint = new Paint();
        colorPaint.setColor(color);
        return colorPaint;
    }
}
