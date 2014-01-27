package com.fridgeboard;

import android.content.Context;
import android.database.Cursor;

public class GroceriesData {
	
	private DataAccess.DataSource datasource;
	public GroceriesData(Context context) {
		DataAccess dataAccess = new DataAccess();
	   	
        datasource = dataAccess.new DataSource(context);
        datasource.open();		
	}
	
	// [name, value, desc] for category
	public Cursor getListForCategory(int option) {
	    return datasource.getGroceryItems(option);
	}
		
	public int getTotalCount() {
		return datasource.getAllGroceryItems().getCount();
	}
	
	public void close()
	{
		datasource.close();
	}
}

