package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.DB.CategoryDAO;
import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.Models.Category;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Result> list;
    private final PracticeDAO practiceDAO;
    private final CategoryDAO categoryDAO;

    public HistoryAdapter(Context context, ArrayList<Result> list) {
        this.context = context;
        this.list = list;
        this.practiceDAO = new PracticeDAO(context);
        this.categoryDAO = new CategoryDAO(context);
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) { return list.get(position); }

    @Override
    public long getItemId(int position) { return list.get(position).getResultId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvHistoryPracticeName);
            holder.tvCategory = convertView.findViewById(R.id.tvHistoryCategory);
            holder.tvDate = convertView.findViewById(R.id.tvHistoryDate);
            holder.tvScore = convertView.findViewById(R.id.tvHistoryScore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Result result = list.get(position);


        Practice practice = practiceDAO.getpracticebyid(result.getPracticeId());
        if (practice != null) {
            holder.tvName.setText(practice.getPracticeName());

            Category cat = categoryDAO.getcategorybyid(practice.getCategoryId());
            if(cat != null) holder.tvCategory.setText(cat.getCategoryName());
        } else {
            holder.tvName.setText("Bài tập đã xóa");
            holder.tvCategory.setText("Không rõ");
        }

        holder.tvDate.setText("Nộp lúc: " + result.getSubmittedAt());

        double score = result.getScore() != null ? result.getScore() : 0.0;
        holder.tvScore.setText(String.format("%.1f", score));

        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvCategory, tvDate, tvScore;
    }
}