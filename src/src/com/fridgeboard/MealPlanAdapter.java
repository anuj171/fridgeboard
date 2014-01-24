package com.fridgeboard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MealPlanAdapter extends BaseExpandableListAdapter {

    Context context; 
    int layoutResourceId;  
    ArrayList<MealCategory> categoryList;
        
    public MealPlanAdapter(Context context, ArrayList<MealCategory> categoryList) {
//        super(context, layoutResourceId, data);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
     return categoryList.get(groupPosition).mealList.get(childPosition);
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
     return childPosition;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
      View view, ViewGroup parent) {
      
     Meal meal = (Meal) getChild(groupPosition, childPosition);
     if (view == null) {
	      LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      view = infalInflater.inflate(R.layout.widget_recipe_item, null);
     }
	 ((TextView)view.findViewById(R.id.txtTitle)).setText(meal.title);
	 ((TextView)view.findViewById(R.id.txtDesc)).setText(meal.desc);
	 ((TextView)view.findViewById(R.id.txtDuration)).setText(meal.duration);
	 ((ImageView)view.findViewById(R.id.imgIcon)).setImageResource(meal.icon);

     return view;
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
    	return categoryList.get(groupPosition).mealList.size();
    }
    
    @Override
    public Object getGroup(int groupPosition) {
     return categoryList.get(groupPosition);
    }
    
    @Override
    public int getGroupCount() {
     return categoryList.size();
    }
    
    @Override
    public long getGroupId(int groupPosition) {
     return groupPosition;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
      ViewGroup parent) {
      
     MealCategory category = (MealCategory) getGroup(groupPosition);
     if (view == null) {
      LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inf.inflate(R.layout.meal_plan_category, null);
     }
      
     ((TextView) view.findViewById(R.id.category)).setText(category.category.trim());
      
     return view;
    }
    
    @Override
    public boolean hasStableIds() {
     return true;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
     return true;
    }
     
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        MealHolder holder = null;
//        
//        if(row == null)
//        {
//            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//            
//            holder = new MealHolder();
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
//            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
//            holder.txtDesc = (TextView)row.findViewById(R.id.txtDesc);
//            holder.txtDuration = (TextView)row.findViewById(R.id.txtDuration);
//            
//            row.setTag(holder);
//        }
//        else
//        {
//            holder = (MealHolder)row.getTag();
//        }
//        
//        Meal meal = data[position];
//        holder.txtTitle.setText(meal.title);
//        holder.txtDesc.setText(meal.desc);
//        holder.txtDuration.setText(meal.duration);
//        holder.imgIcon.setImageResource(meal.icon);
//        
//        return row;
//    }
//    
//    static class MealHolder
//    {
//        ImageView imgIcon;
//        TextView txtTitle;
//        TextView txtDesc;
//        TextView txtDuration;
//    }
}