package com.android.lmj.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LinearActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear1);
    }

    public void finishActivity(View v){
        Toast.makeText(getApplicationContext(), "Linear Activity teminate", Toast.LENGTH_SHORT).show();
        finish();
    }
}
