package org.learn.servicesplayground;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EmployeeService extends Service {
    private static final String AUTHORITY = "org.learn.employeemanagement.EmployeeContentProvider";
    private static final String PATH = "employees";
    private static final String URI = "content://" + AUTHORITY + "/" + PATH;
    private static final Uri CONTENT_URI = Uri.parse(URI);
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    public static final String BROADCAST_EMPLOYEE_DEPARTMENT = "org.learn.servicesplayground.EMPLOYEE_DEPARTMENT";
    public static final String EMPLOYEE_NAME = "employee_name";
    public static final String EMPLOYEE_DEPARTMENT = "employee_department";

    public EmployeeService() {
        super();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String selection = "name = ?";
            String[] selectionArgs = {bundle.getString(EMPLOYEE_NAME)};
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                String department = cursor.getString(cursor.getColumnIndex("department"));
                publishResults(department);
                cursor.close();
            } else {
                publishResults("Not Found!");
            }
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        String employeeName = intent.getStringExtra(EMPLOYEE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(EMPLOYEE_NAME, employeeName);
        message.setData(bundle);
        serviceHandler.sendMessage(message);
        return super.onStartCommand(intent, flags, startId);
    }

    private void publishResults(String department) {
        Intent intent = new Intent(BROADCAST_EMPLOYEE_DEPARTMENT);
        intent.putExtra(EMPLOYEE_DEPARTMENT, department);
        sendBroadcast(intent);
    }
}
