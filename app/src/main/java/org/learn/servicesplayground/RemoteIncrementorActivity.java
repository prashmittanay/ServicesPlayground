package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.learn.remoteincrementorservice.IIncrementor;

public class RemoteIncrementorActivity extends AppCompatActivity {
    private static final String TAG = "RemoteIncActivity";
    private TextView mRemoteValueTextView;
    private Button mRemoteValueButton;
    private IIncrementor iIncrementorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_incrementor);
        mRemoteValueButton = findViewById(R.id.button_remote_int_value);
        mRemoteValueTextView = findViewById(R.id.textview_remote_int_value);
        mRemoteValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int currentValue = iIncrementorService.getCurrentValue();
                    mRemoteValueTextView.setText(String.valueOf(currentValue));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initConnection() {
        if (iIncrementorService == null) {
            Intent intent = new Intent(IIncrementor.class.getName());
            intent.setAction("remote.incrementor.service");
            intent.setPackage("org.learn.remoteincrementorservice");
            bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service Connected");
            iIncrementorService = IIncrementor.Stub.asInterface((IBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service Disconnected");
            iIncrementorService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (iIncrementorService == null) {
            initConnection();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }
}