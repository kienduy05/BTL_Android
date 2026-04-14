package com.example.quizapp.Activities.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Adapter.PracticeAdapter;
import com.example.quizapp.Adapter.QuestionCheckboxAdapter;
import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.DB.QuestionDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Question;
import com.example.quizapp.R;

import java.util.ArrayList;
import java.util.HashSet;

public class AdminPracticeActivity extends AppCompatActivity {

    ListView lvPractices;
    EditText etSearch;
    Button btnAdd;
    ImageButton btnBack;
    TextView tvEmpty;

    PracticeDAO practiceDAO;
    CategoryDAO categoryDAO;
    QuestionDAO questionDAO;
    PracticeAdapter adapter;
    ArrayList<Practice> practiceList, filteredList;
    ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_practice);

        // Ánh xạ
        lvPractices = findViewById(R.id.lvPractices);
        etSearch    = findViewById(R.id.etSearch);
        btnAdd      = findViewById(R.id.btnAdd);
        btnBack     = findViewById(R.id.btnBack);
        tvEmpty     = findViewById(R.id.tvEmpty);

        practiceDAO = new PracticeDAO(this);
        categoryDAO = new CategoryDAO(this);
        questionDAO = new QuestionDAO(this);

        loadData();

        btnBack.setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> showDialog(null));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        lvPractices.setOnItemClickListener((parent, view, position, id) ->
                showDialog(filteredList.get(position)));

        lvPractices.setOnItemLongClickListener((parent, view, position, id) -> {
            Practice selected = filteredList.get(position);
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu1, popup.getMenu());
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
        practiceList = practiceDAO.getallpractice();
        categoryList = categoryDAO.getallcategory();
        filteredList = new ArrayList<>(practiceList);
        adapter = new PracticeAdapter(this, filteredList);
        lvPractices.setAdapter(adapter);
        updateEmpty();
    }

    private void filterList(String query) {
        filteredList.clear();
        for (Practice p : practiceList) {
            if (p.getPracticeName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        updateEmpty();
    }

    private void updateEmpty() {
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showDialog(Practice practice) {
        boolean isEdit = practice != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Sửa bài tập" : "Thêm bài tập");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_practice, null);
        builder.setView(dialogView);

        Spinner spinnerCategory   = dialogView.findViewById(R.id.spinnerCategory);
        EditText etName           = dialogView.findViewById(R.id.etPracticeName);
        EditText etDesc           = dialogView.findViewById(R.id.etPracticeDescription);
        EditText etTimeLimit      = dialogView.findViewById(R.id.etTimeLimit);
        EditText etTotalQuestions = dialogView.findViewById(R.id.etTotalQuestions);
        ListView lvQuestionSelect = dialogView.findViewById(R.id.lvQuestionSelect);
        TextView tvNoQuestion     = dialogView.findViewById(R.id.tvNoQuestion);
        TextView tvSelectedCount  = dialogView.findViewById(R.id.tvSelectedCount);

        // HashSet lưu questionId được chọn
        HashSet<Integer> selectedQuestionIds = new HashSet<>();

        // Setup Spinner danh mục
        ArrayList<String> catNames = new ArrayList<>();
        catNames.add("-- Chọn danh mục --");
        for (Category c : categoryList) catNames.add(c.getCategoryName());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, catNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Khi chọn danh mục → load câu hỏi
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    lvQuestionSelect.setVisibility(View.GONE);
                    tvNoQuestion.setVisibility(View.VISIBLE);
                    tvNoQuestion.setText("Chọn danh mục để xem câu hỏi");
                    return;
                }

                int selectedCatId = categoryList.get(position - 1).getCategoryId();
                ArrayList<Question> questionsByCat = questionDAO.getquestionbycategoryid(selectedCatId);

                if (questionsByCat.isEmpty()) {
                    lvQuestionSelect.setVisibility(View.GONE);
                    tvNoQuestion.setVisibility(View.VISIBLE);
                    tvNoQuestion.setText("Danh mục này chưa có câu hỏi nào");
                } else {
                    tvNoQuestion.setVisibility(View.GONE);
                    lvQuestionSelect.setVisibility(View.VISIBLE);

                    QuestionCheckboxAdapter cbAdapter = new QuestionCheckboxAdapter(
                            AdminPracticeActivity.this, questionsByCat, selectedQuestionIds);

                    cbAdapter.setOnSelectionChangedListener(count -> {
                        tvSelectedCount.setText("Đã chọn: " + count);
                        etTotalQuestions.setText(String.valueOf(count));
                    });

                    lvQuestionSelect.setAdapter(cbAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Nếu sửa thì điền dữ liệu cũ
        if (isEdit) {
            etName.setText(practice.getPracticeName());
            etDesc.setText(practice.getPracticeDescription());
            if (practice.getTimeLimit() != null)
                etTimeLimit.setText(String.valueOf(practice.getTimeLimit()));
            if (practice.getTotalQuestions() != null)
                etTotalQuestions.setText(String.valueOf(practice.getTotalQuestions()));

            // Chọn đúng danh mục trong spinner (+1 vì có "-- Chọn danh mục --")
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getCategoryId() == practice.getCategoryId()) {
                    spinnerCategory.setSelection(i + 1);
                    break;
                }
            }

            // Load câu hỏi hiện tại của bài tập vào selectedIds
            ArrayList<Question> currentQuestions = questionDAO.getquestionbypracticeid(practice.getPracticeId());
            for (Question q : currentQuestions) {
                selectedQuestionIds.add(q.getQuestionId());
            }
            tvSelectedCount.setText("Đã chọn: " + selectedQuestionIds.size());
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", (dialog, which) -> {
            String name     = etName.getText().toString().trim();
            String desc     = etDesc.getText().toString().trim();
            String timeStr  = etTimeLimit.getText().toString().trim();
            String totalStr = etTotalQuestions.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên bài tập!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spinnerCategory.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Vui lòng chọn danh mục!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedCatId = categoryList.get(spinnerCategory.getSelectedItemPosition() - 1).getCategoryId();
            Integer timeLimit = timeStr.isEmpty() ? null : Integer.parseInt(timeStr);
            Integer totalQ    = selectedQuestionIds.size() > 0
                    ? selectedQuestionIds.size()
                    : (totalStr.isEmpty() ? null : Integer.parseInt(totalStr));

            if (isEdit) {
                practice.setCategoryId(selectedCatId);
                practice.setPracticeName(name);
                practice.setPracticeDescription(desc);
                practice.setTimeLimit(timeLimit);
                practice.setTotalQuestions(totalQ);
                int result = practiceDAO.updatepractice(practice);
                updateSelectedQuestions(practice.getPracticeId(), selectedQuestionIds);
                Toast.makeText(this,
                        result > 0 ? "Cập nhật thành công!" : "Cập nhật thất bại!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Practice newItem = new Practice();
                newItem.setCategoryId(selectedCatId);
                newItem.setPracticeName(name);
                newItem.setPracticeDescription(desc);
                newItem.setTimeLimit(timeLimit);
                newItem.setTotalQuestions(totalQ);
                newItem.setCreatedBy(1);
                long newPracticeId = practiceDAO.insertpractice(newItem);
                if (newPracticeId > 0) {
                    updateSelectedQuestions((int) newPracticeId, selectedQuestionIds);
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void updateSelectedQuestions(int practiceId, HashSet<Integer> selectedIds) {
        for (int questionId : selectedIds) {
            questionDAO.updatepracticeid(questionId, practiceId);
        }
    }

    private void showDeleteDialog(Practice practice) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa bài tập \"" + practice.getPracticeName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = practiceDAO.deletepractice(practice.getPracticeId());
                    Toast.makeText(this,
                            result > 0 ? "Xóa thành công!" : "Xóa thất bại!",
                            Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}