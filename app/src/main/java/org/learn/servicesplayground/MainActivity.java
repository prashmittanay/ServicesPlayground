package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button mIncremntorServiceButton;
    private Button mAccessProviderButton;
    private Button mCameraButton;
    private Button mDownloadImageButton;
    private TextView mTextView;
    private IncrementorService mIncrementorService;
    private Intent mIncrementorServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIncrementorServiceIntent = new Intent(this, IncrementorService.class);
        startService(mIncrementorServiceIntent);
        mIncremntorServiceButton = findViewById(R.id.button_main_inc_service);
        mAccessProviderButton = findViewById(R.id.button_main_access_provider);
        mCameraButton = findViewById(R.id.button_main_camera_service);
        mDownloadImageButton = findViewById(R.id.button_main_download_image);
        mIncremntorServiceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IncrementorActivity.class);
                startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIncrementorServiceIntent);
    }
}