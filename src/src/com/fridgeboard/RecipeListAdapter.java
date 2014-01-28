package com.fridgeboard;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fridgeboard.DataAccess.RecipeItem;

public class RecipeListAdapter extends ArrayAdapter<RecipeItem> {
	public List<RecipeItem> recipeItems = null;
	public RecipeItem dummyItem;
	int layoutResourceId;
	Context context;
	
	public RecipeListAdapter(Context context, int layoutResourceId, List<RecipeItem> recipeItems){
		super(context, layoutResourceId, recipeItems);
		
		this.context = context;
		this.recipeItems = recipeItems;
		this.layoutResourceId = layoutResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecipeItem item;
		if(position < this.recipeItems.size()){
			item=(RecipeItem)this.recipeItems.get(position);			
		}
		else
			item = this.dummyItem;
		
		RecipeHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new RecipeHolder();
            holder.image = (ImageView)row.findViewById(R.id.imgIconSearch);
            holder.title = (TextView)row.findViewById(R.id.txtTitleSearch);
            holder.desc = (TextView)row.findViewById(R.id.txtDescSearch);
            holder.duration = (TextView)row.findViewById(R.id.txtDurationSearch);
            holder.nutrition = (TextView)row.findViewById(R.id.txtNutritionSearch);
            holder.imageTimeTaken = (ImageView)row.findViewById(R.id.imageTimeTakenSearch);
            row.setTag(holder);
        }
        else
        {
            holder = (RecipeHolder)row.getTag();
        }

        holder.title.setText(item.getName());
        holder.desc.setText(item.getDescription());
        holder.duration.setText(item.getTotalTime());
        holder.nutrition.setText("Nutrition: " + item.getNutrition());
        holder.image.setImageResource(
        		context.getResources().getIdentifier(
    				item.getImage(), 
    				"drawable", context.getApplicationContext().getPackageName()));
        holder.imageTimeTaken.setImageResource(getClockImageFromTime(item.getTotalTime()));
        
        return row;
	}
	int getClockImageFromTime(String time_string){
		int time = (int) Integer.parseInt(time_string.substring(0, time_string.length()-5));
		if (time <= 5){
			return context.getResources().getIdentifier(
    				"clock05", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 10){
			return context.getResources().getIdentifier(
    				"clock10", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 15){
			return context.getResources().getIdentifier(
    				"clock15", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 20){
			return context.getResources().getIdentifier(
    				"clock20", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 25){
			return context.getResources().getIdentifier(
    				"clock25", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 30){
			return context.getResources().getIdentifier(
    				"clock30", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 35){
			return context.getResources().getIdentifier(
    				"clock35", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 40){
			return context.getResources().getIdentifier(
    				"clock40", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 45){
			return context.getResources().getIdentifier(
    				"clock45", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 50){
			return context.getResources().getIdentifier(
    				"clock50", 
    				"drawable", context.getApplicationContext().getPackageName());
		} else if (time <= 55){
			return context.getResources().getIdentifier(
    				"clock55", 
    				"drawable", context.getApplicationContext().getPackageName());
		}  
		return context.getResources().getIdentifier(
				"clock60", 
				"drawable", context.getApplicationContext().getPackageName());
		}
	
	public class RecipeHolder{
		ImageView image;
		TextView title;
		TextView desc;
		TextView duration;
		TextView nutrition;
		ImageView imageTimeTaken;
	}
}