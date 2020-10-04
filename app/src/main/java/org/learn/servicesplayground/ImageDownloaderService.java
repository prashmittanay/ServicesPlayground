package org.learn.servicesplayground;

import android.app.IntentService;
import android.content.Intent;

import java.io.File;
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
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                URL url = new URL(urlString);
                inputStream = url.openConnection().getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                fileOutputStream = new FileOutputStream(outputFile.getPath());
                int next = -1;
                while ((next = inputStreamReader.read()) != -1) {
                    fileOutputStream.write(next);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
