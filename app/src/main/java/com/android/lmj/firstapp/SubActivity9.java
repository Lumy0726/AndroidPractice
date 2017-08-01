package com.android.lmj.firstapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity9 extends AppCompatActivity {
    //System value.
    LayoutInflater inflater;
    //View
    LinearLayout sub9Container;
    TextView textView_Number;
    EditText editText_Str;
    //string number.
    int strNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub9);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sub9Container = (LinearLayout) findViewById(R.id.sub9_Container);
        textView_Number = (TextView) findViewById(R.id.textView_Number);
        editText_Str = (EditText) findViewById(R.id.editText_Str);
        Button button = (Button) findViewById(R.id.button_Confirm);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { onButton_Confirm(); }
        });
        button = (Button) findViewById(R.id.button36);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { save(); }
        });
        button = (Button) findViewById(R.id.button37);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { sendToSD(); }
        });
        load();
    }
    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }

    void load(){
        //Save Str.
        Toast.makeText(getApplicationContext(), "load fail", Toast.LENGTH_SHORT).show();
    }
    void save(){
        //recovery Str.
        Toast.makeText(getApplicationContext(), "save fail", Toast.LENGTH_SHORT).show();
    }
    void sendToSD(){
        //send string to sdCard.
        Toast.makeText(getApplicationContext(), "send To SDCard fail", Toast.LENGTH_SHORT).show();
    }
    public void onButton_Confirm(){
        inflater.inflate(R.layout.activity_sub9_list, sub9Container, true);
        Button button = (Button) sub9Container.getChildAt(strNum).findViewById(R.id.button_Delete);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onButton_Delete((View)v.getParent().getParent());
            }
        });
        TextView textView = (TextView) sub9Container.getChildAt(strNum).findViewById(R.id.textView_Str);
        textView.setText(editText_Str.getText().toString());
        editText_Str.setText("");
        textView = (TextView) sub9Container.getChildAt(strNum).findViewById(R.id.textView_Number);
        textView.setText("" + strNum);
        strNum++;
    }
    public void onButton_Delete(View v){
        sub9Container.removeView(v);
        strNum--;
        TextView textView;
        for (int loop1=0; loop1 < strNum; loop1++){
            textView = (TextView) sub9Container.getChildAt(loop1).findViewById(R.id.textView_Number);
            textView.setText("" + loop1);
        }
    }
    public void onButton_Finish(View v){ finish(); }
}
