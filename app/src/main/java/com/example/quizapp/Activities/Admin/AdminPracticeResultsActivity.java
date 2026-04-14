package com.example.quizapp.Activities.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Adapter.ResultAdapter;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;

public class AdminPracticeResultsActivity extends AppCompatActivity {

    TextView tvPracticeName, tvSummary, tvEmpty;
    ListView lvResults;

    ResultDAO resultDAO;
    ArrayList<Result> list;

    int practiceId;
    String practiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (practiceId == -1) {
            tvEmpty.setText("Không nhận được dữ liệu bài tập!");
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_practice_results);

        // ===== Ánh xạ =====
        tvPracticeName = findViewById(R.id.tvPracticeName);
        tvSummary      = findViewById(R.id.tvSummary);
        tvEmpty        = findViewById(R.id.tvEmpty);
        lvResults      = findViewById(R.id.lvResults);

        resultDAO = new ResultDAO(this);

        // ===== Nhận dữ liệu từ Intent =====
        practiceId = getIntent().getIntExtra("practiceId", -1);
        practiceName = getIntent().getStringExtra("practiceName");

        // ===== Set tên bài =====
        if (practiceName != null) {
            tvPracticeName.setText("Bài: " + practiceName);
        }

        // ===== Load dữ liệu =====
        loadData();
    }

    private void loadData() {
        list = resultDAO.getResultsWithUser(practiceId);
        if (list == null) list = new ArrayList<>();

        // ===== Gắn adapter =====
        ResultAdapter adapter = new ResultAdapter(this, list);
        lvResults.setAdapter(adapter);

        // ===== Xử lý empty =====
        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }

        // ===== Thống kê =====
        updateSummary();
    }

    private void updateSummary() {
        if (list == null || list.isEmpty()) {
            tvSummary.setText("Chưa có lượt làm nào");
            return;
        }

        int count = list.size();

        double sum = 0;
        int validScoreCount = 0;

        for (Result r : list) {
            if (r.getScore() != null) {
                sum += r.getScore();
                validScoreCount++;
            }
        }

        double avg = validScoreCount > 0 ? sum / validScoreCount : 0;

        tvSummary.setText("Lượt làm: " + count + " | Điểm TB: " + String.format("%.2f", avg));
    }
}