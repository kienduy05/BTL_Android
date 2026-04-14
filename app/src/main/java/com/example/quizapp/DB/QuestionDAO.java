package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.Question;

import java.util.ArrayList;

public class QuestionDAO {
    private final AppQuizDB dbHelper;

    public QuestionDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<Question> getallquestion() {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Question", null);

        if (cursor.moveToFirst()) {
            do {
                Question item = new Question();
                item.setQuestionId(cursor.getInt(0));
                item.setPracticeId(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setImageUrl(cursor.getString(3));
                item.setExplanation(cursor.getString(4));
                item.setQuestionOrder(cursor.isNull(5) ? null : cursor.getInt(5));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Question getquestionbyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Question WHERE questionId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Question item = new Question();
            item.setQuestionId(cursor.getInt(0));
            item.setPracticeId(cursor.getInt(1));
            item.setContent(cursor.getString(2));
            item.setImageUrl(cursor.getString(3));
            item.setExplanation(cursor.getString(4));
            item.setQuestionOrder(cursor.isNull(5) ? null : cursor.getInt(5));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertquestion(Question item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("practiceId", item.getPracticeId());
        values.put("content", item.getContent());
        values.put("imageUrl", item.getImageUrl());
        values.put("explanation", item.getExplanation());
        values.put("questionOrder", item.getQuestionOrder());

        long result = db.insert("Question", null, values);
        db.close();
        return result;
    }
    public ArrayList<Question> getquestionbycategoryid(int categoryId) {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT q.* FROM Question q " +
                        "INNER JOIN Practice p ON q.practiceId = p.practiceId " +
                        "WHERE p.categoryId = ?",
                new String[]{String.valueOf(categoryId)}
        );

        if (cursor.moveToFirst()) {
            do {
                Question item = new Question();
                item.setQuestionId(cursor.getInt(0));
                item.setPracticeId(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setImageUrl(cursor.getString(3));
                item.setExplanation(cursor.getString(4));
                item.setQuestionOrder(cursor.isNull(5) ? null : cursor.getInt(5));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<Question> getquestionbypracticeid(int practiceId) {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Question WHERE practiceId=?",
                new String[]{String.valueOf(practiceId)});

        if (cursor.moveToFirst()) {
            do {
                Question item = new Question();
                item.setQuestionId(cursor.getInt(0));
                item.setPracticeId(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setImageUrl(cursor.getString(3));
                item.setExplanation(cursor.getString(4));
                item.setQuestionOrder(cursor.isNull(5) ? null : cursor.getInt(5));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Cập nhật practiceId cho 1 câu hỏi
    public int updatepracticeid(int questionId, int practiceId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("practiceId", practiceId);
        int result = db.update("Question", values, "questionId=?",
                new String[]{String.valueOf(questionId)});
        db.close();
        return result;
    }
    public int updatequestion(Question item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("practiceId", item.getPracticeId());
        values.put("content", item.getContent());
        values.put("imageUrl", item.getImageUrl());
        values.put("explanation", item.getExplanation());
        values.put("questionOrder", item.getQuestionOrder());

        int result = db.update("Question", values, "questionId=?",
                new String[]{String.valueOf(item.getQuestionId())});
        db.close();
        return result;
    }

    public int deletequestion(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Question", "questionId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}