package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Activities.Admin.AdminDashboardActivity;
import com.example.quizapp.Activities.ForgotPasswordActivity;
import com.example.quizapp.Activities.RegisterActivity;
import com.example.quizapp.Activities.User.UserDashboardActivity;
import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.Models.Account;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText edtEmail, edtPassword;
    TextView txtRegisterNow,txtForgotPassword;
    Button btnLogin;

    AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Anh xa
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterNow = findViewById(R.id.txtRegisterNow);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);


        accountDAO = new AccountDAO(this);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Account acc = accountDAO.getAccountByEmailPassword(email, password);

            if (acc != null) {
                if(acc.getRole()==0){
                    // LOGIN SUCCESS
                    Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                    intent.putExtra("userID", acc.getUserId());
                    startActivity(intent);
                    finish();
                }
                else{
                    // LOGIN SUCCESS
                    Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    intent.putExtra("userID", acc.getUserId());
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
        txtRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}