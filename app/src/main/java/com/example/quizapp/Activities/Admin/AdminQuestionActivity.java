package com.example.quizapp.Activities.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Adapter.QuestionAdapter;
import com.example.quizapp.DB.AnswerOptionDAO;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.DB.QuestionDAO;
import com.example.quizapp.Models.AnswerOption;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Question;
import com.example.quizapp.R;

import java.util.ArrayList;

public class AdminQuestionActivity extends AppCompatActivity {

    ListView lvQuestions;
    EditText etSearch;
    Button btnAdd;
    ImageButton btnBack;
    TextView tvEmpty;
    Spinner spinnerPracticeFilter;

    QuestionDAO questionDAO;
    PracticeDAO practiceDAO;
    AnswerOptionDAO answerOptionDAO;
    QuestionAdapter adapter;
    ArrayList<Question> questionList, filteredList;
    ArrayList<Practice> practiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_question);

        // Ánh xạ
        lvQuestions          = findViewById(R.id.lvQuestions);
        etSearch             = findViewById(R.id.etSearch);
        btnAdd               = findViewById(R.id.btnAdd);
        btnBack              = findViewById(R.id.btnBack);
        tvEmpty              = findViewById(R.id.tvEmpty);
        spinnerPracticeFilter = findViewById(R.id.spinnerPracticeFilter);

        questionDAO    = new QuestionDAO(this);
        practiceDAO    = new PracticeDAO(this);
        answerOptionDAO = new AnswerOptionDAO(this);

        setupPracticeFilter();
        loadData();

        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Nút thêm
        btnAdd.setOnClickListener(v -> showDialog(null));

        // Tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Filter theo bài tập
        spinnerPracticeFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Click item → sửa
        lvQuestions.setOnItemClickListener((parent, view, position, id) ->
                showDialog(filteredList.get(position)));

        // Long click → popup menu Sửa/Xóa
        lvQuestions.setOnItemLongClickListener((parent, view, position, id) -> {
            Question selected = filteredList.get(position);
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

    private void setupPracticeFilter() {
        practiceList = practiceDAO.getallpractice();
        ArrayList<String> names = new ArrayList<>();
        names.add("Tất cả bài tập");
        for (Practice p : practiceList) names.add(p.getPracticeName());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPracticeFilter.setAdapter(spinnerAdapter);
    }

    private void loadData() {
        questionList = questionDAO.getallquestion();
        filteredList = new ArrayList<>(questionList);
        adapter = new QuestionAdapter(this, filteredList);
        lvQuestions.setAdapter(adapter);
        updateEmpty();
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        int spinnerPos = spinnerPracticeFilter.getSelectedItemPosition();
        // spinnerPos = 0 → tất cả, spinnerPos > 0 → lọc theo practice
        int selectedPracticeId = spinnerPos == 0 ? -1 : practiceList.get(spinnerPos - 1).getPracticeId();

        filteredList.clear();
        for (Question q : questionList) {
            boolean matchName = q.getContent().toLowerCase().contains(query);
            boolean matchPractice = selectedPracticeId == -1 || q.getPracticeId() == selectedPracticeId;
            if (matchName && matchPractice) {
                filteredList.add(q);
            }
        }
        adapter.notifyDataSetChanged();
        updateEmpty();
    }

    private void updateEmpty() {
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showDialog(Question question) {
        boolean isEdit = question != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Sửa câu hỏi" : "Thêm câu hỏi");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_question, null);
        builder.setView(dialogView);

        Spinner spinnerPractice = dialogView.findViewById(R.id.spinnerPractice);
        EditText etContent      = dialogView.findViewById(R.id.etContent);
        EditText etExplanation  = dialogView.findViewById(R.id.etExplanation);
        EditText etAnswerA      = dialogView.findViewById(R.id.etAnswerA);
        EditText etAnswerB      = dialogView.findViewById(R.id.etAnswerB);
        EditText etAnswerC      = dialogView.findViewById(R.id.etAnswerC);
        EditText etAnswerD      = dialogView.findViewById(R.id.etAnswerD);
        RadioButton rbA         = dialogView.findViewById(R.id.rbA);
        RadioButton rbB         = dialogView.findViewById(R.id.rbB);
        RadioButton rbC         = dialogView.findViewById(R.id.rbC);
        RadioButton rbD         = dialogView.findViewById(R.id.rbD);

        // Chỉ cho chọn 1 radio
        rbA.setOnClickListener(v -> { rbB.setChecked(false); rbC.setChecked(false); rbD.setChecked(false); });
        rbB.setOnClickListener(v -> { rbA.setChecked(false); rbC.setChecked(false); rbD.setChecked(false); });
        rbC.setOnClickListener(v -> { rbA.setChecked(false); rbB.setChecked(false); rbD.setChecked(false); });
        rbD.setOnClickListener(v -> { rbA.setChecked(false); rbB.setChecked(false); rbC.setChecked(false); });

        // Setup Spinner bài tập
        ArrayList<String> practiceNames = new ArrayList<>();
        for (Practice p : practiceList) practiceNames.add(p.getPracticeName());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, practiceNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPractice.setAdapter(spinnerAdapter);

        // Nếu sửa thì điền dữ liệu cũ
        if (isEdit) {
            etContent.setText(question.getContent());
            etExplanation.setText(question.getExplanation());

            // Chọn đúng bài tập trong spinner
            for (int i = 0; i < practiceList.size(); i++) {
                if (practiceList.get(i).getPracticeId() == question.getPracticeId()) {
                    spinnerPractice.setSelection(i);
                    break;
                }
            }

            // Load đáp án cũ
            ArrayList<AnswerOption> options = answerOptionDAO.getansweroptionbyquestionid(question.getQuestionId());
            EditText[] etAnswers = {etAnswerA, etAnswerB, etAnswerC, etAnswerD};
            RadioButton[] rbs    = {rbA, rbB, rbC, rbD};
            for (int i = 0; i < options.size() && i < 4; i++) {
                etAnswers[i].setText(options.get(i).getAnswerText());
                if (options.get(i).getIsCorrect() == 1) {
                    rbs[i].setChecked(true);
                }
            }
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", (dialog, which) -> {
            String content     = etContent.getText().toString().trim();
            String explanation = etExplanation.getText().toString().trim();
            String ansA = etAnswerA.getText().toString().trim();
            String ansB = etAnswerB.getText().toString().trim();
            String ansC = etAnswerC.getText().toString().trim();
            String ansD = etAnswerD.getText().toString().trim();

            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung câu hỏi!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ansA.isEmpty() || ansB.isEmpty() || ansC.isEmpty() || ansD.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ 4 đáp án!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!rbA.isChecked() && !rbB.isChecked() && !rbC.isChecked() && !rbD.isChecked()) {
                Toast.makeText(this, "Vui lòng chọn đáp án đúng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (practiceList.isEmpty()) {
                Toast.makeText(this, "Chưa có bài tập nào, hãy thêm bài tập trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPracticeId = practiceList.get(spinnerPractice.getSelectedItemPosition()).getPracticeId();
            int[] isCorrects = {
                    rbA.isChecked() ? 1 : 0,
                    rbB.isChecked() ? 1 : 0,
                    rbC.isChecked() ? 1 : 0,
                    rbD.isChecked() ? 1 : 0
            };
            String[] answers = {ansA, ansB, ansC, ansD};

            if (isEdit) {
                // Cập nhật câu hỏi
                question.setPracticeId(selectedPracticeId);
                question.setContent(content);
                question.setExplanation(explanation);
                int result = questionDAO.updatequestion(question);

                // Xóa đáp án cũ rồi insert lại
                answerOptionDAO.deletebyquestionid(question.getQuestionId());
                for (int i = 0; i < 4; i++) {
                    AnswerOption opt = new AnswerOption(question.getQuestionId(), answers[i], isCorrects[i], i + 1);
                    answerOptionDAO.insertansweroption(opt);
                }
                Toast.makeText(this,
                        result > 0 ? "Cập nhật thành công!" : "Cập nhật thất bại!",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Thêm câu hỏi mới
                Question newQ = new Question();
                newQ.setPracticeId(selectedPracticeId);
                newQ.setContent(content);
                newQ.setExplanation(explanation);
                newQ.setQuestionOrder(questionList.size() + 1);
                long questionId = questionDAO.insertquestion(newQ);

                // Thêm 4 đáp án
                if (questionId > 0) {
                    for (int i = 0; i < 4; i++) {
                        AnswerOption opt = new AnswerOption((int) questionId, answers[i], isCorrects[i], i + 1);
                        answerOptionDAO.insertansweroption(opt);
                    }
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

    private void showDeleteDialog(Question question) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa câu hỏi này không?\nCác đáp án liên quan cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa đáp án trước
                    answerOptionDAO.deletebyquestionid(question.getQuestionId());
                    // Rồi xóa câu hỏi
                    int result = questionDAO.deletequestion(question.getQuestionId());
                    Toast.makeText(this,
                            result > 0 ? "Xóa thành công!" : "Xóa thất bại!",
                            Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}