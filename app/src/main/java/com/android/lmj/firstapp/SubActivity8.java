package com.android.lmj.firstapp;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class SubActivity8 extends AppCompatActivity {
    long backKeyTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);
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
}
