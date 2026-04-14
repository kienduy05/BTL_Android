package com.example.quizapp.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Adapter.PracticeAdapter;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.R;

import java.util.ArrayList;

public class PracticeListActivity extends AppCompatActivity {
    ListView lvPractice;
    ImageView btnBack;
    PracticeDAO practiceDAO;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);

        userId = getIntent().getIntExtra("userID", -1);

        lvPractice = findViewById(R.id.lvPractice);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        practiceDAO = new PracticeDAO(this);
        ArrayList<Practice> list = practiceDAO.getallpractice();
        PracticeAdapter adapter = new PracticeAdapter(this, list);
        lvPractice.setAdapter(adapter);

        lvPractice.setOnItemClickListener((parent, view, position, id) -> {
            Practice selected = list.get(position);

            Intent intent = new Intent(PracticeListActivity.this, QuizActivity.class);
            intent.putExtra("userID", userId);
            intent.putExtra("practiceId", selected.getPracticeId());
            intent.putExtra("timeLimit", selected.getTimeLimit());
            startActivity(intent);
        });
    }
}