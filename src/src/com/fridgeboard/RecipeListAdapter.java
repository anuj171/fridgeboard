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
            
            row.setTag(holder);
        }
        else
        {
            holder = (RecipeHolder)row.getTag();
        }

        holder.title.setText(item.getName());
        holder.desc.setText(item.getDescription());
        holder.duration.setText("Time: "+ item.getTotalTime());
        holder.nutrition.setText(item.getNutrition());
        holder.image.setImageResource(
        		context.getResources().getIdentifier(
    				item.getImage(), 
    				"drawable", context.getApplicationContext().getPackageName()));
        
        return row;
	}
	
	public class RecipeHolder{
		ImageView image;
		TextView title;
		TextView desc;
		TextView duration;
		TextView nutrition;
	}
}