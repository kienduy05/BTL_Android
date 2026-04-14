package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.quizapp.DB.PracticeDAO;
import com.example.quizapp.Models.Practice;
import com.example.quizapp.Models.Question;
import com.example.quizapp.R;

import java.util.ArrayList;
import java.util.HashSet;

public class QuestionCheckboxAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Question> list;
    private final HashSet<Integer> selectedIds; // lưu questionId đã tick
    private final PracticeDAO practiceDAO;
    private OnSelectionChangedListener listener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    public QuestionCheckboxAdapter(Context context, ArrayList<Question> list, HashSet<Integer> selectedIds) {
        this.context = context;
        this.list = list;
        this.selectedIds = selectedIds;
        this.practiceDAO = new PracticeDAO(context);
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.listener = listener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_question_checkbox, parent, false);
            holder = new ViewHolder();
            holder.cbQuestion  = convertView.findViewById(R.id.cbQuestion);
            holder.tvContent   = convertView.findViewById(R.id.tvQuestionContent);
            holder.tvPractice  = convertView.findViewById(R.id.tvQuestionPractice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Question item = list.get(position);

        holder.tvContent.setText(item.getContent());

        // Hiển thị tên bài tập hiện tại
        Practice practice = practiceDAO.getpracticebyid(item.getPracticeId());
        if (practice != null) {
            holder.tvPractice.setText("Bài: " + practice.getPracticeName());
        } else {
            holder.tvPractice.setText("Chưa có bài tập");
        }

        // Set trạng thái checkbox
        holder.cbQuestion.setOnCheckedChangeListener(null); // tránh trigger khi recycle
        holder.cbQuestion.setChecked(selectedIds.contains(item.getQuestionId()));

        // Xử lý tick
        holder.cbQuestion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedIds.add(item.getQuestionId());
            } else {
                selectedIds.remove(item.getQuestionId());
            }
            if (listener != null) {
                listener.onSelectionChanged(selectedIds.size());
            }
        });

        // Click cả row cũng toggle checkbox
        convertView.setOnClickListener(v -> holder.cbQuestion.toggle());

        return convertView;
    }

    public HashSet<Integer> getSelectedIds() {
        return selectedIds;
    }

    static class ViewHolder {
        CheckBox cbQuestion;
        TextView tvContent, tvPractice;
    }
}