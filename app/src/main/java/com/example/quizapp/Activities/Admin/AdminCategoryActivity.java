package com.example.quizapp.Activities.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Adapter.CategoryAdapter;
import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.R;

import java.util.ArrayList;

public class AdminCategoryActivity extends AppCompatActivity {

    ListView lvCategories;
    EditText etSearch;
    Button btnAdd;
    ImageButton btnBack;
    TextView tvEmpty;

    CategoryDAO categoryDAO;
    CategoryAdapter adapter;
    ArrayList<Category> categoryList, filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_category);

        // Ánh xạ
        lvCategories = findViewById(R.id.lvCategories);
        etSearch     = findViewById(R.id.etSearch);
        btnAdd       = findViewById(R.id.btnAdd);
        btnBack      = findViewById(R.id.btnBack);
        tvEmpty      = findViewById(R.id.tvEmpty);

        categoryDAO = new CategoryDAO(this);

        loadData();

        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Nút thêm
        btnAdd.setOnClickListener(v -> showDialog(null));

        // Tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Click item → sửa
        lvCategories.setOnItemClickListener((parent, view, position, id) ->
                showDialog(filteredList.get(position)));

        // Long click → popup menu Sửa/Xóa
        lvCategories.setOnItemLongClickListener((parent, view, position, id) -> {
            Category selected = filteredList.get(position);
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu2, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menuEdit) {
                    showDialog(selected);
                } else if (menuItem.getItemId() == R.id.menuDelete) {
                    showDeleteDialog(selected);
                }
                return true;
            });
            popup.show();
            return true;
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
        loadData();
    }

    private void loadData() {
        categoryList = categoryDAO.getallcategory();
        filteredList = new ArrayList<>(categoryList);
        adapter = new CategoryAdapter(this, filteredList);
        lvCategories.setAdapter(adapter);
        updateEmpty();
    }

    private void filterList(String query) {
        filteredList.clear();
        for (Category c : categoryList) {
            if (c.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(c);
            }
        }
        adapter.notifyDataSetChanged();
        updateEmpty();
    }

    private void updateEmpty() {
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showDialog(Category category) {
        boolean isEdit = category != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Sửa danh mục" : "Thêm danh mục");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText etName  = dialogView.findViewById(R.id.etCategoryName);
        EditText etDesc  = dialogView.findViewById(R.id.etCategoryDescription);
        EditText etImage = dialogView.findViewById(R.id.etCategoryImage);

        if (isEdit) {
            etName.setText(category.getCategoryName());
            etDesc.setText(category.getCategoryDescription());
            etImage.setText(category.getImageUrl());
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", (dialog, which) -> {
            String name  = etName.getText().toString().trim();
            String desc  = etDesc.getText().toString().trim();
            String image = etImage.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                category.setCategoryName(name);
                category.setCategoryDescription(desc);
                category.setImageUrl(image);
                int result = categoryDAO.updatecategory(category);
                Toast.makeText(this,
                        result > 0 ? "Cập nhật thành công!" : "Cập nhật thất bại!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Category newItem = new Category(name, desc, image);
                long result = categoryDAO.insertcategory(newItem);
                Toast.makeText(this,
                        result > 0 ? "Thêm thành công!" : "Thêm thất bại!",
                        Toast.LENGTH_SHORT).show();
            }
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showDeleteDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa danh mục \"" + category.getCategoryName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = categoryDAO.deletecategory(category.getCategoryId());
                    Toast.makeText(this,
                            result > 0 ? "Xóa thành công!" : "Xóa thất bại!",
                            Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}