package com.example.quizapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppQuizDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "QuizAppDB";
    public static final int DB_VERSION = 2;

    public AppQuizDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CATEGORY
        db.execSQL("CREATE TABLE Category (" +
                "categoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoryName TEXT NOT NULL, " +
                "categoryDescription TEXT, " +
                "imageUrl TEXT)");

        // ACCOUNT
        db.execSQL("CREATE TABLE Account (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullName TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role INTEGER NOT NULL, " +
                "avatarUrl TEXT, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // PRACTICE
        db.execSQL("CREATE TABLE Practice (" +
                "practiceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoryId INTEGER NOT NULL, " +
                "practiceName TEXT NOT NULL, " +
                "practiceDescription TEXT, " +
                "timeLimit INTEGER, " +
                "totalQuestions INTEGER, " +
                "createdBy INTEGER, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // QUESTION
        db.execSQL("CREATE TABLE Question (" +
                "questionId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "practiceId INTEGER NOT NULL, " +
                "content TEXT NOT NULL, " +
                "imageUrl TEXT, " +
                "explanation TEXT, " +
                "questionOrder INTEGER)");

        // ANSWER OPTION
        db.execSQL("CREATE TABLE AnswerOption (" +
                "answerOptionId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "questionId INTEGER NOT NULL, " +
                "answerText TEXT NOT NULL, " +
                "isCorrect INTEGER NOT NULL, " +
                "optionOrder INTEGER)");

        // RESULT
        db.execSQL("CREATE TABLE Result (" +
                "resultId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "practiceId INTEGER NOT NULL, " +
                "score REAL, " +
                "correctCount INTEGER, " +
                "wrongCount INTEGER, " +
                "totalQuestions INTEGER, " +
                "duration INTEGER, " +
                "submittedAt DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // RESULT DETAIL
        db.execSQL("CREATE TABLE ResultDetail (" +
                "resultDetailId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "resultId INTEGER NOT NULL, " +
                "questionId INTEGER NOT NULL, " +
                "selectedAnswerOptionId INTEGER, " +
                "isCorrect INTEGER NOT NULL)");

        // =========================
        // INSERT SAMPLE ACCOUNT
        // =========================
        db.execSQL("INSERT INTO Account (fullName, email, password, role, avatarUrl) VALUES " +
                "('Root Admin', 'root@admin.com', '123456', 1, NULL)");

        db.execSQL("INSERT INTO Account (fullName, email, password, role, avatarUrl) VALUES " +
                "('Nguyen Van A', 'user1@gmail.com', '123456', 0, NULL)");

        // =========================
        // INSERT SAMPLE CATEGORY
        // =========================
        db.execSQL("INSERT INTO Category (categoryName, categoryDescription, imageUrl) VALUES (" +
                "'Toan hoc', " +
                "'Mon hoc luyen tu duy va tinh toan', " +
                "NULL)");

        // =========================
        // INSERT SAMPLE PRACTICE
        // categoryId = 1
        // createdBy = 1 (Root Admin)
        // =========================
        db.execSQL("INSERT INTO Practice (categoryId, practiceName, practiceDescription, timeLimit, totalQuestions, createdBy) VALUES (" +
                "1, " +
                "'Kiem tra Toan co ban', " +
                "'Bai kiem tra mau gom 1 cau hoi', " +
                "15, " +
                "1, " +
                "1)");

        // =========================
        // INSERT SAMPLE QUESTION
        // practiceId = 1
        // =========================
        db.execSQL("INSERT INTO Question (practiceId, content, imageUrl, explanation, questionOrder) VALUES (" +
                "1, " +
                "'2 + 2 bang bao nhieu?', " +
                "NULL, " +
                "'2 cong 2 bang 4.', " +
                "1)");

        // =========================
        // INSERT SAMPLE ANSWER OPTIONS
        // questionId = 1
        // =========================
        db.execSQL("INSERT INTO AnswerOption (questionId, answerText, isCorrect, optionOrder) VALUES (1, '3', 0, 1)");
        db.execSQL("INSERT INTO AnswerOption (questionId, answerText, isCorrect, optionOrder) VALUES (1, '4', 1, 2)");
        db.execSQL("INSERT INTO AnswerOption (questionId, answerText, isCorrect, optionOrder) VALUES (1, '5', 0, 3)");
        db.execSQL("INSERT INTO AnswerOption (questionId, answerText, isCorrect, optionOrder) VALUES (1, '6', 0, 4)");

        // =========================
        // INSERT SAMPLE RESULT
        // userId = 2 (Nguyen Van A)
        // practiceId = 1
        // =========================
        db.execSQL("INSERT INTO Result (userId, practiceId, score, correctCount, wrongCount, totalQuestions, duration) VALUES (" +
                "2, " +
                "1, " +
                "10.0, " +
                "1, " +
                "0, " +
                "1, " +
                "12)");

        // =========================
        // INSERT SAMPLE RESULT DETAIL
        // resultId = 1
        // questionId = 1
        // selectedAnswerOptionId = 2 ('4')
        // =========================
        db.execSQL("INSERT INTO ResultDetail (resultId, questionId, selectedAnswerOptionId, isCorrect) VALUES (" +
                "1, " +
                "1, " +
                "2, " +
                "1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ResultDetail");
        db.execSQL("DROP TABLE IF EXISTS Result");
        db.execSQL("DROP TABLE IF EXISTS AnswerOption");
        db.execSQL("DROP TABLE IF EXISTS Question");
        db.execSQL("DROP TABLE IF EXISTS Practice");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Account");
        onCreate(db);
    }
}