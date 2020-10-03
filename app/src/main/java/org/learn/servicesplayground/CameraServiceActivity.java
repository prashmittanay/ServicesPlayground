package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class CameraServiceActivity extends AppCompatActivity {
    private static final String TAG = "CameraServiceActivity";
    private ImageView mServiceImageView;
    private Button mCallImageSeriveButton;
    private TextView mProgressTextView;
    private BroadcastReceiver mCameraBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressTextView.setText("");
            String pictureUri = intent.getStringExtra(CameraService.PICTURE_URI);
            drawImage(pictureUri);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_service);
        getPermissions();
        mServiceImageView = findViewById(R.id.camera_image_view);
        mCallImageSeriveButton = findViewById(R.id.camera_call_service);
        mProgressTextView = findViewById(R.id.camera_process_text);
        mCallImageSeriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraService.class);
                startService(intent);
                mProgressTextView.setText("Clicking Imaging and Fetching the Results...");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mCameraBroadcastReceiver, new IntentFilter(CameraService.BROADCAST_CAMERA_URL));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCameraBroadcastReceiver);
    }

    private void drawImage(String pictureUri) {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureUri);
        ImageView myImage = findViewById(R.id.camera_image_view);
        myImage.setImageBitmap(myBitmap);
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}