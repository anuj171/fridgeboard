package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends Activity {

    private ListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        
        Meal meal_plan[] = new Meal[]
        {
            new Meal(R.drawable.cart, "Boiled Eggs"),
            new Meal(R.drawable.cart, "Bread"),
            new Meal(R.drawable.cart, "Aloo Gobhi"),
            new Meal(R.drawable.cart, "Biryani"),
            new Meal(R.drawable.cart, "Roti"),
            new Meal(R.drawable.cart, "Wine"),
        };
        
		Calendar rightNow = Calendar.getInstance();
		
		DateFormat df = new SimpleDateFormat("EEE, d MMM");
		
       
        MealPlanAdapter adapter = new MealPlanAdapter(this, 
                R.layout.widget_recipe_item, meal_plan);
        
        
        listView1 = (ListView)findViewById(R.id.listView1);
         
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        
        TextView mealPlanHeader = (TextView)header.findViewById(R.id.txtHeader);
        mealPlanHeader.setText(df.format(rightNow.getTime()));
        
        listView1.addHeaderView(header);
        
        listView1.setAdapter(adapter);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		switch(item.getItemId()) {
		case R.id.action_groceries:
			//Toast.makeText(this, "Ordering on Big Basket", Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(this)
		    .setTitle("Generate Grocery List")
		    .setMessage("Generating groceries list for your meal plan.")
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .show();
			break;
		case R.id.action_settings:
			new AlertDialog.Builder(this)
		    .setTitle("Meal Preferences")
		    .setMessage("using default settings.")
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .show();
			break;
		}
		return false;
	}
}