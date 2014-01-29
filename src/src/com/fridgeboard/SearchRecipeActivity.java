package com.fridgeboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fridgeboard.DataAccess.DataSource;
import com.fridgeboard.DataAccess.RecipeItem;
import com.fridgeboard.RecipeListAdapter.RecipeHolder;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRecipeActivity extends Activity {

	List<RecipeItem> recipeList = null;
	List<RecipeItem> fullRecipeList = null;
	List<String> recipeNames = null;
	RecipeItem dummyItem;
	RecipeListAdapter recipeListAdapter;
	ArrayAdapter<?> autoAdapter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) findViewById(R.id.titletext);
        titleText.setText(R.string.title_activity_search_recipe);
		
		setContentView(R.layout.activity_search_recipe);
		
		DataAccess dataAccess = new DataAccess();
    	DataSource datasource = dataAccess.new DataSource(this);
        datasource.open();
        fullRecipeList = datasource.getAllRecipeItems();
        dummyItem = datasource.dummyItem;
        datasource.close();
        
        recipeNames = new ArrayList<String>();
        
        SortFullRecipeList();
        UpdateRecipeList("");
        
		final ListView recipeListView = (ListView)findViewById(R.id.listView1);
		recipeListAdapter = new RecipeListAdapter(this, R.layout.widget_search_recipe, recipeList);
		recipeListAdapter.dummyItem = dummyItem;
		recipeListView.setAdapter(recipeListAdapter);
		
		recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  final RecipeItem item = (RecipeItem) parent.getItemAtPosition(position);
		    	  SendToHomeScreen(item);
		      }
		    });
		
		AutoCompleteTextView edtTitle = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		edtTitle.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				Editable box = (Editable)arg0;            	
        		UpdateRecipeList(box.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
        edtTitle.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
            		if(arg2.getAction() == KeyEvent.ACTION_DOWN){
	            		
            		}
            		return false;
                }
        });
        
        String[] data = recipeNames.toArray(new String[recipeNames.size()]);
        autoAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_dropdown_item_1line, data);
        edtTitle.setAdapter(autoAdapter);
        edtTitle.setThreshold(1);
        UpdateAdapters();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.search_recipe, menu);
		return false;
	}
	
	
	
	private void SortFullRecipeList(){
		Collections.sort(fullRecipeList, new Comparator<RecipeItem>(){
        	public int compare(RecipeItem item1, RecipeItem item2){
        		return item1.getName().compareToIgnoreCase(item2.getName());
        	}
        });
	}
	
	private void UpdateRecipeList(String text){
		if(text == null || text == "") {
			recipeList = new ArrayList<RecipeItem>(fullRecipeList);
			recipeNames.clear();
			for(int i=0;i<recipeList.size();++i){
				recipeNames.add(recipeList.get(i).getName());
			}
		}
		else {
			recipeList.clear();
			recipeNames.clear();
			for(int i=0;i<fullRecipeList.size();++i){
				RecipeItem item = fullRecipeList.get(i);
				String name = item.getName();				
				if(name.toLowerCase().contains(text.toLowerCase())){
					recipeList.add(item);
					recipeNames.add(name);
				}
			}
		}
		UpdateAdapters();
	}
	
	private void UpdateAdapters(){
		if(recipeListAdapter!=null)
		recipeListAdapter.notifyDataSetChanged();
//		if(autoAdapter!=null)
//		autoAdapter.notifyDataSetChanged();
	}
    public void SendToHomeScreen(RecipeItem item){
    	
        Intent recipeAddIntent = new Intent(this, HomeScreen.class);
	   	recipeAddIntent.putExtra(Recipe.RECIPE_ID, item.getId());
	   	
    	startActivity(recipeAddIntent);
    }
}
