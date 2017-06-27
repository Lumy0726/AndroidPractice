package com.android.lmj.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SubActivity1 extends AppCompatActivity {

    protected int num=0;
    protected static int staticNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);
    }

    public void finishActivity(View v){
        finish();
    }

    public void onCountButton(View v){
        Toast.makeText(getApplicationContext(), "Button Clicked, static실행횟수: " + ++staticNum + ", 실행횟수: " + ++num, Toast.LENGTH_SHORT).show();
    }

    public void onButton_LinearLayout(View v){
        Intent intent=new Intent(getApplicationContext(), LinearActivity1.class);
        startActivity(intent);
    }
}
