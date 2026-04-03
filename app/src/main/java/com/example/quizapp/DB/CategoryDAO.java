package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.Category;

import java.util.ArrayList;

public class CategoryDAO {
    private final AppQuizDB dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    public ArrayList<Category> getallcategory() {
        ArrayList<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category", null);

        if (cursor.moveToFirst()) {
            do {
                Category item = new Category();
                item.setCategoryId(cursor.getInt(0));
                item.setCategoryName(cursor.getString(1));
                item.setCategoryDescription(cursor.getString(2));
                item.setImageUrl(cursor.getString(3));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Category getcategorybyid(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category WHERE categoryId=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Category item = new Category();
            item.setCategoryId(cursor.getInt(0));
            item.setCategoryName(cursor.getString(1));
            item.setCategoryDescription(cursor.getString(2));
            item.setImageUrl(cursor.getString(3));
            cursor.close();
            db.close();
            return item;
        }

        cursor.close();
        db.close();
        return null;
    }

    public long insertcategory(Category item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryName", item.getCategoryName());
        values.put("categoryDescription", item.getCategoryDescription());
        values.put("imageUrl", item.getImageUrl());

        long result = db.insert("Category", null, values);
        db.close();
        return result;
    }

    public int updatecategory(Category item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryName", item.getCategoryName());
        values.put("categoryDescription", item.getCategoryDescription());
        values.put("imageUrl", item.getImageUrl());

        int result = db.update("Category", values, "categoryId=?",
                new String[]{String.valueOf(item.getCategoryId())});
        db.close();
        return result;
    }

    public int deletecategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Category", "categoryId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}