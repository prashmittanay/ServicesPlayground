package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RemoteIncrementorActivity extends AppCompatActivity {
    private static final String TAG = "RemoteIncrementorActivity";
    private TextView mRemoteValueTextView;
    private Button mRemoteValueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_incrementor);
        mRemoteValueButton = findViewById(R.id.button_remote_int_value);
        mRemoteValueTextView = findViewById(R.id.textview_remote_int_value);
        mRemoteValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }
}