package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageDownloaderActivity extends AppCompatActivity {
    private static final String TAG = "ImageDownloaderActivity";
    private ImageView mDownloadedImageView;
    private Button mDownloadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloader);
        mDownloadedImageView = findViewById(R.id.imgview_download_image);
        mDownloadImageButton = findViewById(R.id.button_download_image);
        mDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}