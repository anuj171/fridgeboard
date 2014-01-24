package com.fridgeboard;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fridgeboard.DataAccess.RecipeDataHelper;
import com.fridgeboard.DataAccess.RecipeItem;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;

class GroceriesTable extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mealplanner.db";
	private static final int DATABASE_VERSION = 3;
	
	public GroceriesTable(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL("CREATE TABLE Category ( id INTEGER PRIMARY KEY, name VARCHAR( 100 ) );");
			database.execSQL("INSERT INTO [Category] ([id], [name]) VALUES (1, 'Bakery')");
			database.execSQL("INSERT INTO [Category] ([id], [name]) VALUES (2, 'Vegetables')");
			database.execSQL("INSERT INTO [Category] ([id], [name]) VALUES (3, 'Cereals')");
			database.execSQL("INSERT INTO [Category] ([id], [name]) VALUES (4, 'Pulses')");
			database.execSQL("INSERT INTO [Category] ([id], [name]) VALUES (5, 'Dairy')");
			
			database.execSQL("CREATE TABLE Ingredients ( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR( 100 ) NOT NULL, cat_id INTEGER REFERENCES Category ( id ) ); ");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (1, 'Capsicum', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (2, 'Mushroom', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (3, 'Tomatoes', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (4, 'Potatoes', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (5, 'Onion', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (6, 'French Beans', 2)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (7, 'Milk', 5)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (8, 'Butter', 5)");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id]) VALUES (9, 'Cheese', 5);");
		}
		catch(SQLException e) {
			String x = e.toString();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE Ingredients");
		db.execSQL("DROP TABLE Category");
		onCreate(db);
	}
}

class GroceriesDB {
	  private GroceriesTable dbHelper;
	  
	  String sql = "select i.id as _id, i.name as name, c.name as value from Ingredients i, Category c where i.cat_id = c.id";
	  
	  public GroceriesDB(Context context) {
	    dbHelper = new GroceriesTable(context);
	  }

	  public SQLiteDatabase getDB() throws SQLException {
	    return dbHelper.getReadableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public Cursor getItems() {
		  Cursor c = null;
		  
		  try {
			  c = getDB().rawQuery(sql, null);		  
		  }
		  catch(SQLException e) {
			  String x = e.toString();
		  }
		  int count = c.getCount();
		  
		  return c;
	  }
}

public class GroceriesData {
	
	GroceriesDB db;
	public GroceriesData(Context context) {
		db = new GroceriesDB(context);		
	}
	
	public Cursor getListForRecipesAndCategory(List<Recipe> recipes, int option) {
	    return db.getItems();	    
	}
	
	public int getTotalCount() {
		int count = 0;
		return count;
	}
}

