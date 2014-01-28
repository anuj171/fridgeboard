package com.fridgeboard;

import java.util.ArrayList;
import java.util.List;

import com.fridgeboard.DataAccess.DataSource;
import com.fridgeboard.DataAccess.RecipeItem;
import com.fridgeboard.RecipeListAdapter.RecipeHolder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchRecipeActivity extends Activity {

	List<RecipeItem> recipeList = null;
	RecipeItem dummyItem;
	RecipeListAdapter recipeListAdapter;
	
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
        
		final ListView recipeListView = (ListView)findViewById(R.id.listView1);
		recipeListAdapter = new RecipeListAdapter(this, R.layout.widget_recipe_item_one, recipeList);
		recipeListAdapter.dummyItem = dummyItem;
		recipeListView.setAdapter(recipeListAdapter);
		recipeListAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_recipe, menu);
		return true;
	}
	
    public void loadRecipe(View view) {
//    	int[] tag_array = (int [])view.getTag();
//	   	int position = tag_array[0];
//	
//	   	RecipeItem item = (RecipeItem) recipeListAdapter.recipeItems.get(position);
//	   	
//	   	Intent recipeAddIntent = new Intent(this, HomeScreen.class);
//	   	recipeAddIntent.putExtra(Recipe.RECIPE_ID, item.getId());
//	   	
//    	startActivity(recipeAddIntent);
	}
}
