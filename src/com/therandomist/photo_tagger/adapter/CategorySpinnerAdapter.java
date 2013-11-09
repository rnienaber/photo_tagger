package com.therandomist.photo_tagger.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.therandomist.photo_tagger.model.Category;

import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final List<Category> data;

    public CategorySpinnerAdapter(List<Category> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(parent.getContext());
        label.setText(data.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(parent.getContext());
        label.setText(data.get(position).getName());

        return label;
    }
}
