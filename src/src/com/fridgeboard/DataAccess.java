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
	
public class RecipeDataHelper extends SQLiteOpenHelper {

	  public static final String TABLE_RECIPES = "recipes";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";

	  private static final String DATABASE_NAME = "commments.db";
	  private static final int DATABASE_VERSION = 2;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_RECIPES + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_NAME
	      + " text not null);";

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
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
	    onCreate(db);
	  }
	} 

public class RecipeItem {
	  private long id;
	  private String name;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return name;
	  }
	} 

public class RecipeDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private RecipeDataHelper dbHelper;
	  private String[] allColumns = { RecipeDataHelper.COLUMN_ID,
	      RecipeDataHelper.COLUMN_NAME };

	  public RecipeDataSource(Context context) {
	    dbHelper = new RecipeDataHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public RecipeItem createRecipeItem(String name) {
	    ContentValues values = new ContentValues();
	    values.put(RecipeDataHelper.COLUMN_NAME, name);
	    long insertId = database.insert(RecipeDataHelper.TABLE_RECIPES, null,
	        values);
	    Cursor cursor = database.query(RecipeDataHelper.TABLE_RECIPES,
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
	    database.delete(RecipeDataHelper.TABLE_RECIPES, RecipeDataHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<RecipeItem> getAllRecipeItems() {
	    List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();

	    Cursor cursor = database.query(RecipeDataHelper.TABLE_RECIPES,
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

	  private RecipeItem cursorToRecipe(Cursor cursor) {
	    RecipeItem recipeItem = new RecipeItem();
	    recipeItem.setId(cursor.getLong(0));
	    recipeItem.setName(cursor.getString(1));
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

	  private static final String DATABASE_NAME = "meals2.db";
	  private static final int DATABASE_VERSION = 2;

//	  private static final String DATABASE_CREATE = "create table "
//		      + TABLE_RECIPES + "(" + COLUMN_ID
//		      + " integer primary key autoincrement, " + COLUMN_NAME
//		      + " text not null);";

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
