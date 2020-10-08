package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;

public class CameraServiceActivity extends AppCompatActivity {
    private static final String TAG = "CameraServiceActivity";
    private FrameLayout mServiceFrameLayout;
    private Button mCallImageSeriveButton;
    private TextView mProgressTextView;
    private Intent mCameraServiceIntent;
    private int mDisplayOrientation;
    private boolean isReleaseRequired = true;

    private BroadcastReceiver mCameraBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pictureUri = intent.getStringExtra(CameraService.PICTURE_URI);
            try {
                drawImage(pictureUri);
                mProgressTextView.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_service);
        mServiceFrameLayout = findViewById(R.id.camera_image_view);
        mCallImageSeriveButton = findViewById(R.id.camera_call_service);
        mProgressTextView = findViewById(R.id.camera_process_text);
        CameraService.mCamera = CameraUtils.getCameraInstance();
        CameraService.mCameraPreview = new CameraPreview(this, CameraService.mCamera);
        mServiceFrameLayout.addView(CameraService.mCameraPreview);

        mCallImageSeriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReleaseRequired = false;
                mCameraServiceIntent = new Intent(getApplicationContext(), CameraService.class);
                mCameraServiceIntent.putExtra(CameraService.DISPLAY_ORIENTATION, mDisplayOrientation);
                startService(mCameraServiceIntent);
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mCameraBroadcastReceiver);
        if (mCameraServiceIntent != null) {
            stopService(mCameraServiceIntent);
        }
        if (isReleaseRequired)
            CameraUtils.releaseCameraInstance(CameraService.mCamera);
    }

    private void drawImage(String pictureUri) throws IOException {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureUri);
        mServiceFrameLayout.removeAllViews();
        ImageView imageView = new ImageView(this);
        mServiceFrameLayout.addView(imageView);
        imageView.setImageBitmap(myBitmap);
        ((ViewGroup)mCallImageSeriveButton.getParent()).removeView(mCallImageSeriveButton);
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mDisplayOrientation = CameraUtils.getCameraDisplayOrientation(CameraServiceActivity.this,
                        Camera.CameraInfo.CAMERA_FACING_BACK, CameraService.mCamera);
                mCamera.setDisplayOrientation(mDisplayOrientation);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mHolder.getSurface() == null) {
                return;
            }
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }
}