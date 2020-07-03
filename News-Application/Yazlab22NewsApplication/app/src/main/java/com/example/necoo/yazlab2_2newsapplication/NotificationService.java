package com.example.necoo.yazlab2_2newsapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class NotificationService extends Service {
    public int counter=0;
    FileOps fileOps;
    Context ctx;
    Context main;
    NotificationHelper notificationHelper=null;
    private static final int NOTIFICATION_ID = 1;

    public NotificationService(Context applicationContext) {
        super();
        ctx=this;
        main=applicationContext;
        fileOps = new FileOps();
        Log.i("HERE", "here I am!");
    }

    public NotificationService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        notificationHelper = new NotificationHelper(NotificationService.this);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, NotificationBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 10000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));

                    fileOps = new FileOps();
                    String stateNumber = fileOps.read(getApplicationContext(),"notificationState.txt");
                    String newStateNumber = ScrollingActivity.getNotificationState();

                    if(!stateNumber.equals(newStateNumber))
                    {
                        if(!newStateNumber.equals("exit")){
                            notificationHelper.notify("Yeni Haberlerim var :) ","Az önce yeni bir haber eklendi.");
                            fileOps.write(getApplicationContext(),"notificationState.txt",newStateNumber);
                        }

                            //notificationHelper.notify("Çıkış yaptın veya bağlantın kesildi.","Güle Gülee.");


                    }
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

