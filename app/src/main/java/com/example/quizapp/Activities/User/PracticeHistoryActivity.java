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

    // Các biến dùng cho tính năng Lọc
    Spinner spnCategoryFilter;
    CategoryDAO categoryDAO;
    PracticeDAO practiceDAO;
    ArrayList<Result> fullUserHistoryList; // Chứa toàn bộ lịch sử gốc
    ArrayList<Category> categoryList; // Chứa danh sách môn học

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

        // 1. Ánh xạ View cơ bản
        lvHistory = findViewById(R.id.lvHistory);
        btnBack = findViewById(R.id.btnBack);
        spnCategoryFilter = findViewById(R.id.spnCategoryFilter);

        // 2. Nhận userID và xử lý nút Back
        userId = getIntent().getIntExtra("userID", -1);
        btnBack.setOnClickListener(v -> finish());

        // 3. Khởi tạo Database và lấy danh sách lịch sử của User
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

        // 4. Lấy danh sách môn học và đổ vào Spinner
        categoryList = categoryDAO.getallcategory();
        ArrayList<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả môn học"); // Thêm mục mặc định

        for (Category cat : categoryList) {
            categoryNames.add(cat.getCategoryName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spnCategoryFilter.setAdapter(spinnerAdapter);

        // 5. Lắng nghe sự kiện khi chọn 1 môn học trong Spinner
        spnCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Gọi hàm lọc dữ liệu bên dưới
                filterHistory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // === HÀM XỬ LÝ LỌC LỊCH SỬ ===
    private void filterHistory(int spinnerPosition) {
        ArrayList<Result> filteredList = new ArrayList<>();

        if (spinnerPosition == 0) {
            // Nếu chọn "Tất cả môn học" -> Hiện toàn bộ danh sách gốc
            filteredList.addAll(fullUserHistoryList);
        } else {
            // Nếu chọn môn cụ thể -> Lấy ID của môn đó để lọc
            // Lùi 1 index vì Spinner có mục "Tất cả" ở đầu tiên
            int selectedCategoryId = categoryList.get(spinnerPosition - 1).getCategoryId();

            for (Result r : fullUserHistoryList) {
                Practice p = practiceDAO.getpracticebyid(r.getPracticeId());
                if (p != null && p.getCategoryId() == selectedCategoryId) {
                    filteredList.add(r);
                }
            }
        }

        // Đẩy danh sách đã lọc lên ListView
        HistoryAdapter adapter = new HistoryAdapter(this, filteredList);
        lvHistory.setAdapter(adapter);

        // Setup lại sự kiện click để mở chi tiết bài làm
        lvHistory.setOnItemClickListener((parent, view, position, id) -> {
            Result clickedResult = filteredList.get(position);
            Intent intent = new Intent(PracticeHistoryActivity.this, HistoryDetailActivity.class);
            intent.putExtra("resultId", clickedResult.getResultId());
            startActivity(intent);
        });
    }
}