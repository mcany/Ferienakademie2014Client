package de.ferienakademie.neverrest.controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import de.ferienakademie.neverrest.R;

/**
 * Created by explicat on 10/1/14.
 */
public class NeverrestService extends Service {


    public static final String TAG = NeverrestService.class.getSimpleName();
    private IBinder mBinder = new NeverrestServiceBinder();

    public class NeverrestServiceBinder extends Binder {
        public NeverrestService getService() {
            return NeverrestService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    /*
        MediaPlayer mp = MediaPlayer.
        MediaPlayer mp = MediaPlayer.create(Test.this, R.raw.mysound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }

        });
        mp.start();
    }

    */
}
