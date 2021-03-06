package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IncrementorActivity extends AppCompatActivity {
    private Button mIncremntorIntButton;
    private TextView mIntValueTextView;
    private IncrementorService mIncrementorService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementor);
        mIncremntorIntButton = findViewById(R.id.button_incrementor_int_value);
        mIntValueTextView = findViewById(R.id.textview_incrementor_int_value);
        mIncremntorIntButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mBound) {
                    int num = mIncrementorService.getIntStatus();
                    mIntValueTextView.setText(String.valueOf(num));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, IncrementorService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        mBound = false;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IncrementorService.LocalBinder binder = (IncrementorService.LocalBinder) service;
            mIncrementorService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}