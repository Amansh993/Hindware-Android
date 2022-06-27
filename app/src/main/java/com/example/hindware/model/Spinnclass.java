package com.example.hindware.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Spinnclass extends ArrayAdapter<DropDownResponseList> {


    public Spinnclass(Context context, int textViewResourceId,
                       List<DropDownResponseList> values) {
        super(context, textViewResourceId, values);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position)._Value);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position)._Value);
        return label;
    }
}
