package com.android.lmj.firstapp;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SubActivity7 extends AppCompatActivity {
    View[] touchView = new View[4];
    TextView touchLog;
    int logLine = 0;
    long time = SystemClock.elapsedRealtime();
    GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub7);
        touchView[0] = findViewById(R.id.touchView1);
        touchView[1] = findViewById(R.id.touchView2);
        touchView[2] = findViewById(R.id.touchView3);
        touchView[3] = findViewById(R.id.touchView4);
        touchLog = (TextView) findViewById(R.id.touchLog);
        touchLog.setText("");
        touchView[0].setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewTouch1(event);
                return true;
            }
        });
        touchView[1].setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewTouch2(event);
                return true;
            }
        });
        for (int loop1 = 2; loop1 < 4; loop1++){
            if (touchView[loop1] != null){
                touchView[loop1].setOnTouchListener(new View.OnTouchListener(){
                    int code;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        viewTouch(event, code);
                        return true;
                    }
                    View.OnTouchListener setCode(int code){
                        this.code = code;
                        return this;
                    }
                }.setCode(loop1));
            }
        }
        detector = new GestureDetector(this, new GesDetectorLog(this));
        if (savedInstanceState != null){
            int touchLogLine = savedInstanceState.getInt("touchLogLine", -1);
            if (touchLogLine >= 0){
                logLine = touchLogLine;
                touchLog.setText(savedInstanceState.getString("touchLog"));
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("touchLogLine", logLine);
        outState.putString("touchLog", touchLog.getText().toString());
    }

    void viewTouch1(MotionEvent input){
        long touchTime = SystemClock.elapsedRealtime();
        int action = input.getAction();
        float curX = input.getX();
        float curY = input.getY();
        String str = "";
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                str = "Down: ";
                break;
            case MotionEvent.ACTION_MOVE:
                str = "Move: ";
                break;
            case MotionEvent.ACTION_UP:
                str = "--Up: ";
                break;
            default:
                str = "ELSE: ";
                break;
        }
        time = touchTime - time;
        if (0 < time && time < 10000){
            touchLogOutput(String.format("%4d: %s%9.4f, %9.4f", time, str, curX, curY));
        }
        else touchLogOutput(String.format("----: %s%9.4f, %9.4f", str, curX, curY));
        time = touchTime;
    }
    void viewTouch2(MotionEvent input){
        detector.onTouchEvent(input);
        //TODO
    }
    void viewTouch(MotionEvent input, int code){
        int action = input.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            touchView[code].setBackgroundColor(0xff00ddff);
        }
        else if (action == MotionEvent.ACTION_UP){
            touchView[code].setBackgroundColor( (code == 2) ? 0xffffffff : 0x77000000);
        }
    }
    void touchLogOutput(String input){
        if (logLine < 10){
            if (logLine != 0) touchLog.append("\n");
            touchLog.append(input);
            logLine++;
        }
        else {
            StringBuilder str = new StringBuilder(touchLog.getText().toString());
            while (str.charAt(0) != '\n') str.deleteCharAt(0);
            str.deleteCharAt(0);
            str.append('\n');
            str.append(input);
            touchLog.setText(str);
        }
    }
    public void onButton_Finish(View v){ finish(); }
}

class GesDetectorLog implements GestureDetector.OnGestureListener {
    SubActivity7 out;
    GesDetectorLog(SubActivity7 out){
        this.out = out;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        out.touchLogOutput("--Ges--onDown");
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {
        out.touchLogOutput("--Ges--onShowPress");
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        out.touchLogOutput("--Ges--onSingleTapUp");
        return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        out.touchLogOutput("--Ges--onScroll: X:" + distanceX + " Y:" + distanceY);
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        out.touchLogOutput("--Ges--onLongPress");
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        out.touchLogOutput("--Ges--onFling: X:" + velocityX + " Y:" + velocityY);
        return true;
    }
}