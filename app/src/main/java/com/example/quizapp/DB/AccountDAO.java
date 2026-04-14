package com.example.quizapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizapp.Models.Account;

import java.util.ArrayList;

public class AccountDAO {

    private final AppQuizDB dbHelper;

    public AccountDAO(Context context) {
        dbHelper = new AppQuizDB(context);
    }

    // INSERT
    public long insertAccount(Account acc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("fullName", acc.getFullName());
        values.put("email", acc.getEmail());
        values.put("password", acc.getPassword());
        values.put("role", acc.getRole());
        values.put("avatarUrl", acc.getAvatarUrl());

        long result = db.insert("Account", null, values);
        db.close();
        return result;
    }

    // LOGIN
    public boolean login(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;
    }

    // GET ALL
    public ArrayList<Account> getAllAccount() {
        ArrayList<Account> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Account", null);

        if (cursor.moveToFirst()) {
            do {
                Account acc = new Account();
                acc.setUserId(cursor.getInt(0));
                acc.setFullName(cursor.getString(1));
                acc.setEmail(cursor.getString(2));
                acc.setPassword(cursor.getString(3));
                acc.setRole(cursor.getInt(4));
                acc.setAvatarUrl(cursor.getString(5));
                acc.setCreatedAt(cursor.getString(6));

                list.add(acc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // GET BY ID
    public Account getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE userId=?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            Account acc = new Account();
            acc.setUserId(cursor.getInt(0));
            acc.setFullName(cursor.getString(1));
            acc.setEmail(cursor.getString(2));
            acc.setPassword(cursor.getString(3));
            acc.setRole(cursor.getInt(4));
            acc.setAvatarUrl(cursor.getString(5));

            cursor.close();
            db.close();
            return acc;
        }

        cursor.close();
        db.close();
        return null;
    }

    // UPDATE
    public int updateAccount(Account acc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("fullName", acc.getFullName());
        values.put("email", acc.getEmail());
        values.put("password", acc.getPassword());
        values.put("role", acc.getRole());
        values.put("avatarUrl", acc.getAvatarUrl());

        int result = db.update("Account", values, "userId=?",
                new String[]{String.valueOf(acc.getUserId())});

        db.close();
        return result;
    }

    // DELETE
    public int deleteAccount(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Account", "userId=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    // LOGIN RETURN ACCOUNT
    public Account getAccountByEmailPassword(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE email=? AND password=?",
                new String[]{email, password}
        );

        if (cursor.moveToFirst()) {
            Account acc = new Account();
            acc.setUserId(cursor.getInt(0));
            acc.setFullName(cursor.getString(1));
            acc.setEmail(cursor.getString(2));
            acc.setPassword(cursor.getString(3));
            acc.setRole(cursor.getInt(4));
            acc.setAvatarUrl(cursor.getString(5));
            acc.setCreatedAt(cursor.getString(6));

            cursor.close();
            db.close();
            return acc;
        }

        cursor.close();
        db.close();
        return null;
    }

    // GET ACCOUNT BY ID
    public Account getAccountByID(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE userId=?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            Account acc = new Account();
            acc.setUserId(cursor.getInt(0));
            acc.setFullName(cursor.getString(1));
            acc.setEmail(cursor.getString(2));
            acc.setPassword(cursor.getString(3));
            acc.setRole(cursor.getInt(4));
            acc.setAvatarUrl(cursor.getString(5));
            acc.setCreatedAt(cursor.getString(6));


            cursor.close();
            db.close();
            return acc;
        }

        cursor.close();
        db.close();
        return null;
    }
    // CHECK EMAIL EXISTS
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE email=?",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;
    }

    // UPDATE PASSWORD BY EMAIL
    public int updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int result = db.update(
                "Account",
                values,
                "email=?",
                new String[]{email}
        );

        db.close();
        return result;
    }
    public boolean hasResult(int userId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Result WHERE userId=?",
                new String[]{String.valueOf(userId)}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;
    }
}