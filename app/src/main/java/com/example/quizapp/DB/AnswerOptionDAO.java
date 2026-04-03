package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.AnswerOption;

import java.util.ArrayList;

public class AnswerOptionDAO {
    private final AppQuizDB dbHelper;

    public AnswerOptionDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<AnswerOption> getallansweroption() {
        ArrayList<AnswerOption> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AnswerOption", null);

        if (cursor.moveToFirst()) {
            do {
                AnswerOption item = new AnswerOption();
                item.setAnswerOptionId(cursor.getInt(0));
                item.setQuestionId(cursor.getInt(1));
                item.setAnswerText(cursor.getString(2));
                item.setIsCorrect(cursor.getInt(3));
                item.setOptionOrder(cursor.isNull(4) ? null : cursor.getInt(4));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public AnswerOption getansweroptionbyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AnswerOption WHERE answerOptionId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            AnswerOption item = new AnswerOption();
            item.setAnswerOptionId(cursor.getInt(0));
            item.setQuestionId(cursor.getInt(1));
            item.setAnswerText(cursor.getString(2));
            item.setIsCorrect(cursor.getInt(3));
            item.setOptionOrder(cursor.isNull(4) ? null : cursor.getInt(4));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertansweroption(AnswerOption item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("questionId", item.getQuestionId());
        values.put("answerText", item.getAnswerText());
        values.put("isCorrect", item.getIsCorrect());
        values.put("optionOrder", item.getOptionOrder());

        long result = db.insert("AnswerOption", null, values);
        db.close();
        return result;
    }

    public int updateansweroption(AnswerOption item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("questionId", item.getQuestionId());
        values.put("answerText", item.getAnswerText());
        values.put("isCorrect", item.getIsCorrect());
        values.put("optionOrder", item.getOptionOrder());

        int result = db.update("AnswerOption", values, "answerOptionId=?",
                new String[]{String.valueOf(item.getAnswerOptionId())});
        db.close();
        return result;
    }

    public int deleteansweroption(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("AnswerOption", "answerOptionId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}