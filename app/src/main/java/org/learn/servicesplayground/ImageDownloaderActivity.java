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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloaderActivity extends AppCompatActivity {
    private static final String TAG = "ImageDownloaderActivity";
    private ImageView mDownloadedImageView;
    private Button mDownloadImageButton;
    private TextView mPendingTextView;
    private BroadcastReceiver mImageDownloadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPendingTextView.setText("");
            String imagePath = intent.getStringExtra(ImageDownloaderService.IMAGE_PATH);
            drawImage(imagePath);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloader);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        mDownloadedImageView = findViewById(R.id.imgview_download_image);
        mDownloadImageButton = findViewById(R.id.button_download_image);
        mPendingTextView = findViewById(R.id.textview_image_download_message);
        mDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPendingTextView.setText("Downloading Image ... ");
                Intent intent = new Intent(getApplicationContext(), ImageDownloaderService.class);
                intent.putExtra(ImageDownloaderService.IMAGE_URL, "https://i.imgur.com/4nusSJC.jpeg");
                startService(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mImageDownloadBroadcastReceiver, new IntentFilter(ImageDownloaderService.DOWNLOAD_COMPLETE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mImageDownloadBroadcastReceiver);
    }

    private void drawImage(String pictureUri) {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureUri);
        mDownloadedImageView.setImageBitmap(myBitmap);
    }
}