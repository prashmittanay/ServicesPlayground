package org.learn.servicesplayground;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class EmployeeService extends IntentService {
    private static final String AUTHORITY = "org.learn.employeemanagement.EmployeeContentProvider";
    private static final String PATH = "employees";
    private static final String URI = "content://" + AUTHORITY + "/" +PATH;
    private static final Uri CONTENT_URI = Uri.parse(URI);
    public static final String BROADCAST_EMPLOYEE_DEPARTMENT = "org.learn.servicesplayground.EMPLOYEE_DEPARTMENT";
    public static final String EMPLOYEE_NAME = "employee_name";
    public static final String EMPLOYEE_DEPARTMENT = "employee_department";

    public EmployeeService() {
        super("EmployeeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String employeeName = intent.getStringExtra(EMPLOYEE_NAME);
            String selection = "name = ?";
            String[] selectionArgs = {employeeName};
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                String department = cursor.getString(cursor.getColumnIndex("department"));
                publishResults(department);
                cursor.close();
            } else {
                publishResults("Not Found!");
            }
        }
    }

    private void publishResults(String department) {
        Intent intent = new Intent(BROADCAST_EMPLOYEE_DEPARTMENT);
        intent.putExtra(EMPLOYEE_DEPARTMENT, department);
        sendBroadcast(intent);
    }
}
