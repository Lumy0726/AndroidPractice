package com.android.lmj.firstapp;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SubActivity4 extends AppCompatActivity {
    public static final int makeNumMax = 8;
    int makeNum = 0;
    long time;
    LayoutInflater inflater;
    LinearLayout[] container = new LinearLayout[makeNumMax + 1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        container[0] = (LinearLayout) findViewById(R.id.container);
        //time = SystemClock.elapsedRealtime();
        //while (SystemClock.elapsedRealtime() - time < 10000);
    }

    public void onButton_Make(View v){
        if (makeNum < 8){
            inflater.inflate(R.layout.inflate_recursive,container[makeNum++], true);
            container[makeNum] = (LinearLayout) container[makeNum - 1].findViewById(R.id.containerIn).findViewById(R.id.container);
        }
        else {
            Toast.makeText(getApplicationContext(), "더 만들시 위험합니다...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onButton_Delete(View v){
        if (makeNum > 0){
            container[makeNum--] = null;
            container[makeNum].removeAllViews();
        }
        else {
            Toast.makeText(getApplicationContext(), "삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onButton_Finish(View v){ finish(); }
}
