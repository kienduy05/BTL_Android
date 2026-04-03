package com.example.quizapp.Activities.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.Models.Account;
import com.example.quizapp.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgAvatar;
    TextView txtCreatedAt;
    EditText edtFullName;
    EditText edtEmail;
    EditText edtNewPassword;
    EditText edtConfirmPassword;
    Button btnUpdateProfile;

    AccountDAO accountDAO;
    Account currentAccount;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        accountDAO = new AccountDAO(this);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtCreatedAt = findViewById(R.id.txtCreatedAt);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        userId = getIntent().getIntExtra("userID", -1);

        loadProfile(userId);

        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadProfile(int userId) {
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentAccount = accountDAO.getAccountByID(userId);

        if (currentAccount == null) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtFullName.setText(currentAccount.getFullName());
        edtEmail.setText(currentAccount.getEmail());

        if (currentAccount.getCreatedAt() != null) {
            txtCreatedAt.setText("Ngày tạo tài khoản: " + currentAccount.getCreatedAt());
        } else {
            txtCreatedAt.setText("Ngày tạo tài khoản: Không có dữ liệu");
        }
    }

    private void updateProfile() {
        String fullName = edtFullName.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            edtFullName.setError("Vui lòng nhập họ tên");
            edtFullName.requestFocus();
            return;
        }

        String passwordToSave = currentAccount.getPassword();

        if (!TextUtils.isEmpty(newPassword)) {
            if (newPassword.length() < 6) {
                edtNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                edtNewPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                edtConfirmPassword.setError("Vui lòng xác nhận mật khẩu mới");
                edtConfirmPassword.requestFocus();
                return;
            }

            if (confirmPassword.length() < 6) {
                edtConfirmPassword.setError("Mật khẩu xác nhận phải có ít nhất 6 ký tự");
                edtConfirmPassword.requestFocus();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                edtConfirmPassword.setError("Xác nhận mật khẩu không khớp");
                edtConfirmPassword.requestFocus();
                return;
            }

            passwordToSave = newPassword;
        }

        currentAccount.setFullName(fullName);
        currentAccount.setPassword(passwordToSave);

        int result = accountDAO.updateAccount(currentAccount);

        if (result > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            edtNewPassword.setText("");
            edtConfirmPassword.setText("");
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }}