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
    TextView tvDetailTime;

    ResultDetailDAO resultDetailDAO;
    ResultDAO resultDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        tvDetailTotalScore = findViewById(R.id.tvDetailTotalScore);
        lvHistoryDetail = findViewById(R.id.lvHistoryDetail);
        btnBackDetail = findViewById(R.id.btnBackDetail);
        tvDetailTime = findViewById(R.id.tvDetailTime);

        btnBackDetail.setOnClickListener(v -> finish());

        int resultId = getIntent().getIntExtra("resultId", -1);

        resultDAO = new ResultDAO(this);
        resultDetailDAO = new ResultDetailDAO(this);

        Result result = resultDAO.getresultbyid(resultId);
        if (result != null) {
            tvDetailTotalScore.setText("Điểm số: " + String.format("%.1f", result.getScore()));
            int duration = 0;
            if (result.getDuration() != null) {
                duration = result.getDuration();
            }

            int minutes = duration / 60;
            int seconds = duration % 60;
            tvDetailTime.setText(String.format("⏱️ Thời gian làm bài: %02d:%02d", minutes, seconds));
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