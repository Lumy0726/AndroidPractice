package com.android.lmj.firstapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.android.lmj.firstapp.tools.Tools;

/**
 * Created by LMJ on 2017-08-01.
 */

public class Button_Cancel extends AppCompatButton{
    Button_Cancel(Context context){ super(context); }
    Button_Cancel(Context context, AttributeSet att){ super(context, att); }
    void resize(){
        int w = getWidth(), h = getHeight();
        if (w > h){
            setWidth(h);
        }
        else {
            setHeight(w);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Tools.resetBitmap(canvas, 0xff000000);
        int w = canvas.getWidth(), h = canvas.getHeight();
        int wide = (int)Tools.dipToPix(7);
        w -= wide; h -= wide;
        canvas.drawRect(wide, wide, w, h, Tools.colorPaint(0xffffffff));
        canvas.drawLine(wide, wide, w, h, Tools.linePaint(0xffff0000, wide / 2));
        canvas.drawLine(wide, h, w, wide, Tools.linePaint(0xffff0000, wide / 2));
    }
}
