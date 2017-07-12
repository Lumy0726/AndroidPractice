package com.android.lmj.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity3 extends AppCompatActivity {

    Button button;
    TextView textView;
    ImageView imageView;
    static int imageFlag=1;
    static int imageSize=800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        button = (Button)findViewById(R.id.button16);
        textView = (TextView)findViewById(R.id.textView3);
        imageView = (ImageView)findViewById(R.id.imageView);
        setViewSize(imageSize);
        setViewFlag(imageFlag);
        //ViewGroup.LayoutParams layoutText = (ViewGroup.LayoutParams) textView.getLayoutParams();
        //ViewGroup.LayoutParams layoutImage = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        //layoutText.height = 1500;
        //layoutText.width= 1500;
        //layoutImage.height = 1500;
        //layoutImage.width= 1500;
        //layoutText.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 800, getResources().getDisplayMetrics());
        //layoutImage.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 800, getResources().getDisplayMetrics());
    }

    protected int dipToPix(int dip){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
    protected void setViewSize(int dip){
        ViewGroup.LayoutParams layoutText = (ViewGroup.LayoutParams) textView.getLayoutParams();
        ViewGroup.LayoutParams layoutImage = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        layoutText.width = layoutText.height = layoutImage.width = layoutImage.height = dipToPix(dip);
        textView.requestLayout(); imageView.requestLayout();
    }
    protected void setViewFlag(int flag){
        if (flag == 1){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            button.setText("이미지 감추기");
        }
        else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            button.setText("이미지 보이기");
        }
    }
    public void onButton_Finish(View v){ finish(); }
    public void onButton_Image(View v){
        setViewFlag(imageFlag = (imageFlag + 1) % 2);
    }
    public void onButton_ImageEx(View v){
        setViewSize(imageSize+=100);
    }
    public void onButton_ImageCt(View v){
        if (imageSize < 200){
            Toast.makeText(getApplicationContext(), "축소 불가", Toast.LENGTH_SHORT).show();
            return;
        }
        setViewSize(imageSize-=100);
    }
}
