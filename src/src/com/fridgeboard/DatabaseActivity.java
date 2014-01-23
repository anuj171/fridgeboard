package com.fridgeboard;

import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

// this is the main activity
public class DatabaseActivity extends ListActivity {

	private DataAccess.RecipeDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_database);
        
        DataAccess dataAccess = new DataAccess();
        datasource = dataAccess.new RecipeDataSource(this);
        datasource.open();

        List<DataAccess.RecipeItem> values = datasource.getAllRecipeItems();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<DataAccess.RecipeItem> adapter = new ArrayAdapter<DataAccess.RecipeItem>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
      @SuppressWarnings("unchecked")
      ArrayAdapter<DataAccess.RecipeItem> adapter = (ArrayAdapter<DataAccess.RecipeItem>) getListAdapter();
      DataAccess.RecipeItem comment = null;
      switch (view.getId()) {
      case R.id.add:
        String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
        int nextInt = new Random().nextInt(3);
        // save the new comment to the database
        comment = datasource.createRecipeItem(comments[nextInt]);
        adapter.add(comment);
        break;
      case R.id.delete:
        if (getListAdapter().getCount() > 0) {
          comment = (DataAccess.RecipeItem) getListAdapter().getItem(0);
          datasource.deleteRecipeItem(comment);
          adapter.remove(comment);
        }
        break;
      }
      adapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
}
