package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.Models.Result;
import com.example.quizapp.R;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Result> list;

    public ResultAdapter(Context context, ArrayList<Result> list) {
        this.context = context;
        this.list = list;
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
        return list.get(position).getResultId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_result, parent, false);

            holder = new ViewHolder();
            holder.tvUser   = convertView.findViewById(R.id.tvUser);
            holder.tvScore  = convertView.findViewById(R.id.tvScore);
            holder.tvDetail = convertView.findViewById(R.id.tvDetail);
            holder.tvTime   = convertView.findViewById(R.id.tvTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Result r = list.get(position);

        // User
        holder.tvUser.setText(r.getUserName());

        // Score
        holder.tvScore.setText("Điểm: " + r.getScore());

        // Đúng / Sai
        holder.tvDetail.setText("Đúng: " + r.getCorrectCount()
                + " | Sai: " + r.getWrongCount());

        // Thời gian
        if (r.getDuration() != null) {
            holder.tvTime.setText("Thời gian: " + r.getDuration() + "s");
        } else {
            holder.tvTime.setText("Không có dữ liệu");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvUser, tvScore, tvDetail, tvTime;
    }
}