package com.example.quizapp.Activities.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.Models.Account;
import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class UserDashboardActivity extends AppCompatActivity {

    Account accountNow;
    AccountDAO accountDAO;
    ResultDAO resultDAO;

    TextView txtUserName;
    TextView txtTotalResultValue;
    TextView txtAverageScoreValue;
    TextView txtCorrectCountValue;
    TextView txtWrongCountValue;
    ImageView imgAva;

    LinearLayout btnPractice;
    LinearLayout btnHistory;
    LinearLayout btnProfile;
    LinearLayout btnLogout;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashboard);

        accountDAO = new AccountDAO(this);
        resultDAO = new ResultDAO(this);

        txtUserName = findViewById(R.id.txtUserName);
        txtTotalResultValue = findViewById(R.id.txtTotalResultValue);
        txtAverageScoreValue = findViewById(R.id.txtAverageScoreValue);
        txtCorrectCountValue = findViewById(R.id.txtCorrectCountValue);
        txtWrongCountValue = findViewById(R.id.txtWrongCountValue);

        btnPractice = findViewById(R.id.btnPractice);
        btnHistory = findViewById(R.id.btnHistory);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        imgAva = findViewById(R.id.imgAva);


        userId = getIntent().getIntExtra("userID", -1);

        loadUserInfo(userId);
        loadDashboardStatistics(userId);


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, ProfileActivity.class);
                intent.putExtra("userID", userId);
                startActivity(intent);
            }
        });

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, PracticeListActivity.class);
                intent.putExtra("userID", userId);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, PracticeHistoryActivity.class);
                intent.putExtra("userID", userId);
                startActivity(intent);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, com.example.quizapp.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo(userId);
        loadDashboardStatistics(userId);
    }

    private void loadUserInfo(int userId) {
        if (userId == -1) {
            txtUserName.setText("Người dùng");
            return;
        }

        accountNow = accountDAO.getAccountByID(userId);

        if (accountNow != null) {
            txtUserName.setText(accountNow.getFullName());
            if(accountNow.getAvatarUrl() != null){
                imgAva.setImageURI(Uri.parse(accountNow.getAvatarUrl()));
            }
        } else {
            txtUserName.setText("Người dùng");
        }

    }

    private void loadDashboardStatistics(int userId) {
        if (userId == -1) {
            txtTotalResultValue.setText("0");
            txtAverageScoreValue.setText("0.0");
            txtCorrectCountValue.setText("0");
            txtWrongCountValue.setText("0");
            return;
        }

        ArrayList<Result> resultList = resultDAO.getallresult();

        int totalResult = 0;
        int totalCorrect = 0;
        int totalWrong = 0;
        double totalScore = 0;

        for (Result item : resultList) {
            if (item.getUserId() == userId) {
                totalResult++;

                if (item.getCorrectCount() != null) {
                    totalCorrect += item.getCorrectCount();
                }

                if (item.getWrongCount() != null) {
                    totalWrong += item.getWrongCount();
                }

                if (item.getScore() != null) {
                    totalScore += item.getScore();
                }
            }
        }

        double averageScore = totalResult > 0 ? totalScore / totalResult : 0;

        txtTotalResultValue.setText(String.valueOf(totalResult));
        txtAverageScoreValue.setText(String.format(Locale.getDefault(), "%.1f", averageScore));
        txtCorrectCountValue.setText(String.valueOf(totalCorrect));
        txtWrongCountValue.setText(String.valueOf(totalWrong));
    }
}