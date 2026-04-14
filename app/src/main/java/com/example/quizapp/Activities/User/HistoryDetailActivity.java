package com.example.quizapp.Activities.User;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Adapter.HistoryDetailAdapter;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.DB.ResultDetailDAO;
import com.example.quizapp.Models.Result;
import com.example.quizapp.Models.ResultDetail;
import com.example.quizapp.R;

import java.util.ArrayList;

public class HistoryDetailActivity extends AppCompatActivity {

    TextView tvDetailTotalScore;
    ListView lvHistoryDetail;
    ImageView btnBackDetail;

    ResultDetailDAO resultDetailDAO;
    ResultDAO resultDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        tvDetailTotalScore = findViewById(R.id.tvDetailTotalScore);
        lvHistoryDetail = findViewById(R.id.lvHistoryDetail);
        btnBackDetail = findViewById(R.id.btnBackDetail);

        btnBackDetail.setOnClickListener(v -> finish());

        int resultId = getIntent().getIntExtra("resultId", -1);

        resultDAO = new ResultDAO(this);
        resultDetailDAO = new ResultDetailDAO(this);

        Result result = resultDAO.getresultbyid(resultId);
        if (result != null) {
            tvDetailTotalScore.setText("Điểm số: " + String.format("%.1f", result.getScore()));
        }

        ArrayList<ResultDetail> allDetails = resultDetailDAO.getallresultdetail();
        ArrayList<ResultDetail> filteredDetails = new ArrayList<>();

        for (ResultDetail rd : allDetails) {
            if (rd.getResultId() == resultId) {
                filteredDetails.add(rd);
            }
        }

        HistoryDetailAdapter adapter = new HistoryDetailAdapter(this, filteredDetails);
        lvHistoryDetail.setAdapter(adapter);
    }
}