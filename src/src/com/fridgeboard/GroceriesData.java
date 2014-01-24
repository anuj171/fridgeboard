package com.fridgeboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class GroceriesTable extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mealplanner.db";
	private static final int DATABASE_VERSION = 1;
	
	public GroceriesTable(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
	    database.execSQL("CREATE TABLE Ingredients (" +
	    		"id   INTEGER         PRIMARY KEY AUTOINCREMENT, " +
	    		"name VARCHAR( 100 )  NOT NULL);");
	    
	    database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (1, 'Capsicum', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (2, 'Mushroom', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (3, 'Tomatoes', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (4, 'Potatoes', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (5, 'Onion', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (6, 'French Beans', 2); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (7, 'Milk', 5); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (8, 'Butter', 5); INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (9, 'Cheese', 5);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}

class GroceriesDB {
	  private SQLiteDatabase database;
	  private GroceriesTable dbHelper;
	  
	  public GroceriesDB(Context context) {
	    dbHelper = new GroceriesTable(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getReadableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
}

public class GroceriesData {
	
	Map<String, List<String>> groceries;
	
	public List<String> getListForRecipesAndCategory(List<Recipe> recipes, int option) {
		Map<String, List<String>> map = getGroceriesForRecipes(null);
	    return map.get(Groceries.Categories[option]);	    
	}
	
	public int getTotalCount() {
		int count = 0;
		for(String cat : groceries.keySet()) {
			count += ((List<String>)groceries.get(cat)).size();			
		}
		return count;
	}
	
	public Map<String, List<String>> getGroceriesForRecipes(List<Recipe> recipes) {
		if (groceries == null) {
			groceries = new HashMap<String, List<String>>();
			
			for(int i=0; i<Groceries.Categories.length; i++)
				groceries.put(Groceries.Categories[i], new ArrayList<String>());
			
			List<String> vegetables = groceries.get(Groceries.Categories[2]);
			vegetables.add("Capsicum");
			vegetables.add("Vegetables");
			vegetables.add("Something");
			vegetables.add("Else");
		}
		
		return groceries;		
	}
}

