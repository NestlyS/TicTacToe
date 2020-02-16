package com.example.mygame;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public static final String GAME_PREFERENCES = "GamePrefs";
    public static final String GAME_SETTINGS = "GameSettings" ;
    private MediaPlayer mPlayer;
    private boolean mIsBound =  false;
    private MusicService mServ;
    private ScreenReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService .class);
        startService(music);

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mServ != null){
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        boolean isScreenOff = false;

        for (Display display : dm.getDisplays()) {
            if (display.getState() == Display.STATE_OFF) {
                isScreenOff = true;
                break;
            }
        }
        if(isScreenOff){
            if(mServ != null){
                mServ.pauseMusic();
            }
        }

        if (mReceiver.wasScreenOn) {
            mServ.pauseMusic();

        }
    }

    @Override
    protected void onUserLeaveHint() {
        if(mServ != null)
            mServ.pauseMusic();
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        if(mServ != null)
            mServ.pauseMusic();
        super.onBackPressed();
    }

    private ServiceConnection Scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServ = ((MusicService.ServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService(){
        if(mIsBound){
            unbindService(Scon);
            mIsBound = false;
        }
    }

    public class ScreenReceiver extends BroadcastReceiver {

        public boolean wasScreenOn = true;

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                mServ.pauseMusic();
                wasScreenOn = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // and do whatever you need to do here
                wasScreenOn = true;
                mServ.resumeMusic();
            }
        }

    }
}
