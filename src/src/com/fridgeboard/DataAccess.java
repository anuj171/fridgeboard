package com.fridgeboard;

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
}
