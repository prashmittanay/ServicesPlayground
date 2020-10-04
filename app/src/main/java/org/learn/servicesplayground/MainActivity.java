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

public class MainActivity extends AppCompatActivity {
    private Button mIntValueButton;
    private Button mAccessProviderButton;
    private Button mCameraButton;
    private Button mDownloadImageButton;
    private TextView mTextView;
    private IncrementorService mIncrementorService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textview_int_value);
        mIntValueButton = findViewById(R.id.button_main_int_value);
        mAccessProviderButton = findViewById(R.id.button_main_access_provider);
        mCameraButton = findViewById(R.id.button_main_camera_service);
        mDownloadImageButton = findViewById(R.id.button_main_download_image);
        mIntValueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mBound) {
                    int num = mIncrementorService.getIntStatus();
                    mTextView.setText(String.valueOf(num));
                }
            }
        });
        mAccessProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccessContentActivity.class);
                startActivity(intent);
            }
        });
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraServiceActivity.class);
                startActivity(intent);
            }
        });
        mDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageDownloaderActivity.class);
                startActivity(intent);
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