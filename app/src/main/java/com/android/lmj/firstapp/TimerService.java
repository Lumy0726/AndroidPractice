package com.android.lmj.firstapp;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //primary code.
    public static final int TIMER_MAX = 10;
    int timerNum = 0;
    long[] timeSave = new long[TIMER_MAX];
    int[] timePeriod = new int[TIMER_MAX];
    int[] timeID = new int[TIMER_MAX];
    TimerHandle timerHandle;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            return Service.START_STICKY;
        }
        else {
            int command = intent.getIntExtra("command", -1);
            int period, id, loop1;
            switch(command){
                case TimerHandle.ADD:
                    period = intent.getIntExtra("period", 1);
                    id = intent.getIntExtra("id", 0);
                    for (loop1 = 0; loop1 < timerNum; loop1++){
                        if (timeID[loop1] == id){
                            timePeriod[loop1] = period;
                            break;
                        }
                    }
                    if (loop1 == timerNum && timerNum < TIMER_MAX){
                        timePeriod[timerNum] = period;
                        timeID[timerNum] = id;
                        timeSave[timerNum++] = SystemClock.elapsedRealtime();
                    }
                    break;
                case TimerHandle.DELETE:
                    id = intent.getIntExtra("id", 0);
                    for (loop1=0; loop1 < timerNum; loop1++){
                        if (timeID[loop1] == id){
                            if (loop1 != timerNum - 1){
                                timePeriod[loop1] = timePeriod[timerNum - 1];
                                timeID[loop1] = timeID[timerNum - 1];
                                timeSave[loop1] = timeSave[timerNum - 1];
                            }
                            timerNum--;
                            break;
                        }
                    }
                    break;
                case TimerHandle.START:
                    long time= SystemClock.elapsedRealtime();
                    for (loop1 = 0; loop1 < timerNum; loop1++){ timeSave[loop1] = time; }
                    timerHandle = new TimerHandle(this);
                    timerHandle.sendEmptyMessageDelayed(0, 1);
                    break;
                case TimerHandle.STOP:
                    timerHandle = null;
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void timeCheck(){
        long time = SystemClock.elapsedRealtime();
        for (int loop1 = 0; loop1 < timerNum; loop1++){
            Log.d("send loop1", loop1 + "");
            while (time - timeSave[loop1] >= (long)timePeriod[loop1]){
                //SubActivity5.class 를 동적으로 결정할 수 있는 방법?
                Intent intent = new Intent(getApplicationContext(), SubActivity5.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", timeID[loop1]);
                Log.d("send id", timeID[loop1] + "");
                startActivity(intent);
                timeSave[loop1]+=(long)timePeriod[loop1];
            }
        }
        if (timerHandle != null){
            timerHandle.sendEmptyMessageDelayed(0, 1);
        }
    }
}