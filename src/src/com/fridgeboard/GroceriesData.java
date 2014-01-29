package com.fridgeboard;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

class GroceriesDb {
	
	private DataAccess.DataSource datasource;
	public GroceriesDb(Context context) {
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

public class GroceriesData extends SimpleCursorAdapter {

	private int mSelectedPosition;
	private Cursor items;
	private Context context;
	private int layout;
	
	public GroceriesData(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.context = context;
	    this.layout = layout;
	}
	
}

