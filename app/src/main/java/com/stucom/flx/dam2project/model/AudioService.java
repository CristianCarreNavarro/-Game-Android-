package com.stucom.flx.dam2project.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;

import com.stucom.flx.dam2project.R;

import static android.app.Service.START_STICKY;

public class AudioService extends Service{


    static final int DECREASE = 1, INCREASE = 2, START = 3, PAUSE = 4;
    Boolean shouldPause = false;
    //Music
    MediaPlayer mediaPlayer;
    //Sound
    private SoundPool soundPool;

    public void startLoop(){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.worms2);
        }
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void decrease(){
        mediaPlayer.setVolume(0.2f, 0.2f);
    }
    private void increase(){
        mediaPlayer.setVolume(1.0f, 1.0f);
    }
    private void start(){
        startLoop();
        shouldPause = false;
    }

    private void pause(){
        shouldPause = true;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(shouldPause) {
                            mediaPlayer.pause();
                        }
                    }
                }, 100);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(getClass().getSimpleName(), "Creating service");
    }

    //startService(intent)



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startLoop();
        Log.i(getClass().getSimpleName(), "Intent received");

        try {
            int actionDefault = 0;
            int action = actionDefault;

            if(intent != null){
                if(intent.hasExtra("action")){
                    action = intent.getIntExtra("action", actionDefault);
                }
            }

            switch (action) {
                case INCREASE:
                    increase();
                    break;
                case DECREASE:
                    decrease();
                    break;
                case START:
                    start();
                    break;
                case PAUSE:
                    pause();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
