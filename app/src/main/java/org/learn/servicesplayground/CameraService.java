package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraService extends Service {
    private static final String TAG = "CameraService";
    private int mRotation = 0;
    public static Camera mCamera;
    public static CameraServiceActivity.CameraPreview mCameraPreview;
    public static final String BROADCAST_CAMERA_URL = "org.learn.servicesplayground.CAMERA_URL";
    public static final String PICTURE_URI = "picture_uri";
    public static final String DISPLAY_ORIENTATION = "orientation";

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
        mRotation = intent.getIntExtra(DISPLAY_ORIENTATION, 0);
        mCamera.takePicture(null, null, mPicture);
        return START_NOT_STICKY;
    }

    private void publishResults(String pictureUri) {
        Intent intent = new Intent(BROADCAST_CAMERA_URL);
        intent.putExtra(PICTURE_URI, pictureUri);
        sendBroadcast(intent);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = FileUtils.getOutputMediaFile();
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            bitmap = CameraUtils.rotateImage(bitmap, mRotation);
            if (pictureFile == null) {
                Log.e(TAG, " >>>> FILE OBJECT NULL");
                return;
            }
            FileOutputStream fileOutputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                pictureFile.createNewFile();
                fileOutputStream = new FileOutputStream(pictureFile);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                publishResults(pictureFile.getAbsolutePath());
                CameraUtils.releaseCameraInstance(mCamera);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream!= null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
