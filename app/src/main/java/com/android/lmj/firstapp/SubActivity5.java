package com.android.lmj.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
사용자 정의 클래스
Service
Intent
SheredPref
TimeEvent
*/

public class SubActivity5 extends AppCompatActivity {
    static boolean[] timeFlag = new boolean[3];
    static int[] time = new int[3];
    Button[] button = new Button[3];
    static {
        for (int loop1=0; loop1 < 3; loop1++) timeFlag[loop1] = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub5);
        button[0] = (Button) findViewById(R.id.buttonT0);
        button[1] = (Button) findViewById(R.id.buttonT1);
        button[2] = (Button) findViewById(R.id.buttonT2);
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        intent.putExtra("command", TimerHandle.START);
        startService(intent);
        for (int loop1=0; loop1 < 3; loop1++){
            if (timeFlag[loop1]){
                intent = new Intent(getApplicationContext(), TimerService.class);
                intent.putExtra("command", TimerHandle.ADD);
                intent.putExtra("period", (int)(1000 / Math.pow(2, loop1)));
                intent.putExtra("id", loop1);
                startService(intent);
            }
        }
        startService(intent);
        setButtonText();
        processIntent(getIntent());
    }

    @Override
    protected void onPause() {
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        intent.putExtra("command", TimerHandle.STOP);
        startService(intent);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        intent.putExtra("command", TimerHandle.START);
        startService(intent);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }

    void processIntent(Intent intent){
        if (intent!=null){
            int id = intent.getIntExtra("id", -1);
            if (0 <= id && id < 3){
                Log.d("Receive id", id + "");
                time[id]++;
                setButtonText();
            }
        }
    }
    void setButtonText(){
        for (int loop1=0; loop1 < 3; loop1++){
            button[loop1].setText((time[loop1] / Math.pow(2, loop1)) + "초\n" + ((timeFlag[loop1])?"작동중":"멈춤"));
        }
    }
    public void onButton_TimeSec0(View v){
        timeFlag[0] = !timeFlag[0];
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        if (timeFlag[0]){
            intent.putExtra("command", TimerHandle.ADD);
            intent.putExtra("period", 1000);
            intent.putExtra("id", 0);
        }
        else {
            intent.putExtra("command", TimerHandle.DELETE);
            intent.putExtra("id", 0);
        }
        startService(intent);
        setButtonText();
    }
    public void onButton_TimeSec1(View v){
        timeFlag[1] = !timeFlag[1];
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        if (timeFlag[1]){
            intent.putExtra("command", TimerHandle.ADD);
            intent.putExtra("period", 500);
            intent.putExtra("id", 1);
        }
        else {
            intent.putExtra("command", TimerHandle.DELETE);
            intent.putExtra("id", 1);
        }
        startService(intent);
        setButtonText();
    }
    public void onButton_TimeSec2(View v){
        timeFlag[2] = !timeFlag[2];
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        if (timeFlag[2]){
            intent.putExtra("command", TimerHandle.ADD);
            intent.putExtra("period", 250);
            intent.putExtra("id", 2);
        }
        else {
            intent.putExtra("command", TimerHandle.DELETE);
            intent.putExtra("id", 2);
        }
        startService(intent);
        setButtonText();
    }

    public void onButton_Finish(View v){ finish(); }
}
