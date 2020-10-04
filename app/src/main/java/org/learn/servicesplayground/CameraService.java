package org.learn.servicesplayground;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraService extends Service {
    private static final String TAG = "CameraService";
    private Camera mCamera;
    public static final String BROADCAST_CAMERA_URL = "org.learn.servicesplayground.CAMERA_URL";
    public static final String PICTURE_URI = "picture_uri";

    public CameraService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCamera = getCameraInstance();
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewTexture(new SurfaceTexture(10));
            mCamera.startPreview();
            mCamera.takePicture(null, null, mPicture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void publishResults(String pictureUri) {
        Intent intent = new Intent(BROADCAST_CAMERA_URL);
        intent.putExtra(PICTURE_URI, pictureUri);
        sendBroadcast(intent);
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            camera.stopPreview();

            if (pictureFile == null) {
                Log.e(TAG, " >>>> FILE OBJECT NULL");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                publishResults(pictureFile.getAbsolutePath());

            } catch (FileNotFoundException e) {
                e.printStackTrace();              //<-------- show exception
            } catch (IOException e) {
                e.printStackTrace();              //<-------- show exception
            }

            releaseCameraInstance();
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "test");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    private void releaseCameraInstance() {
        mCamera.release();
    }
}
