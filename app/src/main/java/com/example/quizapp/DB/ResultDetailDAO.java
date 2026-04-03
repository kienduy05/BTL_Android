package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.ResultDetail;

import java.util.ArrayList;

public class ResultDetailDAO {
    private final AppQuizDB dbHelper;

    public ResultDetailDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<ResultDetail> getallresultdetail() {
        ArrayList<ResultDetail> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ResultDetail", null);

        if (cursor.moveToFirst()) {
            do {
                ResultDetail item = new ResultDetail();
                item.setResultDetailId(cursor.getInt(0));
                item.setResultId(cursor.getInt(1));
                item.setQuestionId(cursor.getInt(2));
                item.setSelectedAnswerOptionId(cursor.isNull(3) ? null : cursor.getInt(3));
                item.setIsCorrect(cursor.getInt(4));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public ResultDetail getresultdetailbyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ResultDetail WHERE resultDetailId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            ResultDetail item = new ResultDetail();
            item.setResultDetailId(cursor.getInt(0));
            item.setResultId(cursor.getInt(1));
            item.setQuestionId(cursor.getInt(2));
            item.setSelectedAnswerOptionId(cursor.isNull(3) ? null : cursor.getInt(3));
            item.setIsCorrect(cursor.getInt(4));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertresultdetail(ResultDetail item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("resultId", item.getResultId());
        values.put("questionId", item.getQuestionId());
        values.put("selectedAnswerOptionId", item.getSelectedAnswerOptionId());
        values.put("isCorrect", item.getIsCorrect());

        long result = db.insert("ResultDetail", null, values);
        db.close();
        return result;
    }

    public int updateresultdetail(ResultDetail item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("resultId", item.getResultId());
        values.put("questionId", item.getQuestionId());
        values.put("selectedAnswerOptionId", item.getSelectedAnswerOptionId());
        values.put("isCorrect", item.getIsCorrect());

        int result = db.update("ResultDetail", values, "resultDetailId=?",
                new String[]{String.valueOf(item.getResultDetailId())});
        db.close();
        return result;
    }

    public int deleteresultdetail(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("ResultDetail", "resultDetailId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}