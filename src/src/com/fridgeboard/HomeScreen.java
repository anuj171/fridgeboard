package com.fridgeboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class HomeScreen extends Activity {

    private ListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        
        Meal meal_plan[] = new Meal[]
        {
            new Meal(R.drawable.cart, "Cloudy"),
            new Meal(R.drawable.cart, "Cloudy"),
            new Meal(R.drawable.cart, "Cloudy"),
            new Meal(R.drawable.cart, "Cloudy"),
            new Meal(R.drawable.cart, "Cloudy"),
            new Meal(R.drawable.cart, "Cloudy"),
        };
        
        MealPlanAdapter adapter = new MealPlanAdapter(this, 
                R.layout.widget_recipe_item, meal_plan);
        
        
        listView1 = (ListView)findViewById(R.id.listView1);
         
        View header = (View)getLayoutInflater().inflate(R.layout.home_screen_header, null);
        listView1.addHeaderView(header);
        
        listView1.setAdapter(adapter);
    }
}