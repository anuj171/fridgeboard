package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends Activity {

    private ListView listView1;
	Calendar rightNow;
	DateFormat df;
	private TextView mealPlanHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        
        Meal meal_plan[] = new Meal[]
        {
            new Meal(R.drawable.cart, "Boiled Eggs", "Description description Description description Description description...", "Time: 15 Min"),
            new Meal(R.drawable.food, "Bread", "Description description Description description Description description...", "Time: 10 Min"),
            new Meal(R.drawable.ic_launcher, "Aloo Gobhi", "Description description Description description Description description...", "Time: 25 Min"),
            new Meal(R.drawable.cart, "Biryani", "Description description Description description Description description...", "Time: 45 Min"),
            new Meal(R.drawable.food, "Roti", "Description description Description description Description description...", "Time: 15 Min"),
            new Meal(R.drawable.ic_launcher, "Wine", "Description description Description description Description description...", "Time: 5 Min"),
        };
        
		rightNow = Calendar.getInstance();
		
		df = new SimpleDateFormat("EEE, d MMM");
		
       
        MealPlanAdapter adapter = new MealPlanAdapter(this, 
                R.layout.widget_recipe_item, meal_plan);
        
        
        listView1 = (ListView)findViewById(R.id.listView1);
         
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        
        mealPlanHeader = (TextView)header.findViewById(R.id.txtHeader);
        mealPlanHeader.setText(df.format(rightNow.getTime()));
        
//        Button nextDate = (Button) findViewById(R.id.buttonNext);
//        nextDate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Do something in response to button click
//                TextView mealPlanHeader = (TextView)v.findViewById(R.id.txtHeader);
//                rightNow.add(Calendar.DAY_OF_MONTH, 1);
//                mealPlanHeader.setText(df.format(rightNow.getTime()));
//                v.invalidate();
//            }
//        });
//
//        Button prevDate = (Button) findViewById(R.id.buttonPrevious);
//        prevDate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Do something in response to button click
//                TextView mealPlanHeader = (TextView)v.findViewById(R.id.txtHeader);
//                rightNow.add(Calendar.DAY_OF_MONTH, -1);
//                mealPlanHeader.setText(df.format(rightNow.getTime()));
//                v.invalidate();
//            }
//        });

        listView1.addHeaderView(header);
        
        listView1.setAdapter(adapter);
    }
    
    /** Called when the user touches the button */
    public void nextDay(View view) {
    	rightNow.add(Calendar.DAY_OF_MONTH, 1);
    	mealPlanHeader.setText(df.format(rightNow.getTime()));
    	mealPlanHeader.invalidate();

//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Next Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
	}
    
    /** Called when the user touches the button */
    public void previousDay(View view) {
        // Do something in response to button click
    	rightNow.add(Calendar.DAY_OF_MONTH, -1);
    	mealPlanHeader.setText(df.format(rightNow.getTime()));
    	mealPlanHeader.invalidate();

//    	// Do something in response to button click
//		new AlertDialog.Builder(this)
//	    .setTitle("Previous Day")
//	    .setMessage(df.format(rightNow.getTime()))
//	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // continue with delete
//	        }
//	     })
//	    .show();    
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
			startActivity(new Intent(this, Groceries.class));
			//Toast.makeText(this, "Ordering on Big Basket", Toast.LENGTH_SHORT).show();
//			new AlertDialog.Builder(this)
//		    .setTitle("Generate Grocery List")
//		    .setMessage("Generating groceries list for your meal plan.")
//		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) { 
//		            // continue with delete
//		        }
//		     })
//		    .show();
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, DatabaseActivity.class));
//			new AlertDialog.Builder(this)
//		    .setTitle("Meal Preferences")
//		    .setMessage("using default settings.")
//		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) { 
//		            // continue with delete
//		        }
//		     })
//		    .show();
			break;
		}
		return false;
	}
}