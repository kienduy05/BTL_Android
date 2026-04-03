package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.Result;

import java.util.ArrayList;

public class ResultDAO {
    private final AppQuizDB dbHelper;

    public ResultDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<Result> getallresult() {
        ArrayList<Result> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Result", null);

        if (cursor.moveToFirst()) {
            do {
                Result item = new Result();
                item.setResultId(cursor.getInt(0));
                item.setUserId(cursor.getInt(1));
                item.setPracticeId(cursor.getInt(2));
                item.setScore(cursor.isNull(3) ? null : cursor.getDouble(3));
                item.setCorrectCount(cursor.isNull(4) ? null : cursor.getInt(4));
                item.setWrongCount(cursor.isNull(5) ? null : cursor.getInt(5));
                item.setTotalQuestions(cursor.isNull(6) ? null : cursor.getInt(6));
                item.setDuration(cursor.isNull(7) ? null : cursor.getInt(7));
                item.setSubmittedAt(cursor.getString(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Result getresultbyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Result WHERE resultId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Result item = new Result();
            item.setResultId(cursor.getInt(0));
            item.setUserId(cursor.getInt(1));
            item.setPracticeId(cursor.getInt(2));
            item.setScore(cursor.isNull(3) ? null : cursor.getDouble(3));
            item.setCorrectCount(cursor.isNull(4) ? null : cursor.getInt(4));
            item.setWrongCount(cursor.isNull(5) ? null : cursor.getInt(5));
            item.setTotalQuestions(cursor.isNull(6) ? null : cursor.getInt(6));
            item.setDuration(cursor.isNull(7) ? null : cursor.getInt(7));
            item.setSubmittedAt(cursor.getString(8));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertresult(Result item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", item.getUserId());
        values.put("practiceId", item.getPracticeId());
        values.put("score", item.getScore());
        values.put("correctCount", item.getCorrectCount());
        values.put("wrongCount", item.getWrongCount());
        values.put("totalQuestions", item.getTotalQuestions());
        values.put("duration", item.getDuration());

        long result = db.insert("Result", null, values);
        db.close();
        return result;
    }

    public int updateresult(Result item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", item.getUserId());
        values.put("practiceId", item.getPracticeId());
        values.put("score", item.getScore());
        values.put("correctCount", item.getCorrectCount());
        values.put("wrongCount", item.getWrongCount());
        values.put("totalQuestions", item.getTotalQuestions());
        values.put("duration", item.getDuration());

        int result = db.update("Result", values, "resultId=?",
                new String[]{String.valueOf(item.getResultId())});
        db.close();
        return result;
    }

    public int deleteresult(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Result", "resultId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}