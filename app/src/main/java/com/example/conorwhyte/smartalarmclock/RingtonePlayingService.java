package com.example.conorwhyte.smartalarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.example.conorwhyte.smartalarmclock.R;

public class RingtonePlayingService extends Service {
    MediaPlayer media_song;
    private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getExtras().getString("extra");
        assert state != null;
        switch (state)
        {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }
        if (!this.isRunning && startId == 1)
        {
            Log.e("Going on here  ", "Served");
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();


        }
        if(startId ==0)
        {
           // media_song.stop();
            //mMediaPlayer.reset();

            // this.isRunning = false;
        }

        return START_NOT_STICKY;
    }


}