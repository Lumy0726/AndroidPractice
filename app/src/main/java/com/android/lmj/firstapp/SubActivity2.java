package com.android.lmj.firstapp;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SubActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
    }

    public void onButton6(View v){
        Button button6 = (Button) findViewById(R.id.button6);
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams)button6.getLayoutParams();
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)button6.getLayoutParams();
        //marginValue=(int)(Math.random() * 200);
        //margin.setMargins(0, marginValue, 0, 0);
        layout.verticalBias=(float)Math.random();
        button6.requestLayout();
    }

    public void onButton7(View v){
        finish();
    }
}