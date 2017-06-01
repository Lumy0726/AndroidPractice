package com.android.lmj.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected int buttonClickNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v){
        String output=buttonClickNum + "회, 잘했어!";
        //이전 메세지가 사라지기 전까지 새로운 메세지가 뜨지 않는구나..
        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        buttonClickNum++;
    }
}
