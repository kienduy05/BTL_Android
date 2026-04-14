package com.example.quizapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.Models.Category;
import com.example.quizapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Category> list;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
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
        return list.get(position).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvCategoryName);
            holder.tvDesc = convertView.findViewById(R.id.tvCategoryDesc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category item = list.get(position);
        holder.tvName.setText(item.getCategoryName());

        if (item.getCategoryDescription() != null && !item.getCategoryDescription().isEmpty()) {
            holder.tvDesc.setText(item.getCategoryDescription());
        } else {
            holder.tvDesc.setText("Không có mô tả");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvDesc;
    }
}