package com.android.lmj.firstapp;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SubActivity4 extends AppCompatActivity {
    int makeNum = 0;
    long time;
    LayoutInflater inflater;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        container = (LinearLayout) findViewById(R.id.container);
        //time = SystemClock.elapsedRealtime();
        //while (SystemClock.elapsedRealtime() - time < 10000);
    }

    public void onButton_Make(View v){
        if (makeNum < 100){
            inflater.inflate(R.layout.inflate_recursive, container, true);
            container = (LinearLayout) container.findViewById(R.id.containerIn);
            container = (LinearLayout) container.findViewById(R.id.container);
            makeNum++;
        }
    }

    public void onButton_Delete(View v){

    }

    public void onButton_Finish(View v){ finish(); }
}
