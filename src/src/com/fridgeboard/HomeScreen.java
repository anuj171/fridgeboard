package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends Activity {

	private ArrayList<MealCategory> categoryList = new ArrayList<MealCategory>(); 
	private MealPlanAdapter listAdapter;
	private ExpandableListView mealPlanListView;

    Calendar rightNow;
	DateFormat df;
	DateFormat dayf;
	
	private TextView mealPlanHeader;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        
        loadData();
        
		rightNow = Calendar.getInstance();
		
		df = new SimpleDateFormat("EEE, d MMM");
		dayf = new SimpleDateFormat("EEE");
		
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

    	listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).title = "Meal suggestion";
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

    	;
    	// Do something in response to button click
		new AlertDialog.Builder(this)
	    .setTitle("Load recipe?")
	    .setMessage(listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).title)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with load recipe
	        	startActivity(new Intent(getApplicationContext(), Recipe.class));
	        }
	     })
	    .show();    
	}
    /** Called when the user touches the button */
    public void addMeal(View view) {
//    	startActivity(new Intent(this, SearchRecipeActivity.class));

    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];

	   	listAdapter.categoryList.get(groupPosition).mealList.add(new Meal(R.drawable.ic_launcher, "New meal added", "Description description Description description Description description...", "Time: 15 Min"));
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
    	
    	refreshMealPlan(dayf.format(rightNow.getTime()));
    	listAdapter.categoryList = (ArrayList<MealCategory>) categoryList.clone();
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

    	refreshMealPlan(dayf.format(rightNow.getTime()));
    	listAdapter.categoryList = (ArrayList<MealCategory>) categoryList.clone();
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
    	ArrayList<Meal> breakfasts = new ArrayList<Meal>();
    	breakfasts.add(new Meal(R.drawable.ic_launcher, "Boiled Eggs", "Description description Description description Description description...", "Time: 15 Min"));
    	breakfasts.add(new Meal(R.drawable.food, "Bread", "Description description Description description Description description...", "Time: 10 Min"));
    	categoryList.add(new MealCategory("BREAKFAST", breakfasts));

    	ArrayList<Meal> lunches = new ArrayList<Meal>();
    	lunches.add(new Meal(R.drawable.punjabirajma, "Aloo Gobhi", "Description description Description description Description description...", "Time: 15 Min"));
    	lunches.add(new Meal(R.drawable.food, "Naan", "Description description Description description Description description...", "Time: 10 Min"));
    	categoryList.add(new MealCategory("LUNCH", lunches));

    	ArrayList<Meal> dinners = new ArrayList<Meal>();
    	dinners.add(new Meal(R.drawable.ic_launcher, "Biryani", "Description description Description description Description description...", "Time: 15 Min"));
    	categoryList.add(new MealCategory("DINNER", dinners));
    }
    
    //load dummy next day's meal plan
    private void refreshMealPlan(String date){
    	categoryList.clear();
    	
    	ArrayList<Meal> breakfasts = new ArrayList<Meal>();
    	breakfasts.add(new Meal(R.drawable.ic_launcher, "Boiled Eggs "+date, "Description description Description description Description description...", "Time: 15 Min"));
    	breakfasts.add(new Meal(R.drawable.food, "Bread "+date, "Description description Description description Description description...", "Time: 10 Min"));
    	categoryList.add(new MealCategory("BREAKFAST", breakfasts));

    	ArrayList<Meal> lunches = new ArrayList<Meal>();
    	lunches.add(new Meal(R.drawable.punjabirajma, "Aloo Gobhi "+date, "Description description Description description Description description...", "Time: 15 Min"));
    	lunches.add(new Meal(R.drawable.food, "Naan "+date, "Description description Description description Description description...", "Time: 10 Min"));
    	categoryList.add(new MealCategory("LUNCH", lunches));

    	ArrayList<Meal> dinners = new ArrayList<Meal>();
    	dinners.add(new Meal(R.drawable.ic_launcher, "Biryani "+date, "Description description Description description Description description...", "Time: 15 Min"));
    	categoryList.add(new MealCategory("DINNER", dinners));    	
    }
}