package com.android.lmj.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SubActivity3 extends AppCompatActivity {

    Button button;
    TextView textView;
    ImageView imageView;
    int imageFlag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        button = (Button)findViewById(R.id.button16);
        textView = (TextView)findViewById(R.id.textView3);
        imageView = (ImageView)findViewById(R.id.imageView);
        ViewGroup.LayoutParams layoutText = (ViewGroup.LayoutParams) textView.getLayoutParams();
        ViewGroup.LayoutParams layoutImage = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        layoutText.height = 1500;
        layoutText.width= 1500;
        layoutImage.height = 1500;
        layoutImage.width= 1500;
    }

    public void onButton_Finish(View v){ finish(); }
    public void onButton_Image(View v){
        if (imageFlag == 1){
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            button.setText("이미지 보이기");
            imageFlag = 0;
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            button.setText("이미지 감추기");
            imageFlag = 1;
        }
    }
}
