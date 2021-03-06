package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class IncrementorService extends Service {
    private static final String TAG = "IncrementorService";
    private int mIntValue = 0;
    private final IBinder iBinder = new LocalBinder();
    private IncrementorClass mIncrementorClass;


    public class LocalBinder extends Binder {
        IncrementorService getService() {
            return IncrementorService.this;
        }
    }

    public IncrementorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Incrementor Service Created!");
        mIncrementorClass = new IncrementorClass();
        mIncrementorClass.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIncrementorClass.cancel(true);
        Log.d(TAG, "Incrementor Service Destroyed!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Incrementor Service Binded!");
        return iBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "Incrementor Service Rebinded!");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Incrementor Service Unbinded!");
        return super.onUnbind(intent);
    }

    public int getIntStatus() {
        return mIntValue;
    }

    private class IncrementorClass extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                while (true) {
                    Thread.sleep(50);
                    mIntValue++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

