package com.example.quizapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quizapp.Activities.Admin.AdminEditAccountActivity;
import com.example.quizapp.DB.AccountDAO;
import com.example.quizapp.Models.Account;
import com.example.quizapp.R;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Account> {

    Activity context;
    int idLayout;
    ArrayList<Account> mylist;
    AccountDAO dao;

    public AccountAdapter(Activity context, int idLayout, ArrayList<Account> mylist) {
        super(context, idLayout, mylist);
        this.context = context;
        this.idLayout = idLayout;
        this.mylist = mylist;
        dao = new AccountDAO(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflater = context.getLayoutInflater();
        convertView = myInflater.inflate(idLayout, null);

        Account acc = mylist.get(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtEmail = convertView.findViewById(R.id.txtEmail);
        TextView txtRole = convertView.findViewById(R.id.txtRole);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        txtName.setText(acc.getFullName());
        txtEmail.setText(acc.getEmail());
        txtRole.setText(acc.getRole() == 1 ? "Admin" : "User");

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminEditAccountActivity.class);
            intent.putExtra("userId", acc.getUserId());
            intent.putExtra("fullName", acc.getFullName());
            intent.putExtra("email", acc.getEmail());
            intent.putExtra("password", acc.getPassword());
            intent.putExtra("role", acc.getRole());
            intent.putExtra("position", position);
            context.startActivityForResult(intent, 101);
        });

        btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có muốn xóa tài khoản này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {

                        // CHECK lịch sử
                        if(dao.hasResult(acc.getUserId())){
                            Toast.makeText(context,
                                    "Không thể xóa! Tài khoản đã có lịch sử làm bài",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(acc.getRole()==0){
                            Toast.makeText(context,
                                    "Không thể xóa! Tài khoản là Admin",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // XÓA
                        dao.deleteAccount(acc.getUserId());
                        mylist.remove(position);
                        notifyDataSetChanged();

                        Toast.makeText(context,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        return convertView;
    }
}