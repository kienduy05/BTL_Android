package com.example.quizapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quizapp.DB.AnswerOptionDAO;
import com.example.quizapp.DB.QuestionDAO;
import com.example.quizapp.Models.AnswerOption;
import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.ResultDetail;
import com.example.quizapp.R;

import java.util.ArrayList;

public class HistoryDetailAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ResultDetail> details;
    private final QuestionDAO questionDAO;
    private final AnswerOptionDAO optionDAO;

    public HistoryDetailAdapter(Context context, ArrayList<ResultDetail> details) {
        this.context = context;
        this.details = details;
        this.questionDAO = new QuestionDAO(context);
        this.optionDAO = new AnswerOptionDAO(context);
    }

    @Override
    public int getCount() { return details.size(); }
    @Override
    public Object getItem(int position) { return details.get(position); }
    @Override
    public long getItemId(int position) { return details.get(position).getResultDetailId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history_detail, parent, false);
            holder = new ViewHolder();
            holder.tvContent = convertView.findViewById(R.id.tvQuestionContent);
            holder.tvOps = new TextView[]{
                    convertView.findViewById(R.id.tvOp1), convertView.findViewById(R.id.tvOp2),
                    convertView.findViewById(R.id.tvOp3), convertView.findViewById(R.id.tvOp4)
            };
            holder.layoutExplanation = convertView.findViewById(R.id.layoutExplanation);
            holder.tvExplanationText = convertView.findViewById(R.id.tvExplanationText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ResultDetail rd = details.get(position);
        Question q = questionDAO.getquestionbyid(rd.getQuestionId());
        ArrayList<AnswerOption> ops = optionDAO.getansweroptionbyquestionid(rd.getQuestionId());

        if (q != null) {
            holder.tvContent.setText("Câu " + (position + 1) + ": " + q.getContent());

            if (q.getExplanation() != null && !q.getExplanation().trim().isEmpty()) {
                holder.layoutExplanation.setVisibility(View.VISIBLE);
                holder.tvExplanationText.setText(q.getExplanation());
            } else {
                holder.layoutExplanation.setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < 4; i++) {
            if (i < ops.size()) {
                AnswerOption op = ops.get(i);
                holder.tvOps[i].setVisibility(View.VISIBLE);
                holder.tvOps[i].setText(op.getAnswerText());

                holder.tvOps[i].setBackgroundColor(Color.parseColor("#F5F5F5"));
                holder.tvOps[i].setTextColor(Color.parseColor("#333333"));

                if (op.getIsCorrect() == 1) {
                    holder.tvOps[i].setBackgroundColor(Color.parseColor("#D4EDDA"));
                    holder.tvOps[i].setTextColor(Color.parseColor("#155724"));
                }

                if (rd.getSelectedAnswerOptionId() != null && rd.getSelectedAnswerOptionId() == op.getAnswerOptionId()) {
                    if (op.getIsCorrect() == 0) {
                        holder.tvOps[i].setBackgroundColor(Color.parseColor("#F8D7DA"));
                        holder.tvOps[i].setTextColor(Color.parseColor("#721C24"));
                        holder.tvOps[i].setText(op.getAnswerText() + " (Bạn chọn sai)");
                    } else {
                        holder.tvOps[i].setText(op.getAnswerText() + " (Bạn chọn đúng)");
                    }
                }
            } else {
                holder.tvOps[i].setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvContent, tvExplanationText;
        LinearLayout layoutExplanation;
        TextView[] tvOps;
    }
}