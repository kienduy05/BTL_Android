package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Question;
import com.example.quizapp.R;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Question> list;
    private final PracticeDAO practiceDAO;

    public QuestionAdapter(Context context, ArrayList<Question> list) {
        this.context = context;
        this.list = list;
        this.practiceDAO = new PracticeDAO(context);
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
        return list.get(position).getQuestionId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
            holder = new ViewHolder();
            holder.tvOrder    = convertView.findViewById(R.id.tvQuestionOrder);
            holder.tvContent  = convertView.findViewById(R.id.tvQuestionContent);
            holder.tvPractice = convertView.findViewById(R.id.tvQuestionPractice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Question item = list.get(position);

        // Số thứ tự
        int order = item.getQuestionOrder() != null ? item.getQuestionOrder() : (position + 1);
        holder.tvOrder.setText(String.valueOf(order));

        // Nội dung câu hỏi
        holder.tvContent.setText(item.getContent());

        // Tên bài tập
        Practice practice = practiceDAO.getpracticebyid(item.getPracticeId());
        if (practice != null) {
            holder.tvPractice.setText(practice.getPracticeName());
        } else {
            holder.tvPractice.setText("Không rõ");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvOrder, tvContent, tvPractice;
    }
}