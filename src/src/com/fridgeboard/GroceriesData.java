package com.fridgeboard;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static final int DATABASE_VERSION = 4;
	
	public GroceriesTable(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL("CREATE TABLE Ingredients ( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) NOT NULL, cat_id INTEGER, unit VARCHAR(30) );");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (1, 'Capsicum', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (2, 'Mushroom', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (3, 'Tomatoes', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (4, 'Potatoes', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (5, 'Onion', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (6, 'French Beans', 2, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (7, 'Milk', 0, 'ml');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (8, 'Butter', 0, 'gm');");
			database.execSQL("INSERT INTO [Ingredients] ([id], [name], [cat_id], [unit]) VALUES (9, 'Cheese', 0, 'gm');");

			database.execSQL("CREATE TABLE Recipe ( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) NOT NULL);");
			database.execSQL("INSERT INTO [recipe] ([id], [name]) VALUES (1, 'rajma');");
			database.execSQL("INSERT INTO [recipe] ([id], [name]) VALUES (2, 'rice');");
			database.execSQL("INSERT INTO [recipe] ([id], [name]) VALUES (3, 'dal makhni');");

			database.execSQL("CREATE TABLE recipe_ingred ( rec_id INTEGER REFERENCES recipe (id), ing_id INTEGER REFERENCES Ingredients (id), quantity VARCHAR(30) );");
			database.execSQL("INSERT INTO [recipe_ingred] ([rec_id], [ing_id], [quantity]) VALUES (1, 1, 200);");
			database.execSQL("INSERT INTO [recipe_ingred] ([rec_id], [ing_id], [quantity]) VALUES (1, 7, 500);");
			database.execSQL("INSERT INTO [recipe_ingred] ([rec_id], [ing_id], [quantity]) VALUES (3, 1, 300);");
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
	  	  
	  public GroceriesDB(Context context) {
	    dbHelper = new GroceriesTable(context);
	  }

	  public SQLiteDatabase getDB() throws SQLException {
	    return dbHelper.getReadableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  private Cursor execSql(String sql) {
		  Cursor c = null;

		  try {
			  c = getDB().rawQuery(sql, null);		  
		  }
		  catch(SQLException e) {
			  String x = e.toString();
		  }
		  return c;
	  }

	  public Cursor getItems(int option) {
		  String sql = "select i.id as _id, i.name as name, sum(m.quantity) || ' ' || i.unit as value, 'used in ' || r.name as desc from recipe r, recipe_ingred m, ingredients i where r.id = m.rec_id and i.id = m.ing_id and i.cat_id = %s group by i.id";
		  return execSql(String.format(sql, option));
	  }
	  	  
	  public Cursor getAllItems() {
		  String sql = "select i.id as _id from Ingredients i";
		  return execSql(sql);
	  }
}

public class GroceriesData {
	
	GroceriesDB db;
	public GroceriesData(Context context) {
		db = new GroceriesDB(context);		
	}
	
	// [name, value, desc] for category
	public Cursor getListForCategory(int option) {
	    return db.getItems(option);
	}
		
	public int getTotalCount() {
		return db.getAllItems().getCount();
	}
}

