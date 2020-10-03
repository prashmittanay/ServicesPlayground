package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class IncrementorService extends Service {
    private int mIntValue = 0;
    private final IBinder iBinder = new LocalBinder();

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
        IncrementorClass incrementorClass = new IncrementorClass();
        incrementorClass.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public int getIntStatus() {
        return mIntValue;
    }

    private class IncrementorClass extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                while (true) {
                    Thread.sleep(900);
                    mIntValue++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

