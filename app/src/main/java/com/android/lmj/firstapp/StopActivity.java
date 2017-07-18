package com.android.lmj.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        TextView textView = (TextView) findViewById(R.id.textView10);
        Button button = (Button) findViewById(R.id.button25);
        button.setWidth(textView.getWidth());
        super.onWindowFocusChanged(hasFocus);
    }

    public void onButton_Finish(View v){ finish(); }
}
