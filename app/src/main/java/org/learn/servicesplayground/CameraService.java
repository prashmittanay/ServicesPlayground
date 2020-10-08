package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
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

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = FileUtils.getOutputMediaFile();
            ExifInterface exifInterface = null;
            try {
                exifInterface = new ExifInterface(new ByteArrayInputStream(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            bitmap = CameraUtils.rotateImage(bitmap, 90);


            if (pictureFile == null) {
                Log.e(TAG, " >>>> FILE OBJECT NULL");
                return;
            }
            FileOutputStream fos = null;
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                pictureFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(pictureFile);
                fo.write(bytes.toByteArray());
                publishResults(pictureFile.getAbsolutePath());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos!= null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            CameraUtils.releaseCameraInstance(mCamera);
        }
    };
}
