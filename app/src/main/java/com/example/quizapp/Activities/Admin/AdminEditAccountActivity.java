package com.example.quizapp.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;

public class AdminEditAccountActivity extends AppCompatActivity {
    EditText edtFullName, edtEmail, edtPassword, edtRole;
    Button btnUpdate, btnCancel;

    int userId, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_account);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRole = findViewById(R.id.edtRole);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        position = intent.getIntExtra("position", -1);

        edtFullName.setText(intent.getStringExtra("fullName"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtPassword.setText("********");
        edtRole.setText(String.valueOf(intent.getIntExtra("role", 0)));

        btnUpdate.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String roleText = edtRole.getText().toString().trim();

            if (fullName.isEmpty() || roleText.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int role;
            try {
                role = Integer.parseInt(roleText);
            } catch (Exception e) {
                Toast.makeText(this, "Role phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra("userId", userId);
            data.putExtra("position", position);
            data.putExtra("fullName", fullName);
            data.putExtra("role", role);
            setResult(RESULT_OK, data);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}