package com.example.quizapp.Activities.User;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.DB.AnswerOptionDAO;
import com.example.quizapp.DB.QuestionDAO;
import com.example.quizapp.DB.ResultDAO;
import com.example.quizapp.DB.ResultDetailDAO;
import com.example.quizapp.Models.AnswerOption;
import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.Result;
import com.example.quizapp.Models.ResultDetail;
import com.example.quizapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    TextView txtQuestionIndex, txtCountDown, txtQuestionContent;
    RadioGroup radioGroupAnswers;
    RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    Button btnPrev, btnNext, btnSubmit;

    int userId, practiceId, timeLimit;
    ArrayList<Question> questionsList;
    HashMap<Integer, ArrayList<AnswerOption>> optionsMap = new HashMap<>();
    HashMap<Integer, AnswerOption> userAnswers = new HashMap<>();

    int currentIndex = 0;
    private long startTimeInMillis;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
        loadDataFromDatabase();
        startTimer();
        displayQuestion();
        startTimeInMillis = System.currentTimeMillis();
    }

    private void initViews() {
        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtCountDown = findViewById(R.id.txtCountDown);
        txtQuestionContent = findViewById(R.id.txtQuestionContent);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);

        radioGroupAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            ArrayList<AnswerOption> currentOptions = optionsMap.get(currentIndex);
            if (currentOptions == null) return;

            if (checkedId == R.id.rbOption1 && currentOptions.size() > 0) userAnswers.put(currentIndex, currentOptions.get(0));
            else if (checkedId == R.id.rbOption2 && currentOptions.size() > 1) userAnswers.put(currentIndex, currentOptions.get(1));
            else if (checkedId == R.id.rbOption3 && currentOptions.size() > 2) userAnswers.put(currentIndex, currentOptions.get(2));
            else if (checkedId == R.id.rbOption4 && currentOptions.size() > 3) userAnswers.put(currentIndex, currentOptions.get(3));
        });

        btnNext.setOnClickListener(v -> { currentIndex++; displayQuestion(); });
        btnPrev.setOnClickListener(v -> { currentIndex--; displayQuestion(); });
        btnSubmit.setOnClickListener(v -> submitQuiz());
    }

    private void loadDataFromDatabase() {
        userId = getIntent().getIntExtra("userID", -1);
        practiceId = getIntent().getIntExtra("practiceId", -1);
        timeLimit = getIntent().getIntExtra("timeLimit", 15);

        QuestionDAO qDao = new QuestionDAO(this);
        AnswerOptionDAO aDao = new AnswerOptionDAO(this);

        questionsList = qDao.getquestionbypracticeid(practiceId);

        for (int i = 0; i < questionsList.size(); i++) {
            optionsMap.put(i, aDao.getansweroptionbyquestionid(questionsList.get(i).getQuestionId()));
        }
    }

    private void displayQuestion() {
        if (questionsList == null || questionsList.isEmpty()) {
            Toast.makeText(this, "Chưa có câu hỏi cho bài tập này!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtQuestionIndex.setText("Câu " + (currentIndex + 1) + "/" + questionsList.size());
        txtQuestionContent.setText(questionsList.get(currentIndex).getContent());

        radioGroupAnswers.setOnCheckedChangeListener(null);
        radioGroupAnswers.clearCheck();

        ArrayList<AnswerOption> ops = optionsMap.get(currentIndex);
        rbOption1.setVisibility(View.GONE); rbOption2.setVisibility(View.GONE);
        rbOption3.setVisibility(View.GONE); rbOption4.setVisibility(View.GONE);

        if (ops != null) {
            if (ops.size() > 0) { rbOption1.setVisibility(View.VISIBLE); rbOption1.setText(ops.get(0).getAnswerText()); }
            if (ops.size() > 1) { rbOption2.setVisibility(View.VISIBLE); rbOption2.setText(ops.get(1).getAnswerText()); }
            if (ops.size() > 2) { rbOption3.setVisibility(View.VISIBLE); rbOption3.setText(ops.get(2).getAnswerText()); }
            if (ops.size() > 3) { rbOption4.setVisibility(View.VISIBLE); rbOption4.setText(ops.get(3).getAnswerText()); }
        }

        AnswerOption selectedAns = userAnswers.get(currentIndex);
        if (selectedAns != null && ops != null) {
            if (ops.size() > 0 && selectedAns.getAnswerOptionId() == ops.get(0).getAnswerOptionId()) rbOption1.setChecked(true);
            else if (ops.size() > 1 && selectedAns.getAnswerOptionId() == ops.get(1).getAnswerOptionId()) rbOption2.setChecked(true);
            else if (ops.size() > 2 && selectedAns.getAnswerOptionId() == ops.get(2).getAnswerOptionId()) rbOption3.setChecked(true);
            else if (ops.size() > 3 && selectedAns.getAnswerOptionId() == ops.get(3).getAnswerOptionId()) rbOption4.setChecked(true);
        }

        radioGroupAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            if (ops == null) return;
            if (checkedId == R.id.rbOption1 && ops.size() > 0) userAnswers.put(currentIndex, ops.get(0));
            else if (checkedId == R.id.rbOption2 && ops.size() > 1) userAnswers.put(currentIndex, ops.get(1));
            else if (checkedId == R.id.rbOption3 && ops.size() > 2) userAnswers.put(currentIndex, ops.get(2));
            else if (checkedId == R.id.rbOption4 && ops.size() > 3) userAnswers.put(currentIndex, ops.get(3));
        });

        btnPrev.setEnabled(currentIndex > 0);

        if (currentIndex == questionsList.size() - 1) {
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLimit * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int mins = (int) (millisUntilFinished / 60000);
                int secs = (int) (millisUntilFinished % 60000 / 1000);
                txtCountDown.setText(String.format("%02d:%02d", mins, secs));
            }
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Đã hết thời gian làm bài!", Toast.LENGTH_SHORT).show();
                submitQuiz();
            }
        }.start();
    }

    private void submitQuiz() {
        long endTime = System.currentTimeMillis();
        if (timer != null) timer.cancel();

        int durationInSeconds = (int) ((endTime - startTimeInMillis) / 1000);

        int correctCount = 0;
        int totalQs = questionsList.size();

        for (int i = 0; i < totalQs; i++) {
            AnswerOption ans = userAnswers.get(i);
            if (ans != null && ans.getIsCorrect() == 1) {
                correctCount++;
            }
        }
        double score = totalQs > 0 ? ((double) correctCount / totalQs) * 10 : 0;

        Result result = new Result();
        result.setUserId(userId);
        result.setPracticeId(practiceId);
        result.setScore(score);
        result.setCorrectCount(correctCount);
        result.setWrongCount(totalQs - correctCount);
        result.setTotalQuestions(totalQs);

        result.setDuration(durationInSeconds);

        ResultDAO rDao = new ResultDAO(this);

        long newResultId = rDao.insertresult(result);

        String timeDisplay = durationInSeconds + " giây";
        if (durationInSeconds >= 60) {
            timeDisplay = (durationInSeconds / 60) + " phút " + (durationInSeconds % 60) + " giây";
        }

        Toast.makeText(this, "Hoàn thành! Bạn đạt " + String.format("%.1f", score) +
                " điểm.\nThời gian: " + timeDisplay, Toast.LENGTH_LONG).show();

        finish();
    }
}