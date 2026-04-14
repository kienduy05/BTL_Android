package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.R;

import java.util.ArrayList;

public class PracticeAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Practice> list;
    private final CategoryDAO categoryDAO;

    public PracticeAdapter(Context context, ArrayList<Practice> list) {
        this.context = context;
        this.list = list;
        this.categoryDAO = new CategoryDAO(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getPracticeId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_practice, parent, false);
            holder = new ViewHolder();
            holder.tvName     = convertView.findViewById(R.id.tvPracticeName);
            holder.tvCategory = convertView.findViewById(R.id.tvPracticeCategory);
            holder.tvQuestions = convertView.findViewById(R.id.tvPracticeQuestions);
            holder.tvTime     = convertView.findViewById(R.id.tvPracticeTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Practice item = list.get(position);
        holder.tvName.setText(item.getPracticeName());

        // Lấy tên danh mục
        Category category = categoryDAO.getcategorybyid(item.getCategoryId());
        if (category != null) {
            holder.tvCategory.setText(category.getCategoryName());
        } else {
            holder.tvCategory.setText("Không rõ");
        }

        // Số câu hỏi
        int totalQ = item.getTotalQuestions() != null ? item.getTotalQuestions() : 0;
        holder.tvQuestions.setText(totalQ + " câu");

        // Thời gian
        int time = item.getTimeLimit() != null ? item.getTimeLimit() : 0;
        holder.tvTime.setText(time + " phút");

        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvCategory, tvQuestions, tvTime;
    }
}