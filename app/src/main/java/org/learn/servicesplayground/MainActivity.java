package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button mIncremntorServiceButton;
    private Button mAccessProviderButton;
    private Button mCameraButton;
    private Button mDownloadImageButton;
    private Button mRemoteIncrementorButton;
    private TextView mTextView;
    private IncrementorService mIncrementorService;
    private Intent mIncrementorServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        mIncrementorServiceIntent = new Intent(this, IncrementorService.class);
        mIncremntorServiceButton = findViewById(R.id.button_main_inc_service);
        mAccessProviderButton = findViewById(R.id.button_main_access_provider);
        mCameraButton = findViewById(R.id.button_main_camera_service);
        mDownloadImageButton = findViewById(R.id.button_main_download_image);
        mRemoteIncrementorButton = findViewById(R.id.button_main_remote_incrementor);
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
        mRemoteIncrementorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RemoteIncrementorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET}, 1);
        }
    }
}