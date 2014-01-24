package com.fridgeboard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
	 TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
	 txtTitle.setText(meal.title);
	 View midLayout = (View)view.findViewById(R.id.recipeMidLayout);
	 int[] tagT_array = new int[2]; 
	 tagT_array[0] = groupPosition;
	 tagT_array[1] = childPosition;
	 midLayout.setTag(tagT_array);
	 ((TextView)view.findViewById(R.id.txtDesc)).setText(meal.desc);
	 ((TextView)view.findViewById(R.id.txtDuration)).setText(meal.duration);
	 ImageView imgIcon = (ImageView)view.findViewById(R.id.imgIcon);
	 imgIcon.setImageResource(meal.icon);
	 int[] tagI_array = new int[2]; 
	 tagI_array[0] = groupPosition;
	 tagI_array[1] = childPosition;
	 imgIcon.setTag(tagI_array);

	 ImageButton remove = (ImageButton) view.findViewById(R.id.buttonRemove);
	 int[] tag_array = new int[2]; 
	 tag_array[0] = groupPosition;
	 tag_array[1] = childPosition;
	 remove.setTag(tag_array);
	 ImageButton refresh = (ImageButton) view.findViewById(R.id.buttonRefresh);
	 int[] tag_array2 = new int[2]; 
	 tag_array2[0] = groupPosition;
	 tag_array2[1] = childPosition;
	 refresh.setTag(tag_array2);
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
	 ImageButton add = (ImageButton) view.findViewById(R.id.buttonAdd);
	 int[] tag_array = new int[1]; 
	 tag_array[0] = groupPosition;
	 add.setTag(tag_array);
      
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