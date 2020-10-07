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
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
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
    //private ImageView mServiceImageView;
    private FrameLayout mServiceFrameLayout;
    private Button mCallImageSeriveButton;
    private TextView mProgressTextView;
    private BroadcastReceiver mCameraBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressTextView.setText("");
            String pictureUri = intent.getStringExtra(CameraService.PICTURE_URI);
            try {
                drawImage(pictureUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        CameraService.mCamera = CameraService.getCameraInstance();
        CameraService.mCameraPreview = new CameraPreview(this, CameraService.mCamera);
        mServiceFrameLayout.addView(CameraService.mCameraPreview);
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mCameraBroadcastReceiver);
    }

    private void drawImage(String pictureUri) throws IOException {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureUri);
        ExifInterface ei = new ExifInterface(pictureUri);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_ROTATE_90);

        myBitmap = rotateImage(myBitmap, 90);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mServiceFrameLayout.removeAllViews();
        mServiceFrameLayout.addView(imageView);
        imageView.setImageBitmap(myBitmap);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            //mCamera.stopPreview();
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