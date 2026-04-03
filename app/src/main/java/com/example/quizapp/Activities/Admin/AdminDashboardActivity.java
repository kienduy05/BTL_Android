package com.example.quizapp.Activities.Admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.Models.Account;
import com.example.quizapp.R;

public class AdminDashboardActivity extends AppCompatActivity {
    TextView txtName;
    AccountDAO accountDAO = new AccountDAO(this);
    Account AccountNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        //Anh xa
        txtName = findViewById(R.id.txtName);
        //

        int userId = getIntent().getIntExtra("userID", -1);
        AccountNow = accountDAO.getAccountByID(userId);
        txtName.setText(AccountNow.getFullName());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}