package com.example.quizapp.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.MainActivity;
import com.example.quizapp.Models.Account;
import com.example.quizapp.R;

public class AdminDashboardActivity extends AppCompatActivity {

    TextView txtName;
    LinearLayout btnManageCategory, btnManagePractice, btnManageQuestion, btnManageAccount, btnLogout;

    AccountDAO accountDAO;
    Account accountNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        // Ánh xạ
        txtName           = findViewById(R.id.txtName);
        btnManageCategory = findViewById(R.id.btnManageCategory);
        btnManagePractice = findViewById(R.id.btnManagePractice);
        btnManageQuestion = findViewById(R.id.btnManageQuestion);
        btnManageAccount  = findViewById(R.id.btnManageAccount);
        btnLogout         = findViewById(R.id.btnLogout);

        // Lấy thông tin admin
        accountDAO = new AccountDAO(this);
        int userId = getIntent().getIntExtra("userID", -1);
        accountNow = accountDAO.getAccountByID(userId);
        if (accountNow != null) {
            txtName.setText(accountNow.getFullName());
        }

        // Quản lý danh mục
        btnManageCategory.setOnClickListener(v ->
                startActivity(new Intent(this, AdminCategoryActivity.class)));

        // Quản lý bài tập
        btnManagePractice.setOnClickListener(v ->
                startActivity(new Intent(this, AdminPracticeActivity.class)));

        // Quản lý câu hỏi
        btnManageQuestion.setOnClickListener(v ->
                startActivity(new Intent(this, AdminQuestionActivity.class)));

        // Quản lý tài khoản (placeholder)
        btnManageAccount.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAccountActivity.class)));

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}