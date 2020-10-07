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
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;

public class CameraServiceActivity extends AppCompatActivity {
    private static final String TAG = "CameraServiceActivity";
    //private ImageView mServiceImageView;
    private FrameLayout mServiceFrameLayout;
    private Button mCallImageSeriveButton;
    private TextView mProgressTextView;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private BroadcastReceiver mCameraBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressTextView.setText("");
            String pictureUri = intent.getStringExtra(CameraService.PICTURE_URI);
            //drawImage(pictureUri);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_service);
        getPermissions();
        mServiceFrameLayout = findViewById(R.id.camera_image_view);
        mCallImageSeriveButton = findViewById(R.id.camera_call_service);
        mProgressTextView = findViewById(R.id.camera_process_text);
        mCamera = CameraService.getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        mServiceFrameLayout.addView(mCameraPreview);
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
//        ImageView myImage = findViewById(R.id.camera_image_view);
//        myImage.setImageBitmap(myBitmap);
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }
}