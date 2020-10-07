package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraService extends Service {
    private static final String TAG = "CameraService";
    public static Camera mCamera;
    public static CameraServiceActivity.CameraPreview mCameraPreview;
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
        mCamera.takePicture(null, null, mPicture);
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
            File pictureFile = FileUtils.getOutputMediaFile();
            camera.stopPreview();

            if (pictureFile == null) {
                Log.e(TAG, " >>>> FILE OBJECT NULL");
                return;
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pictureFile);
                fos.write(data);


                publishResults(pictureFile.getAbsolutePath());

            } catch (FileNotFoundException e) {
                e.printStackTrace();              //<-------- show exception
            } catch (IOException e) {
                e.printStackTrace();              //<-------- show exception
            } finally {
                if (fos!= null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            releaseCameraInstance();
        }
    };

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    public static void releaseCameraInstance() {
        mCamera.stopPreview();
        mCamera.release();
    }


}
