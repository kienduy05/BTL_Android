package com.example.quizapp.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Adapter.AccountAdapter;
import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.Models.Account;
import com.example.quizapp.R;

import java.util.ArrayList;

public class AdminAccountActivity extends AppCompatActivity {

    ListView lvAccount;
    Button btnAdd;

    ArrayList<Account> mylist;
    AccountDAO dao;
    AccountAdapter adapter;

    static final int REQUEST_ADD = 100;
    static final int REQUEST_EDIT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        lvAccount = findViewById(R.id.lvAccount);
        btnAdd = findViewById(R.id.btnAdd);

        dao = new AccountDAO(this);
        mylist = dao.getAllAccount();

        adapter = new AccountAdapter(this, R.layout.item_account, mylist);
        lvAccount.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAddAccountActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_ADD) {
                String fullName = data.getStringExtra("fullName");
                String email = data.getStringExtra("email");
                String password = data.getStringExtra("password");
                int role = data.getIntExtra("role", 0);

                Account acc = new Account(fullName, email, password, role, "", "");
                dao.insertAccount(acc);

                mylist.clear();
                mylist.addAll(dao.getAllAccount());
                adapter.notifyDataSetChanged();
            }
            else if (requestCode == REQUEST_EDIT) {
                int userId = data.getIntExtra("userId", -1);
                int position = data.getIntExtra("position", -1);
                String fullName = data.getStringExtra("fullName");
                int role = data.getIntExtra("role", 0);

                Account oldAcc = mylist.get(position);

                Account acc = new Account();
                acc.setUserId(userId);
                acc.setFullName(fullName);
                acc.setEmail(oldAcc.getEmail());
                acc.setPassword(oldAcc.getPassword());
                acc.setRole(role);
                acc.setAvatarUrl(oldAcc.getAvatarUrl());
                acc.setCreatedAt(oldAcc.getCreatedAt());

                dao.updateAccount(acc);
                mylist.set(position, acc);
                adapter.notifyDataSetChanged();
            }
        }
    }
}