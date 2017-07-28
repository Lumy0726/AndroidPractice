package com.android.lmj.firstapp.joystick;

import com.android.lmj.firstapp.tools.Tools;

/**
 * Created by LMJ on 2017-07-27.
 */

//TODO
public class JoyStick {
    int size = (int)Tools.dipToPix(40);
    int wide = (int)Tools.dipToPix(10);
    public JoyStick(){
        super();
        init();
    }
    public JoyStick(int dip){
        size = (int)Tools.dipToPix(dip);
        init();
    }
    public void init(){

    }
}
