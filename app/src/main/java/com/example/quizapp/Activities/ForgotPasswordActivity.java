package com.example.quizapp.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText edtEmailForgot, edtVerifyCode, edtNewPassword, edtConfirmNewPassword;
    private Button btnGetCode, btnChangePassword;
    private TextView txtBackLogin;

    private AccountDAO accountDAO;

    private String generatedCode = "";
    private boolean isCodeGenerated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        accountDAO = new AccountDAO(this);

        btnGetCode.setOnClickListener(v -> handleGetCode());

        btnChangePassword.setOnClickListener(v -> handleChangePassword());

        txtBackLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtVerifyCode = findViewById(R.id.edtVerifyCode);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnGetCode = findViewById(R.id.btnGetCode);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        txtBackLogin = findViewById(R.id.txtBackLogin);
    }

    private void handleGetCode() {
        String email = edtEmailForgot.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmailForgot.setError("Vui lòng nhập email");
            edtEmailForgot.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailForgot.setError("Email không hợp lệ");
            edtEmailForgot.requestFocus();
            return;
        }

        if (!accountDAO.isEmailExists(email)) {
            Toast.makeText(this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
            return;
        }

        generatedCode = generateRandomCode();
        isCodeGenerated = true;

        new AlertDialog.Builder(this)
                .setTitle("Mã xác nhận")
                .setMessage("Mã xác nhận của bạn là: " + generatedCode)
                .setPositiveButton("OK", null)
                .show();
    }

    private void handleChangePassword() {
        String email = edtEmailForgot.getText().toString().trim();
        String inputCode = edtVerifyCode.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmailForgot.setError("Vui lòng nhập email");
            edtEmailForgot.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailForgot.setError("Email không hợp lệ");
            edtEmailForgot.requestFocus();
            return;
        }

        if (!accountDAO.isEmailExists(email)) {
            Toast.makeText(this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isCodeGenerated) {
            Toast.makeText(this, "Vui lòng lấy mã xác nhận trước", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(inputCode)) {
            edtVerifyCode.setError("Vui lòng nhập mã xác nhận");
            edtVerifyCode.requestFocus();
            return;
        }

        if (!inputCode.equals(generatedCode)) {
            edtVerifyCode.setError("Mã xác nhận không đúng");
            edtVerifyCode.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            edtNewPassword.setError("Vui lòng nhập mật khẩu mới");
            edtNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            edtNewPassword.setError("Mật khẩu phải từ 6 ký tự");
            edtNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmNewPassword.setError("Vui lòng xác nhận mật khẩu mới");
            edtConfirmNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmNewPassword.setError("Xác nhận mật khẩu không khớp");
            edtConfirmNewPassword.requestFocus();
            return;
        }

        int result = accountDAO.updatePasswordByEmail(email, newPassword);

        if (result > 0) {
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

            generatedCode = "";
            isCodeGenerated = false;

            edtVerifyCode.setText("");
            edtNewPassword.setText("");
            edtConfirmNewPassword.setText("");

            finish();
        } else {
            Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6 số
        return String.valueOf(code);
    }
}