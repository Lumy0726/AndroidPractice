package com.android.lmj.firstapp;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.lmj.firstapp.timer.Timer;
import com.android.lmj.firstapp.timer.TimerAble;

import static com.android.lmj.firstapp.log.LogSystem.androidLog;

/*
사용자 정의 클래스
Service
Intent
SheredPref
TimeEvent
*/

public class SubActivity5 extends AppCompatActivity implements TimerAble {
    static boolean[] timerFlag = new boolean[3];
    static int[] time = new int[3];
    Timer timer;
    Button[] button = new Button[3];
    static {
        for (int loop1=0; loop1 < 3; loop1++) timerFlag[loop1] = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub5);
        button[0] = (Button) findViewById(R.id.buttonT0);
        button[1] = (Button) findViewById(R.id.buttonT1);
        button[2] = (Button) findViewById(R.id.buttonT2);
        timer = new Timer(this);
        for (int loop1=0; loop1 < 3; loop1++){
            if (timerFlag[loop1]){
                timer.add(loop1, (int)(1000 / Math.pow(2, loop1)));
            }
        }
        setButtonText();
    }

    @Override
    public void onTimer(int id, int sendNum) {
        if (0 <= id && id < 3){
            //androidLog("Receive id " + id);
            time[id]++;
            setButtonText();
        }
    }

    @Override
    protected void onPause() {
        timer.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        timer.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        timer.stop();
        super.onDestroy();
    }

    void setButtonText(){
        for (int loop1=0; loop1 < 3; loop1++){
            String str = String.format("%.2f초\n%s", (time[loop1] / Math.pow(2, loop1)), ((timerFlag[loop1])?"작동중":"멈춤"));
            button[loop1].setText(str);
        }
    }
    public void onButton_TimeSec0(View v){
        timerFlag[0] = !timerFlag[0];
        if (timerFlag[0])timer.add(0, 1000);
        else timer.delete(0);
        setButtonText();
    }
    public void onButton_TimeSec1(View v){
        timerFlag[1] = !timerFlag[1];
        if (timerFlag[1])timer.add(1, 500);
        else timer.delete(1);
        setButtonText();
        //long test = SystemClock.elapsedRealtime();
        //while (SystemClock.elapsedRealtime() - test < 10000);
    }
    public void onButton_TimeSec2(View v){
        timerFlag[2] = !timerFlag[2];
        if (timerFlag[2])timer.add(2, 250);
        else timer.delete(2);
        setButtonText();
    }

    public void onButton_TimerReset(View v){
        for (int loop1=0; loop1 < 3; loop1++){
            if (timerFlag[loop1]){
                timer.delete(loop1);
                timerFlag[loop1]=false;
            }
            time[loop1]=0;
        }
        setButtonText();
    }
    public void onButton_TimerStop(View v){
        Intent intent = new Intent(getApplicationContext(), StopActivity.class);
        startActivity(intent);
    }
    public void onButton_Finish(View v){ finish(); }
}
