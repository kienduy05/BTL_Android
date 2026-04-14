package com.example.quizapp.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Adapter.HistoryAdapter;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;

public class PracticeHistoryActivity extends AppCompatActivity {

    ListView lvHistory;
    ImageView btnBack;
    ResultDAO resultDAO;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvHistory = findViewById(R.id.lvHistory);
        btnBack = findViewById(R.id.btnBack);

        userId = getIntent().getIntExtra("userID", -1);

        btnBack.setOnClickListener(v -> finish());

        resultDAO = new ResultDAO(this);
        ArrayList<Result> allResults = resultDAO.getallresult();
        ArrayList<Result> userHistoryList = new ArrayList<>();

        for (Result r : allResults) {
            if (r.getUserId() == userId) {
                userHistoryList.add(r);
            }
        }

        HistoryAdapter adapter = new HistoryAdapter(this, userHistoryList);
        lvHistory.setAdapter(adapter);

        lvHistory.setOnItemClickListener((parent, view, position, id) -> {
            Result clickedResult = userHistoryList.get(position);
            Intent intent = new Intent(PracticeHistoryActivity.this, HistoryDetailActivity.class);
            intent.putExtra("resultId", clickedResult.getResultId());
            startActivity(intent);
        });
    }
}