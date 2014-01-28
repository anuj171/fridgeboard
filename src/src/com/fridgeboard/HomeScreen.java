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
	   	RecipeItem recipeItem = datasource.getRecipeItem(mealitem_to_be_deleted.recipe_id);
	   	
	   	List<Long> recipeIdsToAvoid= new ArrayList<Long>();
	   	recipeIdsToAvoid.add(recipeItem.getId());
	   	
	   	Meal mealToAdd = addMealToCategory(mealitem_to_be_deleted.category, recipeItem.getCategory() == RecipeCategory.LunchOrDinnerSideDish, recipeIdsToAvoid);

	   	datasource.deleteMealItem(mealitem_to_be_deleted);
    	listAdapter.categoryList.get(groupPosition).mealList.remove(childPosition);
	   	listAdapter.categoryList.get(groupPosition).mealList.add(childPosition, mealToAdd);
    	listAdapter.notifyDataSetInvalidated();
        updateRatings(); 
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
	}

    public void loadRecipe(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];
	   	int childPosition = tag_array[1];
	
	   	long recipeId = (long) listAdapter.categoryList.get(groupPosition).mealList.get(childPosition).recipe_id; // NOTE: use a valid recipe id here
	   	
	   	Intent recipeLaunchIntent = new Intent(this, Recipe.class);
	   	recipeLaunchIntent.putExtra(Recipe.RECIPE_ID, recipeId);
	   	
    	startActivity(recipeLaunchIntent);
	}
    
    /** Called when the user touches the button */
    public void addMeal(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];

	   	String meal_category = listAdapter.categoryList.get(groupPosition).category;
	   	
	   	List<Meal> meals = addMealsToCategory(meal_category, null);
	   	
	   	if (meals.size() > 0)
	   	{
	   		for (int i = 0; i < meals.size(); ++i)
	   		{
			   	listAdapter.categoryList.get(groupPosition).mealList.add(meals.get(i));
	   		}
	   		
	    	listAdapter.notifyDataSetInvalidated();
	        updateRatings();
	   	}
    }
    
    public List<Meal> addMealsToCategory(String meal_category, List<Long> recipeIdsToAvoid) {
    	List<Meal> meals = new ArrayList<Meal>();
    	
    	meals.add(addMealToCategory(meal_category, false, recipeIdsToAvoid));
    	
    	if(meal_category.equals("LUNCH") || meal_category.equals("DINNER")){
    		meals.add(addMealToCategory(meal_category, true, recipeIdsToAvoid));
    	}
    	
    	return meals;
    }
    
	public Meal addMealToCategory(String meal_category, boolean isLunchOrDinnerSideDish, List<Long> recipeIdsToAvoid) {
	   	List<RecipeItem> recipes = null;
	   	if (meal_category.equals("BREAKFAST")){
	   		recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.BreakFast.ordinal());   	
	   	} else if(meal_category.equals("LUNCH") || meal_category.equals("DINNER")){
	    	if (!isLunchOrDinnerSideDish)
	    		recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerMainDish.ordinal());
	    	else
	    		recipes = datasource.getRecipesByCriteria(DataHelper.RECIPE_COLUMN_CATEGORY + " = " + RecipeCategory.LunchOrDinnerSideDish.ordinal());
	   	}
	   	 	
	   	if (recipes != null)
	   		return addRecipeItemToMeal(meal_category, recipes, recipeIdsToAvoid);
	   	
	   	return null;
	}

	private Meal addRecipeItemToMeal(String meal_category, List<RecipeItem> recipes, List<Long> recipeIdsToAvoid) {
		
		int recipeId = (new Random()).nextInt(recipes.size());
		int count = 10;
		while (count-- >= 0 && recipeIdsToAvoid != null)
		{
			if (!recipeIdsToAvoid.contains(recipes.get(recipeId).getId()))
				break;
			
			recipeId = (new Random()).nextInt(recipes.size());
		}
		
		RecipeItem recipe = recipes.get(recipeId);
		
		DataAccess.MealItem new_mealitem = datasource.createMealItem(datef.format(rightNow.getTime()), meal_category, (int)recipe.getId());
		
		return CreateMealItem(new_mealitem, recipe);
	}

    /** Called when the user touches the button */
    public void nextDay(View view) {
    	rightNow.add(Calendar.DAY_OF_MONTH, 1);
    	mealPlanHeader.setText(df.format(rightNow.getTime()));
    	mealPlanHeader.invalidate();
    	
    	loadData();
    	listAdapter.notifyDataSetInvalidated();
        updateRatings();  
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
			break;
		case R.id.action_settings:
			break;
		}
		return false;
	}
    
  //load some initial data into out list 
    private void loadData(){
    	categoryList.clear();

        List<DataAccess.MealItem> mealitems = datasource.getAllMealItemsForADate(datef.format(rightNow.getTime()));
		Log.w("HomeScreen","Loading data for "+datef.format(rightNow.getTime())+", matching meals = "+mealitems.size());

        ArrayList<Meal> breakfasts = new ArrayList<Meal>();
    	ArrayList<Meal> lunches = new ArrayList<Meal>();
    	ArrayList<Meal> dinners = new ArrayList<Meal>();
    	ArrayList<Meal> others = new ArrayList<Meal>();
    	
    	
    	for(int i=0;i<mealitems.size();i++){
    		DataAccess.MealItem meal = mealitems.get(i);
    		DataAccess.RecipeItem recipe = datasource.getRecipeItem(meal.recipe_id);
    		
    		Meal mealItem = CreateMealItem(meal, recipe);
    		
    		if(meal.category.equals("BREAKFAST")){
    			breakfasts.add(mealItem);
    		} else if(meal.category.equals("LUNCH")){
    			lunches.add(mealItem);
    		} else if(meal.category.equals("DINNER")){
    			dinners.add(mealItem);
    		} else {
    			others.add(mealItem);
    		}
    	}	
    	categoryList.add(new MealCategory("BREAKFAST", breakfasts));
    	categoryList.add(new MealCategory("LUNCH", lunches));
    	categoryList.add(new MealCategory("DINNER", dinners));
    	if(others.size() == 0){
    		createData();
    		loadData();
    	}
    }

	private Meal CreateMealItem(DataAccess.MealItem meal, DataAccess.RecipeItem recipe) {
		return new Meal(meal.id,
					this.getResources().getIdentifier(recipe.getImage(), "drawable", "com.fridgeboard"), 
					recipe.getName(), 
					recipe.getDescription(), 
					recipe.getTotalTime(), 
					(int) recipe.getId(), 
					recipe.getHealthRating(), 
					recipe.getTasteRating(),
					recipe.getCategory() == RecipeCategory.LunchOrDinnerSideDish);
	}    
    
    private void createData() {
    	
    	addMealsToCategory("BREAKFAST", null);
    	List<Meal> lunch = addMealsToCategory("LUNCH", null);

    	List<Long> recipeIdsToAvoid = new ArrayList<Long>();
    	for (int i = 0; i < lunch.size(); ++i)
    	{
    		recipeIdsToAvoid.add((long)lunch.get(i).recipe_id);
    	}
    	addMealsToCategory("DINNER", recipeIdsToAvoid);
    	
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
static final int MIN_DISTANCE = 10;
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
