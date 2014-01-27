package com.fridgeboard;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataAccess {
	private static final String DATABASE_NAME = "meals2.db";
	private static final int DATABASE_VERSION = 3;
	
public class RecipeDataHelper extends SQLiteOpenHelper {

	  public static final String TABLE_RECIPE = "recipe";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_IMAGE = "image";
	  public static final String COLUMN_PREP_TIME = "prep_time";
	  public static final String COLUMN_COOKING_TIME = "cooking_time";
	  public static final String COLUMN_TOTAL_TIME = "total_time";
	  public static final String COLUMN_TASTE_RATING = "taste_rating";
	  public static final String COLUMN_HEALTH_RATING = "health_rating";
	  public static final String COLUMN_INGREDIENTS = "ingredients";
	  public static final String COLUMN_INSTRUCTIONS = "instructions";
	  public static final String COLUMN_LINKS = "links";

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table " + TABLE_RECIPE + "(" 
		  + COLUMN_ID + " integer primary key autoincrement, " 
		  + COLUMN_NAME + " text not null,"
	      + COLUMN_IMAGE + " text, "
	      + COLUMN_PREP_TIME + " text, "
	      + COLUMN_COOKING_TIME + " text, "
	      + COLUMN_TOTAL_TIME + " text, "
	      + COLUMN_TASTE_RATING + " float, "
	      + COLUMN_HEALTH_RATING + " float, "
	      + COLUMN_INGREDIENTS + " text, "
	      + COLUMN_INSTRUCTIONS + " text, "
	      + COLUMN_LINKS + " links"
	      + ");";

	  public RecipeDataHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(RecipeDataHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
	    onCreate(db);
	  }
	} 

public class RecipeItem {
	  private long _id;
	  private String _name;
	  private String _image;
	  private String _prepTime;
	  private String _cookingTime;
	  private String _totalTime;
	  private float _tasteRating;
	  private float _healthRating;
	  private String _ingredients;
	  private String _instructions;
	  private String _links;
	  
	  public RecipeItem(
		  long id,
		  String name,
		  String image,
		  String prepTime,
		  String cookingTime,
		  String totalTime,
		  float tasteRating,
		  float healthRating,
		  String ingredients,
		  String instructions,
		  String links)
	  {
		 _id = id;
		 _name = name;
		 _image = image;
		 _prepTime = prepTime;
		 _cookingTime = cookingTime;
		 _totalTime = totalTime;
		 _tasteRating = tasteRating;
		 _healthRating = healthRating;
		 _ingredients = ingredients;
		 _instructions = instructions;
		 _links = links;
	  }
	  
	  public long getId() { return _id; }
	  public String getName() { return _name; }
	  public String getImage() { return _image; }
	  public String getPrepTime() { return _prepTime; }
	  public String getCookingTime() { return _cookingTime; }
	  public String getTotalTime() { return _totalTime; }
	  public float getTasteRating() { return _tasteRating; }
	  public float getHealthRating() { return _healthRating; }
	  public String getIngredients() { return _ingredients; }
	  public String getInstructions() { return _instructions; }
	  public String getLinks() { return _links; }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return _name;
	  }
	} 

public class RecipeDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private RecipeDataHelper dbHelper;
	  
	  private String[] allColumns = { 
			  RecipeDataHelper.COLUMN_ID,
			  RecipeDataHelper.COLUMN_NAME,
			  RecipeDataHelper.COLUMN_IMAGE,
			  RecipeDataHelper.COLUMN_PREP_TIME,
			  RecipeDataHelper.COLUMN_COOKING_TIME,
			  RecipeDataHelper.COLUMN_TOTAL_TIME,
			  RecipeDataHelper.COLUMN_TASTE_RATING,
			  RecipeDataHelper.COLUMN_HEALTH_RATING,
			  RecipeDataHelper.COLUMN_INGREDIENTS,
			  RecipeDataHelper.COLUMN_INSTRUCTIONS,
			  RecipeDataHelper.COLUMN_LINKS};

	  public RecipeDataSource(Context context) {
	    dbHelper = new RecipeDataHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public RecipeItem createRecipeItem(
			  String name,
			  String image,
			  String prepTime,
			  String cookingTime,
			  String totalTime,
			  float tasteRating,
			  float healthRating,
			  String ingredients,
			  String instructions,
			  String links) {
		  
		    ContentValues values = new ContentValues();
		    values.put(RecipeDataHelper.COLUMN_NAME, name);
		    values.put(RecipeDataHelper.COLUMN_IMAGE, image);
		    values.put(RecipeDataHelper.COLUMN_PREP_TIME, prepTime);
		    values.put(RecipeDataHelper.COLUMN_COOKING_TIME, cookingTime);
		    values.put(RecipeDataHelper.COLUMN_TOTAL_TIME, totalTime);
		    values.put(RecipeDataHelper.COLUMN_TASTE_RATING, tasteRating);
		    values.put(RecipeDataHelper.COLUMN_HEALTH_RATING, healthRating);
		    values.put(RecipeDataHelper.COLUMN_INGREDIENTS, ingredients);
		    values.put(RecipeDataHelper.COLUMN_INSTRUCTIONS, instructions);
		    values.put(RecipeDataHelper.COLUMN_LINKS, links);
		    
		    long insertId = database.insert(RecipeDataHelper.TABLE_RECIPE, null,
		        values);
		    Cursor cursor = database.query(RecipeDataHelper.TABLE_RECIPE,
		        allColumns, RecipeDataHelper.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    RecipeItem newRecipeItem = cursorToRecipe(cursor);
		    cursor.close();
		    return newRecipeItem;
	  }

	  public void deleteRecipeItem(RecipeItem recipeItem) {
	    long id = recipeItem.getId();
	    System.out.println("Recipe deleted with id: " + id);
	    database.delete(RecipeDataHelper.TABLE_RECIPE, RecipeDataHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<RecipeItem> getAllRecipeItems() {
	    List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();

	    Cursor cursor = database.query(RecipeDataHelper.TABLE_RECIPE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      RecipeItem recipeItem = cursorToRecipe(cursor);
	      recipeItems.add(recipeItem);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return recipeItems;
	  }
	  

	  public RecipeItem getRecipeItem(long id) {
	    Cursor cursor = database.query(RecipeDataHelper.TABLE_RECIPE,
	        allColumns, RecipeDataHelper.COLUMN_ID + " = " + id, null, null, null, null);

	    cursor.moveToFirst();

	    RecipeItem recipeItem = null;
		if (!cursor.isAfterLast())
		{
	      recipeItem = cursorToRecipe(cursor); 
	    }
		
	    // make sure to close the cursor
	    cursor.close();
	    return recipeItem;
	  }

	  private RecipeItem cursorToRecipe(Cursor cursor) {
	    RecipeItem recipeItem = 
	    	new RecipeItem(
    			cursor.getLong(0),
    			cursor.getString(1),
    			cursor.getString(2),
    			cursor.getString(3),
    			cursor.getString(4),
    			cursor.getString(5),
    			cursor.getFloat(6),
    			cursor.getFloat(7),
    			cursor.getString(8),
    			cursor.getString(9),
    			cursor.getString(10));

	    return recipeItem;
	  }
	} 

public class MealsDataHelper extends SQLiteOpenHelper {

	  public static final String TABLE_MEALS = "meals";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_DATE = "date";
	  public static final String COLUMN_CATEGORY = "category";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_DESC = "desc";
	  public static final String COLUMN_TIMETAKEN = "timetaken";
	  public static final String COLUMN_RECIPE_ID = "recipe_id";

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table " + TABLE_MEALS + "(" 
         + COLUMN_ID + " integer primary key autoincrement, " 
         + COLUMN_DATE + " text not null, " 
         + COLUMN_CATEGORY + " text not null, " 
         + COLUMN_NAME + " text not null, " 
         + COLUMN_DESC + " text not null, " 
         + COLUMN_TIMETAKEN + " text not null, " 
         + COLUMN_RECIPE_ID + " text not null);";

	  public MealsDataHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(RecipeDataHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
	    onCreate(db);
	  }
	} 

public class MealItem {
	  public long id;
	  public String date;
	  public String category;
	  public String name;
	  public String desc;
	  public String timetaken;
	  public String recipe_id;

	  public MealItem(long id, String date, String category, String name, String desc, String timetaken, String recipe_id) {
		    this.id = id;
		    this.date = date;
		    this.category = category;
		    this.name = name;
		    this.desc = desc;
		    this.timetaken = timetaken;
		    this.recipe_id = recipe_id;
		  }

	  public long getId() {
	    return id;
	  }
	  public void setId(long id) {
	    this.id = id;
	  }
	} 

public class MealsDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MealsDataHelper dbHelper;
	  private String[] allColumns = { 
		  MealsDataHelper.COLUMN_ID,
		  MealsDataHelper.COLUMN_DATE,
		  MealsDataHelper.COLUMN_CATEGORY,
		  MealsDataHelper.COLUMN_NAME,
		  MealsDataHelper.COLUMN_DESC,
		  MealsDataHelper.COLUMN_TIMETAKEN,
		  MealsDataHelper.COLUMN_RECIPE_ID,
      };

	  public MealsDataSource(Context context) {
	    dbHelper = new MealsDataHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public MealItem createMealItem(String date, String category, String name, String desc, String timetaken, String recipe_id) {
	    ContentValues values = new ContentValues();
	    values.put(MealsDataHelper.COLUMN_DATE, date);
	    values.put(MealsDataHelper.COLUMN_CATEGORY, category);
	    values.put(MealsDataHelper.COLUMN_NAME, name);
	    values.put(MealsDataHelper.COLUMN_DESC, desc);
	    values.put(MealsDataHelper.COLUMN_TIMETAKEN, timetaken);
	    values.put(MealsDataHelper.COLUMN_RECIPE_ID, recipe_id);
	    long insertId = database.insert(MealsDataHelper.TABLE_MEALS, null,
	        values);
	    System.out.println(insertId);
	    Cursor cursor = database.query(MealsDataHelper.TABLE_MEALS,
	        allColumns, MealsDataHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    MealItem newMealItem = cursorToMeal(cursor);
	    cursor.close();
	    return newMealItem;
	  }
	  
	  public MealItem getMealItemByID(long meal_id){
		    Cursor cursor = database.query(MealsDataHelper.TABLE_MEALS,
			        allColumns, MealsDataHelper.COLUMN_ID + " = " + meal_id, null,
			        null, null, null);
		    cursor.moveToFirst();
		    MealItem newMealItem = cursorToMeal(cursor);
		    cursor.close();
		    return newMealItem;
	  }

	  public void deleteMealItem(MealItem mealItem) {
	    long id = mealItem.getId();
	    System.out.println("Meal deleted with id: " + id);
	    database.delete(MealsDataHelper.TABLE_MEALS, MealsDataHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<MealItem> getAllMealItemsForADate(String date) {
	    List<MealItem> mealItems = new ArrayList<MealItem>();

	    Cursor cursor = database.query(MealsDataHelper.TABLE_MEALS,
	        allColumns, MealsDataHelper.COLUMN_DATE +" = \""+date+"\"", null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      MealItem mealItem = cursorToMeal(cursor);
	      mealItems.add(mealItem);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    System.out.println("Meal numbers: " + mealItems.size());
	    return mealItems;
	  }

	  private MealItem cursorToMeal(Cursor cursor) {
	    MealItem mealItem = new MealItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
	    return mealItem;
	  }
	} 

}
