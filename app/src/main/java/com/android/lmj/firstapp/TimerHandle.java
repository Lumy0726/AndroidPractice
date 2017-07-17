package com.android.lmj.firstapp;

import android.os.Handler;
import android.os.Message;

/**
 * Created by LMJ on 2017-07-17.
 */

public class TimerHandle extends Handler {
    public static final int STOP = 0;
    public static final int START = 1;
    public static final int ADD = 2;
    public static final int DELETE = 3;
    TimerService timerService;
    TimerHandle(TimerService input){super(); timerService = input;}
    public void handleMessage(Message msg){
        timerService.timeCheck();
    }
}