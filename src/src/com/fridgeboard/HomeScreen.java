package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fridgeboard.DataAccess.RecipeDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity {

	private DataAccess.MealsDataSource datasource;

	private ArrayList<MealCategory> categoryList = new ArrayList<MealCategory>(); 
	private MealPlanAdapter listAdapter;
	private ExpandableListView mealPlanListView;

    Calendar rightNow;
	DateFormat df;
	DateFormat dayf;
	DateFormat datef;
	
	private TextView mealPlanHeader;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        rightNow = Calendar.getInstance();
		
		df = new SimpleDateFormat("EEE, d MMM");
		dayf = new SimpleDateFormat("EEE");
		datef = new SimpleDateFormat("EEE, d MMM");
        
    	DataAccess dataAccess = new DataAccess();
        datasource = dataAccess.new MealsDataSource(this);
        datasource.open();

        loadData();
        loadRecipeData();
        
		
		//get reference to the ExpandableListView
		mealPlanListView = (ExpandableListView) findViewById(R.id.mealPlanExpandableList);
		
        
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        
        mealPlanHeader = (TextView)header.findViewById(R.id.txtHeader);
        mealPlanHeader.setText(df.format(rightNow.getTime()));

        mealPlanListView.addHeaderView(header);
        
		//create the adapter by passing your ArrayList data
		listAdapter = new MealPlanAdapter(this, categoryList);
		//attach the adapter to the list
		mealPlanListView.setAdapter(listAdapter);
		 
		//expand all Groups
		expandAll();

    }
    
    private void loadRecipeData()
    {
    	DataAccess dataAccess = new DataAccess();
    	RecipeDataSource datasource = dataAccess.new RecipeDataSource(this);
        datasource.open();
        datasource.fillDataIfEmpty();
        datasource.close();
    }
    
    //method to expand all groups
    private void expandAll() {
     int count = listAdapter.getGroupCount();
     for (int i = 0; i < count; i++){
      mealPlanListView.expandGroup(i);
     }
    }

    /** Called when the user touches the button */
    public void refreshMeal(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];
	   	int childPosition = tag_array[1];
	   	long meal_id_to_delete = listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).meal_id;
	   	DataAccess.MealItem mealitem_to_be_deleted = datasource.getMealItemByID(meal_id_to_delete);  
	   	DataAccess.MealItem new_mealitem = datasource.createMealItem(mealitem_to_be_deleted.date, mealitem_to_be_deleted.category, "New Meal Suggestion", "New meal item description desc desc desc", "25 Min", "new_recipe_id");
	   	datasource.deleteMealItem(mealitem_to_be_deleted);
    	listAdapter.categoryList.get(groupPosition).mealList.remove(childPosition);
    	listAdapter.categoryList.get(groupPosition).mealList.add(childPosition, new Meal(new_mealitem.id, R.drawable.ic_launcher, new_mealitem.name, new_mealitem.desc, new_mealitem.timetaken));
    	listAdapter.notifyDataSetInvalidated();
//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Next Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
	}
    
    /** Called when the user touches the button */
    public void removeMeal(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];
	   	int childPosition = tag_array[1];

	   	long meal_id_to_delete = listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).meal_id;
	   	DataAccess.MealItem mealitem_to_be_deleted = datasource.getMealItemByID(meal_id_to_delete);  
	   	datasource.deleteMealItem(mealitem_to_be_deleted);
    	listAdapter.categoryList.get(groupPosition).mealList.remove(childPosition);
    	listAdapter.notifyDataSetInvalidated();
//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Next Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
	}

    public void loadRecipe(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];
	   	int childPosition = tag_array[1];

	   	Toast.makeText(this, "Loading recipe "+listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).title, Toast.LENGTH_SHORT).show();
    	// Do something in response to button click
    	startActivity(new Intent(this, Recipe.class));
	}
    /** Called when the user touches the button */
    public void addMeal(View view) {
//    	startActivity(new Intent(this, SearchRecipeActivity.class));

    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];

	   	DataAccess.MealItem new_mealitem = datasource.createMealItem(datef.format(rightNow.getTime()), listAdapter.categoryList.get(groupPosition).category, "New "+listAdapter.categoryList.get(groupPosition).category, "Description description Description description Description description...", "Time: 15 Min", "new_meal_added");
    	listAdapter.categoryList.get(groupPosition).mealList.add(new Meal(new_mealitem.id, R.drawable.ic_launcher, new_mealitem.name, new_mealitem.desc, new_mealitem.timetaken));
    	listAdapter.notifyDataSetInvalidated();

    	//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Next Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
	}

    /** Called when the user touches the button */
    public void nextDay(View view) {
    	rightNow.add(Calendar.DAY_OF_MONTH, 1);
    	mealPlanHeader.setText(df.format(rightNow.getTime()));
    	mealPlanHeader.invalidate();
    	
    	loadData();
    	listAdapter.notifyDataSetInvalidated();
//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Next Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
	}
    
    
    /** Called when the user touches the button */
    public void previousDay(View view) {
        // Do something in response to button click
    	rightNow.add(Calendar.DAY_OF_MONTH, -1);
    	mealPlanHeader.setText(df.format(rightNow.getTime()));
    	mealPlanHeader.invalidate();

    	loadData();
    	listAdapter.notifyDataSetInvalidated();
//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Previous Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		switch(item.getItemId()) {
		case R.id.action_groceries:
			startActivity(new Intent(this, Groceries.class));
			//Toast.makeText(this, "Ordering on Big Basket", Toast.LENGTH_SHORT).show();
//			new AlertDialog.Builder(this)
//		    .setTitle("Generate Grocery List")
//		    .setMessage("Generating groceries list for your meal plan.")
//		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) { 
//		            // continue with delete
//		        }
//		     })
//		    .show();
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, DatabaseActivity.class));
//			new AlertDialog.Builder(this)
//		    .setTitle("Meal Preferences")
//		    .setMessage("using default settings.")
//		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) { 
//		            // continue with delete
//		        }
//		     })
//		    .show();
			break;
		}
		return false;
	}
    
  //load some initial data into out list 
    private void loadData(){
    	categoryList.clear();

        List<DataAccess.MealItem> mealitems = datasource.getAllMealItemsForADate(datef.format(rightNow.getTime()));
		Log.w("HomeScreen","Loading data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size());
		Toast.makeText(this, "Loading data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size(), Toast.LENGTH_SHORT).show();

        ArrayList<Meal> breakfasts = new ArrayList<Meal>();
    	ArrayList<Meal> lunches = new ArrayList<Meal>();
    	ArrayList<Meal> dinners = new ArrayList<Meal>();
    	ArrayList<Meal> others = new ArrayList<Meal>();
    	
    	for(int i=0;i<mealitems.size();i++){
    		DataAccess.MealItem meal = mealitems.get(i);
    		if(meal.desc.length()>50){
    			meal.desc = meal.desc.substring(0,49);
    		}
    		if(meal.category.equals("BREAKFAST")){
    			breakfasts.add(new Meal(meal.id, R.drawable.ic_launcher, meal.name, meal.desc, meal.timetaken));
    		} else if(meal.category.equals("LUNCH")){
    			lunches.add(new Meal(meal.id, R.drawable.punjabirajma, meal.name, meal.desc, meal.timetaken));
    		} else if(meal.category.equals("DINNER")){
    			dinners.add(new Meal(meal.id, R.drawable.food, meal.name, meal.desc, meal.timetaken));
    		} else {
    			others.add(new Meal(meal.id, R.drawable.punjabirajma, meal.name, meal.desc, meal.timetaken));
    		}
    	}	
    	categoryList.add(new MealCategory("BREAKFAST", breakfasts));
    	categoryList.add(new MealCategory("LUNCH", lunches));
    	categoryList.add(new MealCategory("DINNER", dinners));
    	if(others.size() == 0){
    		Log.w("HomeScreen","creating data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size());
    		Toast.makeText(this, "creating data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size(), Toast.LENGTH_SHORT).show();
    		createData();
    		loadData();
    	}
    }    
    private void createData(){
    	datasource.createMealItem(datef.format(rightNow.getTime()), "BREAKFAST", "Boiled Eggs", "Eggs boiled,  cut in half & sprayed with salt & onions", "Time: 15 Min", "boiled_eggs");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "BREAKFAST", "Brown Bread", "Bread from Birtannia", "Time: 5 Min", "brown_bread");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "LUNCH", "Aloo Gobhi", "Potato & Cauliflower Curry", "Time: 20 Min", "aloo_gobhi");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "LUNCH", "Roti", "Indian round chapathi", "Time: 15 Min", "roti");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "DINNER", "Biryani", "Hyderabadi delicacy containing rice, chicken & spices", "Time: 30 Min", "biryani");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "DINNER", "Red Wine", "To end a day on high", "Time: 5 Min", "red_wine");
    	datasource.createMealItem(datef.format(rightNow.getTime()), "OTHERS", "dummy", "dummy", "dummy", "dummy_id");
    }
}