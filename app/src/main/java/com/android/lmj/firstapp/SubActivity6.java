package com.android.lmj.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity6 extends AppCompatActivity {

    EditText inputHour;
    EditText inputMinute;
    EditText inputSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub6);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        TextView textView = (TextView) findViewById(R.id.textView_Information);
        Button button = (Button) findViewById(R.id.button_confirm);
        inputHour = (EditText) findViewById(R.id.editText_Hour);
        inputMinute = (EditText) findViewById(R.id.editText_Minute);
        inputSecond = (EditText) findViewById(R.id.editText_Second);
        button.setHeight(textView.getHeight());
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int hour, minute, second;
                String str;
                try{
                    str = inputHour.getText().toString();
                    if (str.equals("")) hour = 0;
                    else hour = Integer.parseInt(str);
                    str = inputMinute.getText().toString();
                    if (str.equals("")) minute = 0;
                    else minute = Integer.parseInt(str);
                    str = inputSecond.getText().toString();
                    if (str.equals("")) second = 0;
                    else second = Integer.parseInt(str);
                    if (hour < 0 || minute < 0 || second < 0){
                        throw new NumberFormatException("negative number");
                    }
                    while (second >= 60){
                        minute++; second -= 60;
                    }
                    while ( minute >= 60){
                        hour++; minute -= 60;
                    }
                    if (hour >= 24){
                        throw new Exception("too large hour");
                    }
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "값이 너무 크거나 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hour == 0 && minute == 0 && second == 0){
                    Toast.makeText(getApplicationContext(), "값을 입력하여 주십시오", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), RestartService.class);
                intent.putExtra("restartTime", (hour * 3600 + minute * 60 + second) * 1000);
                startService(intent);
                intent = new Intent();
                intent.putExtra("restart", true);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("second", second);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        super.onWindowFocusChanged(hasFocus);
    }

    public void onButton_Finish(View v){
        setResult(RESULT_OK);
        finish();
    }
}
