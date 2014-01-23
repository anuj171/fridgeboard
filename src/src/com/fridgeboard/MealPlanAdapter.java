package com.fridgeboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MealPlanAdapter extends ArrayAdapter<Meal>{

    Context context; 
    int layoutResourceId;    
    Meal data[] = null;
    
    public MealPlanAdapter(Context context, int layoutResourceId, Meal[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MealHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new MealHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtDesc = (TextView)row.findViewById(R.id.txtDesc);
            holder.txtDuration = (TextView)row.findViewById(R.id.txtDuration);
            
            row.setTag(holder);
        }
        else
        {
            holder = (MealHolder)row.getTag();
        }
        
        Meal meal = data[position];
        holder.txtTitle.setText(meal.title);
        holder.txtDesc.setText(meal.desc);
        holder.txtDuration.setText(meal.duration);
        holder.imgIcon.setImageResource(meal.icon);
        
        return row;
    }
    
    static class MealHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDesc;
        TextView txtDuration;
    }
}