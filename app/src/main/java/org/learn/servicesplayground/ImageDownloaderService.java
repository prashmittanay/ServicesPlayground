package org.learn.servicesplayground;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloaderService extends IntentService {
    private static final String TAG = "ImageDownloaderService";
    public static final String IMAGE_URL = "image_url";
    public static final String DOWNLOAD_COMPLETE_BROADCAST = "org.learn.servicesplayground.IMAGE_DOWNLOADED";
    public static final String IMAGE_PATH = "image_path";

    public ImageDownloaderService() {
        super("ImageDownloaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String urlString = intent.getStringExtra(IMAGE_URL);
            File outputFile = FileUtils.getOutputMediaFile();

            URL imageUrl = null;
            try {
                imageUrl = new URL(urlString);
                Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                outputFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outputFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            publishResults(outputFile.getAbsolutePath());
        }
    }

    private void publishResults(String outputPath) {
        Intent intent = new Intent(DOWNLOAD_COMPLETE_BROADCAST);
        intent.putExtra(IMAGE_PATH, outputPath);
        sendBroadcast(intent);
    }
}
