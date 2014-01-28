package com.fridgeboard;

import java.util.ArrayList;
import java.util.List;

import com.fridgeboard.DataAccess.DataSource;
import com.fridgeboard.DataAccess.RecipeItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchRecipeActivity extends Activity {

	List<RecipeItem> recipeList = null;
	RecipeItem dummyItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_recipe);
		
		DataAccess dataAccess = new DataAccess();
    	DataSource datasource = dataAccess.new DataSource(this);
        datasource.open();
        recipeList = datasource.getAllRecipeItems(); 
        dummyItem = datasource.dummyItem;
        datasource.close();
        
		ListView recipeListView = (ListView)findViewById(R.id.listView1);
		RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this,R.layout.activity_search_recipe, recipeList);
		recipeListAdapter.dummyItem = dummyItem;
		recipeListView.setAdapter(recipeListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_recipe, menu);
		return true;
	}
}
