package com.android.lmj.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static int buttonClickNum = 0;
    public static final int REQUEST_CODE_ACT6 = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ConstraintLayout rootLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
        Button button = (Button) findViewById(R.id.button3);
        button.setText("가로: " + rootLayout.getWidth() + "\n세로: " + rootLayout.getHeight());
        super.onWindowFocusChanged(hasFocus);
    }

    public void onButton1Clicked(View v){
        String output= ++buttonClickNum + "회, 잘했어!";
        //이전 메세지가 사라지기 전까지 새로운 메세지가 뜨지 않는구나..
        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
    }
    public void onButton_GoogleConnect(View v){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.co.kr"));
        startActivity(myIntent);
    }

    public void onButton_CallConnect(View v){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1010-1100"));
        startActivity(myIntent);
    }

    void processIntent(Intent input){
        int restartTime = input.getIntExtra("restartTime", 0);
        if (restartTime > 0){
            restartTime /= 1000;
            int second = restartTime % 60;
            restartTime /= 60;
            int minute = restartTime % 60;
            int hour = restartTime / 60;

            String str = "";
            if (hour != 0) str+=(hour + "시간 ");
            if (minute != 0) str+=(minute + "분 ");
            if (second != 0) str+=(second + "초 ");
            str += "후 재시작하였습니다.";
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), RestartService.class);
            stopService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACT6){
            if (resultCode == RESULT_OK){
                if (data != null){
                    boolean isRestart = data.getBooleanExtra("restart", false);
                    if (isRestart){
                        int hour = data.getIntExtra("hour", 0);
                        int minute = data.getIntExtra("minute", 0);
                        int second = data.getIntExtra("second", 0);
                        String str = "";
                        if (hour != 0) str+=(hour + "시간 ");
                        if (minute != 0) str+=(minute + "분 ");
                        if (second != 0) str+=(second + "초 ");
                        str += "후 재시작합니다...";
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    }

    public void onButton_NewActivity1(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity1.class);
        startActivity(intent);
    }

    public void onButton_NewActivity2(View v, Intent intent1){
        Intent intent = new Intent(getApplicationContext(), SubActivity2.class);
        startActivity(intent);
    }

    public void onButton_NewActivity3(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity3.class);
        startActivity(intent);
    }

    public void onButton_NewActivity4(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity4.class);
        startActivity(intent);
    }

    public void onButton_NewActivity5(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity5.class);
        startActivity(intent);
    }

    public void onButton_NewActivity6(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity6.class);
        startActivityForResult(intent, REQUEST_CODE_ACT6);
    }
}
