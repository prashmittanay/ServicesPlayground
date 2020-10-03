package org.learn.servicesplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccessContentActivity extends AppCompatActivity {
    private Button mAccessContentButton;
    private EditText mNameEditText;
    private TextView mDepartmentTextView;
    private static final String EMPLOYEE_NAME = "employee_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_content);
        mAccessContentButton = findViewById(R.id.button_access_content);
        mNameEditText = findViewById(R.id.edit_access_name);
        mDepartmentTextView = findViewById(R.id.textview_access_name);
        mAccessContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeeName = mNameEditText.getText().toString();
                Intent intent = new Intent(getApplicationContext(), EmployeeService.class);
                intent.putExtra(EMPLOYEE_NAME, employeeName);
                startService(intent);
                mDepartmentTextView.setText("Fetching Department for: " +employeeName + " ... ");
            }
        });
    }
}