package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fridgeboard.DataAccess.DataHelper;
import com.fridgeboard.DataAccess.RecipeCategory;
import com.fridgeboard.DataAccess.RecipeItem;

public class HomeScreen extends Activity {
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
            
			if (position == 0)
			{
	    	   	Intent intent = new Intent(HomeScreen.this, MealSettingsActivity.class);
	        	startActivity(intent);
			}
			else if (position == 8)
			{
				Intent intent = new Intent(HomeScreen.this, GraphActivity.class);
	        	startActivity(intent);
			}
			else
				Toast.makeText(HomeScreen.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
        	
            drawerLayout.closeDrawer(drawerListView); 
		}
    }

    private String[] drawerListViewItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    
	private DataAccess.DataSource datasource;

	private ArrayList<MealCategory> categoryList = new ArrayList<MealCategory>(); 
	private MealPlanAdapter listAdapter;
	private ExpandableListView mealPlanListView;

    Calendar rightNow;
    private static Calendar olderRightNow = null;
	DateFormat df;
	DateFormat dfday;
	DateFormat dfdate;
	DateFormat dayf;
	DateFormat datef;
	
	private TextView mealPlanHeaderDay, mealPlanHeaderDate;
	private RatingBar planHealthRating, planTasteRating, planCostRating;
	
	private long AddSearchedRecipeId;
	RecipeItem addSearchedRecipeItem;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        
        setContentView(R.layout.home_screen);
        
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) findViewById(R.id.titletext);
        titleText.setText(R.string.app_name);
        
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setVisibility(View.VISIBLE);
        
        ImageButton groceriesButton = (ImageButton) findViewById(R.id.action_groceries);
        groceriesButton.setVisibility(View.VISIBLE);
        
        if(olderRightNow == null)
        	rightNow = Calendar.getInstance();
        else
        	rightNow = olderRightNow;
		
		df = new SimpleDateFormat("EEE, d MMM");
		dfday = new SimpleDateFormat("EEEE");
		dfdate = new SimpleDateFormat("d");
		dayf = new SimpleDateFormat("EEE");
		datef = new SimpleDateFormat("EEE, d MMM");
        
    	DataAccess dataAccess = new DataAccess();
   	
        datasource = dataAccess.new DataSource(this);
        datasource.open(); 

        boolean canDismissHelp = true;
        // Add data of a meal if we are moving here from search screen
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			AddSearchedRecipeId = extras.getLong(Recipe.RECIPE_ID);
			addSearchedRecipeItem = datasource.getRecipeItem(AddSearchedRecipeId);
			if(olderRightNow == null)olderRightNow=rightNow;
			addSearchedRecipe();

			canDismissHelp = extras.getBoolean(SplashScreenActivity.FROM_SPASH);
		}
		
		// olderRightNow is used so that when we navigate from this page and come back we have
		// the recipe to add. Here after adding the recipe, we update the value of olderRightNow
		olderRightNow = rightNow;
		
        loadData();
        
		//get reference to the ExpandableListView
		mealPlanListView = (ExpandableListView) findViewById(R.id.mealPlanExpandableList);
        
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        header.setOnTouchListener(activitySwipeDetector);

        
        mealPlanHeaderDay = (TextView)header.findViewById(R.id.txtHeaderDay);
        mealPlanHeaderDate = (TextView)header.findViewById(R.id.txtHeaderDate);
        updateHeaderDate();
        planHealthRating = (RatingBar)header.findViewById(R.id.planHealthRating);
        planTasteRating = (RatingBar)header.findViewById(R.id.planTasteRating);
        planCostRating = (RatingBar)header.findViewById(R.id.planCostRating);

        updateRatings();
        mealPlanListView.addHeaderView(header);
		//create the adapter by passing your ArrayList data
		listAdapter = new MealPlanAdapter(this, categoryList);
		//attach the adapter to the list
		mealPlanListView.setAdapter(listAdapter);
		 
		//expand all Groups
		expandAll();
		
		// drawer
        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);
 
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        
        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
        
        // App Icon 
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
 
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.settings,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );
        
        //  enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        
        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        
		if(canDismissHelp)
			dismissHelp();
 
        // just styling option add shadow the right edge of the drawer
        //drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }
    
    public void helpScreenClick(View v)
    {
    	dismissHelp();
    }
    
    private void dismissHelp()
    {
    	ImageView helpView  = (ImageView) findViewById(R.id.helpScreen);
    	helpView.setVisibility(View.GONE);
    	getActionBar().show();
    	mealPlanListView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
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
	   	
	   	Meal mealToAdd = addMealToCategory(mealitem_to_be_deleted.category, recipeItem.getCategory() == RecipeCategory.LunchOrDinnerSideDish);

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
    
    // Hard coded for search activity. Can be improved.
    private String GetCategory(RecipeItem recipeItem){
    	if(recipeItem.getCategory() == RecipeCategory.BreakFast)
    		return "BREAKFAST";
    	else
    		return "LUNCH";
    }
    
    private void addSearchedRecipe(){
    	
    	String cat = GetCategory(addSearchedRecipeItem);
    	DataAccess.MealItem new_mealitem = datasource.createMealItem(datef.format(olderRightNow.getTime()), cat, (int)AddSearchedRecipeId);
		CreateMealItem(new_mealitem, addSearchedRecipeItem);
		String text = "Added "+addSearchedRecipeItem.getName()+" to " + cat;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
    /** Called when the user touches the button */
    public void addMeal(View view) {
    	int[] tag_array = (int [])view.getTag();
	   	int groupPosition = tag_array[0];
	   	String meal_category = listAdapter.categoryList.get(groupPosition).category;
    		
	   	List<Meal> meals = addMealsToCategory(meal_category);
	   	
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
    
    public List<Meal> addMealsToCategory(String meal_category) {
    	List<Meal> meals = new ArrayList<Meal>();
    	
    	meals.add(addMealToCategory(meal_category, false));
    	
    	if(meal_category.equals("LUNCH") || meal_category.equals("DINNER")){
    		meals.add(addMealToCategory(meal_category, true));
    	}
    	
    	return meals;
    }
    
	public Meal addMealToCategory(String meal_category, boolean isLunchOrDinnerSideDish) {
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
	   		return addRecipeItemToMeal(meal_category, recipes);
	   	
	   	return null;
	}

	private Meal addRecipeItemToMeal(String meal_category, List<RecipeItem> recipes) {
		
		int recipeId = (new Random()).nextInt(recipes.size());
		
		List<Long> recipeIdsToAvoid = generateAvoidList();
		
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
	
	private List<Long> generateAvoidList()
	{
		List<Long> recipeIdsToAvoid = new ArrayList<Long>();
		List<DataAccess.MealItem> mealItemsToday = datasource.getAllMealItemsForADate(datef.format(rightNow.getTime()));
		
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		
		List<DataAccess.MealItem> mealItemsYesterday = datasource.getAllMealItemsForADate(datef.format(rightNow.getTime()));
		
		rightNow.add(Calendar.DAY_OF_MONTH, 1);
		
		for (int i = 0; i < mealItemsToday.size(); ++i)
		{
			recipeIdsToAvoid.add((long)mealItemsToday.get(i).recipe_id);
		}
		
		for (int i = 0; i < mealItemsYesterday.size(); ++i)
		{
			recipeIdsToAvoid.add((long)mealItemsYesterday.get(i).recipe_id);
		}
		
		return recipeIdsToAvoid;
	}

    /** Called when the user touches the button */
    public void nextDay(View view) {
    	rightNow.add(Calendar.DAY_OF_MONTH, 1);
    	olderRightNow = rightNow;

    	updateHeaderDate();

    	loadData();
    	listAdapter.notifyDataSetInvalidated();
        updateRatings();  
	}
    
    /** Called when the user touches the button */
    public void previousDay(View view) {
        // Do something in response to button click
    	rightNow.add(Calendar.DAY_OF_MONTH, -1);

    	updateHeaderDate();
    	olderRightNow = rightNow;

    	loadData();
    	listAdapter.notifyDataSetInvalidated();
        updateRatings();    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       //setMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onSearchClicked(View v)
    {
    	startActivity(new Intent(this, SearchRecipeActivity.class));
    }
    
    public void onGroceriesClicked(View v)
    {
    	startActivity(new Intent(this, Groceries.class));
    }
    
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)	{
       // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
       // then it has handled the app icon touch event
       if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       
//		switch(item.getItemId()) {
//		case R.id.action_groceries:
//			startActivity(new Intent(this, Groceries.class));
//			break;
//		case R.id.search_settings:
//			startActivity(new Intent(this, SearchRecipeActivity.class));
//			break;
//		}
		return false;
	}
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
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
					recipe.getNutrition(), 
					recipe.getTotalTime(), 
					(int) recipe.getId(), 
					recipe.getHealthRating(), 
					recipe.getTasteRating(),
					recipe.getCostRating(),
					recipe.getCategory() == RecipeCategory.LunchOrDinnerSideDish,
					getClockImageFromTime(recipe.getTotalTime()));
	}    
    
    private void createData() {
    	
    	addMealsToCategory("BREAKFAST");
    	addMealsToCategory("LUNCH");
    	addMealsToCategory("DINNER");
    	
    	datasource.createMealItem(datef.format(rightNow.getTime()), "OTHERS", 1);
    }
    
    private void updateRatings(){
    	int meal_count = 0;
    	float health_avg=0, health_sum = 0;
    	float cost_avg=0, cost_sum = 0;
    	float taste_avg=0, taste_sum = 0;
    	for(int i=0;i<categoryList.size();i++){
    		for(int j=0;j<categoryList.get(i).mealList.size(); j++){
    			meal_count++;
    			health_sum += categoryList.get(i).mealList.get(j).health_rating;
    			taste_sum += categoryList.get(i).mealList.get(j).taste_rating;
    			cost_sum += categoryList.get(i).mealList.get(j).cost_rating;
    		}
    	}
    	if (meal_count > 0){
    		health_avg = health_sum/meal_count;
    		taste_avg = taste_sum/meal_count;
    		cost_avg = cost_sum/meal_count;
    	}
    	planHealthRating.setRating(health_avg);
    	planHealthRating.invalidate();
    	planTasteRating.setRating(taste_avg);
    	planTasteRating.invalidate();
    	planCostRating.setRating(cost_avg);
    	planCostRating.invalidate();
    }

    private void updateHeaderDate(){
        mealPlanHeaderDay.setText(dfday.format(rightNow.getTime()).toUpperCase());
        mealPlanHeaderDate.setText(dfdate.format(rightNow.getTime()));
        mealPlanHeaderDay.invalidate();
        mealPlanHeaderDate.invalidate();
    }
    
	int getClockImageFromTime(String time_string){
		int time = (int) Integer.parseInt(time_string.substring(0, time_string.length()-5));
		if (time <= 5){
			return getResources().getIdentifier(
    				"clock05", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 10){
			return getResources().getIdentifier(
    				"clock10", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 15){
			return getResources().getIdentifier(
    				"clock15", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 20){
			return getResources().getIdentifier(
    				"clock20", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 25){
			return getResources().getIdentifier(
    				"clock25", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 30){
			return getResources().getIdentifier(
    				"clock30", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 35){
			return getResources().getIdentifier(
    				"clock35", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 40){
			return getResources().getIdentifier(
    				"clock40", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 45){
			return getResources().getIdentifier(
    				"clock45", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 50){
			return getResources().getIdentifier(
    				"clock50", 
    				"drawable", getApplicationContext().getPackageName());
		} else if (time <= 55){
			return getResources().getIdentifier(
    				"clock55", 
    				"drawable", getApplicationContext().getPackageName());
		}  
		return getResources().getIdentifier(
				"clock60", 
				"drawable", getApplicationContext().getPackageName());
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
    activity.nextDay(null);
}

public void onLeftToRightSwipe(){
    Log.i(logTag, "LeftToRightSwipe!");
    activity.previousDay(null); 
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
