package com.example.quizapp.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Adapter.PracticeAdapter;
import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.R;

import java.util.ArrayList;

public class PracticeListActivity extends AppCompatActivity {
    ListView lvPractice;
    ImageView btnBack;
    PracticeDAO practiceDAO;
    int userId;
    Spinner spnCategoryFilter;
    CategoryDAO categoryDAO;
    ArrayList<Practice> fullPracticeList;
    ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);

        userId = getIntent().getIntExtra("userID", -1);

        lvPractice = findViewById(R.id.lvPractice);
        btnBack = findViewById(R.id.btnBack);
        spnCategoryFilter = findViewById(R.id.spnCategoryFilterPractice);

        btnBack.setOnClickListener(v -> finish());

        practiceDAO = new PracticeDAO(this);
        categoryDAO = new CategoryDAO(this);
        fullPracticeList = practiceDAO.getallpractice();

        categoryList = categoryDAO.getallcategory();
        ArrayList<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả bài tập");

        for (Category cat : categoryList) {
            categoryNames.add(cat.getCategoryName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spnCategoryFilter.setAdapter(spinnerAdapter);

        spnCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterPracticeList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterPracticeList(int spinnerPosition) {
        ArrayList<Practice> filteredList = new ArrayList<>();

        if (spinnerPosition == 0) {
            filteredList.addAll(fullPracticeList);
        } else {
            int selectedCategoryId = categoryList.get(spinnerPosition - 1).getCategoryId();
            for (Practice p : fullPracticeList) {
                if (p.getCategoryId() == selectedCategoryId) {
                    filteredList.add(p);
                }
            }
        }

        PracticeAdapter adapter = new PracticeAdapter(this, filteredList);
        lvPractice.setAdapter(adapter);

        lvPractice.setOnItemClickListener((parent, view, position, id) -> {
            Practice selected = filteredList.get(position);

            Intent intent = new Intent(PracticeListActivity.this, QuizActivity.class);
            intent.putExtra("userID", userId);
            intent.putExtra("practiceId", selected.getPracticeId());

            int timeLimit = selected.getTimeLimit() != null ? selected.getTimeLimit() : 15;
            intent.putExtra("timeLimit", timeLimit);

            startActivity(intent);
        });
    }
}