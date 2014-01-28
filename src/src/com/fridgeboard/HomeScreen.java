package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.fridgeboard.DataAccess.DataHelper;
import com.fridgeboard.DataAccess.RecipeCategory;
import com.fridgeboard.DataAccess.RecipeItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity {

	private DataAccess.DataSource datasource;

	private ArrayList<MealCategory> categoryList = new ArrayList<MealCategory>(); 
	private MealPlanAdapter listAdapter;
	private ExpandableListView mealPlanListView;

    Calendar rightNow;
	DateFormat df;
	DateFormat dayf;
	DateFormat datef;
	
	private TextView mealPlanHeader;
	private RatingBar planHealthRating, planTasteRating;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        rightNow = Calendar.getInstance();
		
		df = new SimpleDateFormat("EEE, d MMM");
		dayf = new SimpleDateFormat("EEE");
		datef = new SimpleDateFormat("EEE, d MMM");
        
    	DataAccess dataAccess = new DataAccess();
   	
        datasource = dataAccess.new DataSource(this);
        datasource.open(); 

        loadData();
		
		//get reference to the ExpandableListView
		mealPlanListView = (ExpandableListView) findViewById(R.id.mealPlanExpandableList);
		
        
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        header.setOnTouchListener(activitySwipeDetector);

        
        mealPlanHeader = (TextView)header.findViewById(R.id.txtHeader);
        mealPlanHeader.setText(df.format(rightNow.getTime()));

        planHealthRating = (RatingBar)header.findViewById(R.id.planHealthRating);
        planTasteRating = (RatingBar)header.findViewById(R.id.planTasteRating);
        mealPlanHeader.setText(df.format(rightNow.getTime()));

        updateRatings();
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
	   	long meal_id_to_delete = listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).meal_id;
	   	DataAccess.MealItem mealitem_to_be_deleted = datasource.getMealItemByID(meal_id_to_delete);  
	   	List<RecipeItem> recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());
	   	if (mealitem_to_be_deleted.category.equals("BREAKFAST")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.BreakFast.ordinal());   	
	   	} else if(mealitem_to_be_deleted.category.equals("LUNCH")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());   	
	   	} else if(mealitem_to_be_deleted.category.equals("DINNER")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());   	
	   	}
    	RecipeItem breakfast = recipes.get((new Random()).nextInt(recipes.size()));
    	while( mealitem_to_be_deleted.recipe_id == (int) breakfast.getId()){
	    	breakfast = recipes.get((new Random()).nextInt(recipes.size()));
    	}
    	DataAccess.MealItem new_mealitem = datasource.createMealItem(mealitem_to_be_deleted.date, mealitem_to_be_deleted.category, (int) breakfast.getId());
		DataAccess.RecipeItem recipe = datasource.getRecipeItem(new_mealitem.recipe_id);

	   	datasource.deleteMealItem(mealitem_to_be_deleted);
    	listAdapter.categoryList.get(groupPosition).mealList.remove(childPosition);
	   	listAdapter.categoryList.get(groupPosition).mealList.add(childPosition, new Meal(new_mealitem.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    	listAdapter.notifyDataSetInvalidated();
        updateRatings();
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
        updateRatings();

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

//	   	Toast.makeText(this, "Loading recipe "+listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).title, Toast.LENGTH_SHORT).show();
    	
	   	long recipeId = (long) listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).recipe_id; // NOTE: use a valid recipe id here
	   	
	   	Intent recipeLaunchIntent = new Intent(this, Recipe.class);
	   	recipeLaunchIntent.putExtra(Recipe.RECIPE_ID, recipeId);
	   	
    	startActivity(recipeLaunchIntent);
	}
    /** Called when the user touches the button */
    public void addMeal(View view) {
//    	startActivity(new Intent(this, SearchRecipeActivity.class));

    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];

	   	String meal_category = listAdapter.categoryList.get(groupPosition).category;
	   	List<RecipeItem> recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());
	   	if (meal_category.equals("BREAKFAST")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.BreakFast.ordinal());   	
	   	} else if(meal_category.equals("LUNCH")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());   	
	   	} else if(meal_category.equals("DINNER")){
	    	recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());   	
	   	}
    	RecipeItem recipe = recipes.get((new Random()).nextInt(recipes.size()));
	   	DataAccess.MealItem new_mealitem = datasource.createMealItem(datef.format(rightNow.getTime()), meal_category, (int)recipe.getId());
	   	listAdapter.categoryList.get(groupPosition).mealList.add(new Meal(new_mealitem.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    	listAdapter.notifyDataSetInvalidated();
        updateRatings();

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
        updateRatings();
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
        updateRatings();
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
//			startActivity(new Intent(this, DatabaseActivity.class));
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
//		Toast.makeText(this, "Loading data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size(), Toast.LENGTH_SHORT).show();

        ArrayList<Meal> breakfasts = new ArrayList<Meal>();
    	ArrayList<Meal> lunches = new ArrayList<Meal>();
    	ArrayList<Meal> dinners = new ArrayList<Meal>();
    	ArrayList<Meal> others = new ArrayList<Meal>();
    	
    	
    	for(int i=0;i<mealitems.size();i++){
    		DataAccess.MealItem meal = mealitems.get(i);
    		DataAccess.RecipeItem recipe = datasource.getRecipeItem(meal.recipe_id);
    		if(meal.category.equals("BREAKFAST")){
    			breakfasts.add(new Meal(meal.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    		} else if(meal.category.equals("LUNCH")){
    			lunches.add(new Meal(meal.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    		} else if(meal.category.equals("DINNER")){
    			dinners.add(new Meal(meal.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    		} else {
    			others.add(new Meal(meal.id, this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), recipe.getName(), recipe.getDescription(), recipe.getTotalTime(), (int) recipe.getId(), recipe.getHealthRating(), recipe.getTasteRating()));
    		}
    	}	
    	categoryList.add(new MealCategory("BREAKFAST", breakfasts));
    	categoryList.add(new MealCategory("LUNCH", lunches));
    	categoryList.add(new MealCategory("DINNER", dinners));
    	if(others.size() == 0){
  //  		Log.w("HomeScreen","creating data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size());
//    		Toast.makeText(this, "creating data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size(), Toast.LENGTH_SHORT).show();
    		createData();
    		loadData();
    	}
    }    
    private void createData(){
    	List<RecipeItem> breakfast_recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.BreakFast.ordinal());   	
    	List<RecipeItem> lunch_dinner_recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());   	
    	RecipeItem breakfast = breakfast_recipes.get((new Random()).nextInt(breakfast_recipes.size()));
    	datasource.createMealItem(datef.format(rightNow.getTime()), "BREAKFAST", (int) breakfast.getId());
    	int l_id = (int) (new Random()).nextInt(lunch_dinner_recipes.size());
    	RecipeItem lunch = lunch_dinner_recipes.get(l_id);
    	datasource.createMealItem(datef.format(rightNow.getTime()), "LUNCH", (int) lunch.getId());
    	int d_id = (int) (new Random()).nextInt(lunch_dinner_recipes.size()); 
    	while( d_id == l_id){
    		d_id = (int) (new Random()).nextInt(lunch_dinner_recipes.size());
    	}
//		Log.w("HomeScreen",""+d_id+","+(int) lunch.getId());
//		Toast.makeText(this, "ids: "+d_id+","+(int) lunch.getId(), Toast.LENGTH_SHORT).show();
    	RecipeItem dinner = lunch_dinner_recipes.get(d_id);
    	datasource.createMealItem(datef.format(rightNow.getTime()), "DINNER", (int) dinner.getId());
    	datasource.createMealItem(datef.format(rightNow.getTime()), "OTHERS", 1);
    }
    
    private void updateRatings(){
    	int meal_count = 0;
    	float health_avg=0, health_sum = 0;
    	float taste_avg=0, taste_sum = 0;
    	for(int i=0;i<categoryList.size();i++){
    		for(int j=0;j<categoryList.get(i).mealList.size(); j++){
    			meal_count++;
    			health_sum += categoryList.get(i).mealList.get(j).health_rating;
    			taste_sum += categoryList.get(i).mealList.get(j).taste_rating;
    		}
    	}
    	if (meal_count > 0){
    		health_avg = health_sum/meal_count;
    		taste_avg = taste_sum/meal_count;
    	}
    	planHealthRating.setRating(health_avg);
    	planHealthRating.invalidate();
    	planTasteRating.setRating(taste_avg);
    	planTasteRating.invalidate();
    }
    
    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
}
class ActivitySwipeDetector implements View.OnTouchListener {

static final String logTag = "ActivitySwipeDetector";
private HomeScreen activity;
static final int MIN_DISTANCE = 100;
private float downX, downY, upX, upY;

public ActivitySwipeDetector(HomeScreen activity){
    this.activity = activity;
}

public void onRightToLeftSwipe(){
    Log.i(logTag, "RightToLeftSwipe!");
    activity.previousDay(null);
}

public void onLeftToRightSwipe(){
    Log.i(logTag, "LeftToRightSwipe!");
    activity.nextDay(null);
}

public void onTopToBottomSwipe(){
    Log.i(logTag, "onTopToBottomSwipe!");
//    activity.doSomething();
}

public void onBottomToTopSwipe(){
    Log.i(logTag, "onBottomToTopSwipe!");
//    activity.doSomething();
}

public boolean onTouch(View v, MotionEvent event) {
    switch(event.getAction()){
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        case MotionEvent.ACTION_UP: {
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;

            // swipe horizontal?
            if(Math.abs(deltaX) > MIN_DISTANCE){
                // left or right
                if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
                if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
            }
            else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return false; // We don't consume the event
            }

            // swipe vertical?
            if(Math.abs(deltaY) > MIN_DISTANCE){
                // top or down
                if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
                if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
            }
            else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return false; // We don't consume the event
            }

            return true;
        }
    }
    return false;
}
}
