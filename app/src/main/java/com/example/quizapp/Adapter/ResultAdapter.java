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
import java.text.SimpleDateFormat;
import java.util.Date;
public class ResultAdapter extends BaseAdapter {
    private String calculateStartTime(String submittedAt, Integer duration) {
        try {
            if (submittedAt == null || duration == null) return "Không có dữ liệu";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date submittedDate = sdf.parse(submittedAt);
            long startMillis = submittedDate.getTime() - (duration * 1000L);
            Date startDate = new Date(startMillis);
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            return displayFormat.format(startDate);

        } catch (Exception e) {
            return "Lỗi thời gian";
        }
    }
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
            holder.tvTimeStart   = convertView.findViewById(R.id.tvTimeStart);
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
        String startTime = calculateStartTime(r.getSubmittedAt(), r.getDuration());
        holder.tvTimeStart.setText("Thời gian Bắt đầu: " + startTime);
        return convertView;
    }

    static class ViewHolder {
        TextView tvUser, tvScore, tvDetail, tvTime ,tvTimeStart;
    }
}