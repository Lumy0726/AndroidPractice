package com.android.lmj.firstapp.log;

/**
 * Created by LMJ on 2017-07-23.
 */

import android.os.SystemClock;
import android.util.Log;

public class LogSystem {
    String str = "";
    String distStr = "--";
    int count;
    long startTime;
    boolean outputState = true;
    boolean numberState = false;
    boolean timeState = false;
    boolean distState = true;

    public LogSystem(){
        this("UnNamedLog");
    }
    public LogSystem(String str){
        this.str = str;
        startTime = SystemClock.elapsedRealtime();
    }
    public LogSystem(
            String str,
            String distStr,
            boolean numberState,
            boolean timeState,
            boolean distState){
        this(str);
        this.distStr = distStr;
        this.numberState = numberState;
        this.timeState = timeState;
        this.distState = distState; }
    public LogSystem setStr(String str){ this.str = str; return this; }
    public LogSystem setDistStr(String distStr){this.distStr = distStr; return this; }
    public LogSystem resetCount(){count = 0; return this; }
    public LogSystem resetTime(){startTime = (long)0; return this; }
    public LogSystem setOutputState(boolean state){ outputState = state; return this; }
    public LogSystem setNumberState(boolean state){ numberState = state; return this; }
    public LogSystem setTimeState(boolean state){ timeState = state; return this; }
    public LogSystem setDistState(boolean state){ distState = state; return this; }
    public LogSystem outputLog(String output){
        if (outputState == false){ return this; }
        StringBuilder logName = new StringBuilder(30);
        count++;
        if (distState) logName.append(distStr);
        if (timeState){
            int time = (int)((SystemClock.elapsedRealtime() - startTime) % 3600_000) / 10;
            logName.append(String.format("%02d-%02d-%02d:", time / 6000, time / 100 % 60, time % 100));
        }
        logName.append(str);
        if (numberState) logName.append(String.format(":%04d", count));
        if (distState) logName.append(distStr);
        Log.d(logName.toString(), output);
        return this;
    }


    static LogSystem logSystem = new LogSystem("LMJLOG", "--", true, true, true);
    public static void androidLog(String input){
        logSystem.outputLog(input);
    }
}
