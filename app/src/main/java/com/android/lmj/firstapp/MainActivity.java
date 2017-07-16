package com.android.lmj.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected int buttonClickNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v){
        String output= ++buttonClickNum + "회, 잘했어!";
        //이전 메세지가 사라지기 전까지 새로운 메세지가 뜨지 않는구나..
        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
    }
    public void onButton_GoogleConnect(View v){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.co.kr"));
        startActivity(myIntent);
    }

    public void onButton_CallConnect(View v){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1010-1100"));
        startActivity(myIntent);
    }

    public void onButton_NewActivity1(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity1.class);
        startActivity(intent);
    }

    public void onButton_NewActivity2(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity2.class);
        startActivity(intent);
    }

    public void onButton_NewActivity3(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity3.class);
        startActivity(intent);
    }
}
