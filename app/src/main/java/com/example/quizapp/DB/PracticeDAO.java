package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.Practice;

import java.util.ArrayList;

public class PracticeDAO {
    private final AppQuizDB dbHelper;

    public PracticeDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<Practice> getallpractice() {
        ArrayList<Practice> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Practice", null);

        if (cursor.moveToFirst()) {
            do {
                Practice item = new Practice();
                item.setPracticeId(cursor.getInt(0));
                item.setCategoryId(cursor.getInt(1));
                item.setPracticeName(cursor.getString(2));
                item.setPracticeDescription(cursor.getString(3));
                item.setTimeLimit(cursor.isNull(4) ? null : cursor.getInt(4));
                item.setTotalQuestions(cursor.isNull(5) ? null : cursor.getInt(5));
                item.setCreatedBy(cursor.isNull(6) ? null : cursor.getInt(6));
                item.setCreatedAt(cursor.getString(7));
                item.setUpdatedAt(cursor.getString(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Practice getpracticebyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Practice WHERE practiceId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Practice item = new Practice();
            item.setPracticeId(cursor.getInt(0));
            item.setCategoryId(cursor.getInt(1));
            item.setPracticeName(cursor.getString(2));
            item.setPracticeDescription(cursor.getString(3));
            item.setTimeLimit(cursor.isNull(4) ? null : cursor.getInt(4));
            item.setTotalQuestions(cursor.isNull(5) ? null : cursor.getInt(5));
            item.setCreatedBy(cursor.isNull(6) ? null : cursor.getInt(6));
            item.setCreatedAt(cursor.getString(7));
            item.setUpdatedAt(cursor.getString(8));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertpractice(Practice item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryId", item.getCategoryId());
        values.put("practiceName", item.getPracticeName());
        values.put("practiceDescription", item.getPracticeDescription());
        values.put("timeLimit", item.getTimeLimit());
        values.put("totalQuestions", item.getTotalQuestions());
        values.put("createdBy", item.getCreatedBy());

        long result = db.insert("Practice", null, values);
        db.close();
        return result;
    }

    public int updatepractice(Practice item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryId", item.getCategoryId());
        values.put("practiceName", item.getPracticeName());
        values.put("practiceDescription", item.getPracticeDescription());
        values.put("timeLimit", item.getTimeLimit());
        values.put("totalQuestions", item.getTotalQuestions());
        values.put("createdBy", item.getCreatedBy());

        int result = db.update("Practice", values, "practiceId=?",
                new String[]{String.valueOf(item.getPracticeId())});
        db.close();
        return result;
    }

    public int deletepractice(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Practice", "practiceId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}