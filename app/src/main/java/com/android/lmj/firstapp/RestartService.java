package com.android.lmj.firstapp;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.android.lmj.firstapp.timer.Timer;
import com.android.lmj.firstapp.timer.TimerAble;

public class RestartService extends Service implements TimerAble {

    long startTime;
    int restartTime;

    public RestartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences timePref = getSharedPreferences("timePref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timePref.edit();
        editor.putLong("CurSysTime", System.currentTimeMillis());
        editor.putLong("CurClockTime", SystemClock.elapsedRealtime());
        editor.putLong("StartClockTime", startTime);
        editor.putInt("RestartTime", restartTime);
        editor.commit();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            SharedPreferences timePref = getSharedPreferences("timePref", Activity.MODE_PRIVATE);
            if (timePref!=null){
                long curSysTime = System.currentTimeMillis();
                long curClockTime = SystemClock.elapsedRealtime();
                long prevSysTime = timePref.getLong("CurSysTime", (long)0);
                long prevClockTime = timePref.getLong("CurClockTime", (long)0);
                if (0 < curSysTime - prevSysTime && curSysTime - prevSysTime < (long)10000){
                    if (0 < curClockTime - prevClockTime && curClockTime - prevClockTime < (long)10000){
                        startTime = timePref.getLong("StartClockTime", (long)0);
                        restartTime = timePref.getInt("RestartTime", 0);
                        int delay = restartTime + (int)(startTime - SystemClock.elapsedRealtime());
                        Timer.sendOneTimer(this, 0, (delay > 0) ? delay : 1);
                    }
                }
                timePref.edit().clear();
                timePref.edit().commit();
            }
            return Service.START_STICKY;
        }
        else {
            restartTime = intent.getIntExtra("restartTime", 0);
            if (restartTime > 0){
                startTime = SystemClock.elapsedRealtime();
                Timer.sendOneTimer(this, 0, restartTime);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTimer(int id, int sendNum) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("restartTime", restartTime);
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
