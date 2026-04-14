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
    public Result getresultbyid(int resultId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Result WHERE resultId=?", new String[]{String.valueOf(resultId)});
        Result item = null;

        if (cursor.moveToFirst()) {
            item = new Result();
            item.setResultId(cursor.getInt(0));
            item.setUserId(cursor.getInt(1));
            item.setPracticeId(cursor.getInt(2));
            item.setScore(cursor.isNull(3) ? null : cursor.getDouble(3));
            item.setCorrectCount(cursor.isNull(4) ? null : cursor.getInt(4));
            item.setWrongCount(cursor.isNull(5) ? null : cursor.getInt(5));
            item.setTotalQuestions(cursor.isNull(6) ? null : cursor.getInt(6));
            item.setDuration(cursor.isNull(7) ? null : cursor.getInt(7));
            item.setSubmittedAt(cursor.getString(8));
        }

        cursor.close();
        db.close();
        return item;
    }
    public long insertresult(Result result) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId", result.getUserId());
        values.put("practiceId", result.getPracticeId());
        values.put("score", result.getScore());
        values.put("correctCount", result.getCorrectCount());
        values.put("wrongCount", result.getWrongCount());
        values.put("totalQuestions", result.getTotalQuestions());
        values.put("duration", result.getDuration());


        long newRowId = db.insert("Result", null, values);
        db.close();
        return newRowId;
    }
    /*public ArrayList<Result> getResultsByPracticeId(int practiceId) {
        ArrayList<Result> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Result WHERE practiceId=? ORDER BY score DESC",
                new String[]{String.valueOf(practiceId)}
        );

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
    }*/
    public ArrayList<Result> getResultsWithUser(int practiceId) {
        ArrayList<Result> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT r.*, a.fullName FROM Result r " +
                        "INNER JOIN Account a ON r.userId = a.userId " +
                        "WHERE r.practiceId=? ORDER BY r.score DESC",
                new String[]{String.valueOf(practiceId)}
        );

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

                // 👇 QUAN TRỌNG
                item.setUserName(cursor.getString(9));

                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }





}