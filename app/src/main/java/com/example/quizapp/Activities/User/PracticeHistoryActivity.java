package com.example.quizapp.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Adapter.HistoryAdapter;
import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;

public class PracticeHistoryActivity extends AppCompatActivity {

    ListView lvHistory;
    ImageView btnBack;
    ResultDAO resultDAO;
    int userId;

    Spinner spnCategoryFilter;
    CategoryDAO categoryDAO;
    PracticeDAO practiceDAO;
    ArrayList<Result> fullUserHistoryList;
    ArrayList<Category> categoryList;

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
        spnCategoryFilter = findViewById(R.id.spnCategoryFilter);

        userId = getIntent().getIntExtra("userID", -1);
        btnBack.setOnClickListener(v -> finish());

        resultDAO = new ResultDAO(this);
        categoryDAO = new CategoryDAO(this);
        practiceDAO = new PracticeDAO(this);

        ArrayList<Result> allResults = resultDAO.getallresult();
        fullUserHistoryList = new ArrayList<>();
        for (Result r : allResults) {
            if (r.getUserId() == userId) {
                fullUserHistoryList.add(r);
            }
        }

        categoryList = categoryDAO.getallcategory();
        ArrayList<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả môn học"); // Thêm mục mặc định

        for (Category cat : categoryList) {
            categoryNames.add(cat.getCategoryName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spnCategoryFilter.setAdapter(spinnerAdapter);

        spnCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterHistory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterHistory(int spinnerPosition) {
        ArrayList<Result> filteredList = new ArrayList<>();

        if (spinnerPosition == 0) {
            filteredList.addAll(fullUserHistoryList);
        } else {
            int selectedCategoryId = categoryList.get(spinnerPosition - 1).getCategoryId();

            for (Result r : fullUserHistoryList) {
                Practice p = practiceDAO.getpracticebyid(r.getPracticeId());
                if (p != null && p.getCategoryId() == selectedCategoryId) {
                    filteredList.add(r);
                }
            }
        }

        HistoryAdapter adapter = new HistoryAdapter(this, filteredList);
        lvHistory.setAdapter(adapter);

        lvHistory.setOnItemClickListener((parent, view, position, id) -> {
            Result clickedResult = filteredList.get(position);
            Intent intent = new Intent(PracticeHistoryActivity.this, HistoryDetailActivity.class);
            intent.putExtra("resultId", clickedResult.getResultId());
            startActivity(intent);
        });
    }
}