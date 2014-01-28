package com.fridgeboard;

import com.fridgeboard.DataAccess.DataSource;
import com.fridgeboard.DataAccess.Setting;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class MealSettingsActivity extends Activity {

	DataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_settings);
		
		DataAccess dataAccess = new DataAccess();
    	datasource = dataAccess.new DataSource(this);
        datasource.open();
        
        Button saveButton = (Button) findViewById(R.id.button1);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		EditText noOfPortions = (EditText) findViewById(R.id.editText1);
        		datasource.setSetting(Setting.NoOfPortions, noOfPortions.getText().toString());
        		
        		CheckBox skipBreakfast = (CheckBox) findViewById(R.id.checkBox1);
        		datasource.setSetting(Setting.SkipBreakfast, Boolean.toString(skipBreakfast.isChecked()));
        		
        		CheckBox skipLunch = (CheckBox) findViewById(R.id.checkBox2);
        		datasource.setSetting(Setting.SkipLunch, Boolean.toString(skipLunch.isChecked()));
        		
        		CheckBox skipDinner = (CheckBox) findViewById(R.id.checkBox3);
        		datasource.setSetting(Setting.SkipDinner, Boolean.toString(skipDinner.isChecked()));
        		
        		CheckBox skipDrinks = (CheckBox) findViewById(R.id.checkBox4);
        		datasource.setSetting(Setting.SkipDrinks, Boolean.toString(skipDrinks.isChecked()));
        		
        		CheckBox skipSnacks = (CheckBox) findViewById(R.id.checkBox5);
        		datasource.setSetting(Setting.SkipSnacks, Boolean.toString(skipSnacks.isChecked()));
        		
        		RatingBar healthRating = (RatingBar) findViewById(R.id.ratingBar1);
        		datasource.setSetting(Setting.HealthFactor, Float.toString(healthRating.getRating()));
        		
        		RatingBar tasteRating = (RatingBar) findViewById(R.id.ratingBar2);
        		datasource.setSetting(Setting.TasteFactor, Float.toString(tasteRating.getRating()));
        		
        		RatingBar costRating = (RatingBar) findViewById(R.id.ratingBar3);
        		datasource.setSetting(Setting.CostFactor, Float.toString(costRating.getRating()));
            }
        });

        
		EditText noOfPortions = (EditText) findViewById(R.id.editText1);
		noOfPortions.setText(datasource.getSetting(Setting.NoOfPortions));
		
		CheckBox skipBreakfast = (CheckBox) findViewById(R.id.checkBox1);
		skipBreakfast.setChecked(Boolean.parseBoolean(datasource.getSetting(Setting.SkipBreakfast)));
		
		CheckBox skipLunch = (CheckBox) findViewById(R.id.checkBox2);
		skipLunch.setChecked(Boolean.parseBoolean(datasource.getSetting(Setting.SkipLunch)));
		
		CheckBox skipDinner = (CheckBox) findViewById(R.id.checkBox3);
		skipDinner.setChecked(Boolean.parseBoolean(datasource.getSetting(Setting.SkipDinner)));
		
		CheckBox skipDrinks = (CheckBox) findViewById(R.id.checkBox4);
		skipDrinks.setChecked(Boolean.parseBoolean(datasource.getSetting(Setting.SkipDrinks)));
		
		CheckBox skipSnacks = (CheckBox) findViewById(R.id.checkBox5);
		skipSnacks.setChecked(Boolean.parseBoolean(datasource.getSetting(Setting.SkipSnacks)));
		
		RatingBar healthRating = (RatingBar) findViewById(R.id.ratingBar1);
		healthRating.setRating(Float.parseFloat((datasource.getSetting(Setting.HealthFactor))));
		
		RatingBar tasteRating = (RatingBar) findViewById(R.id.ratingBar2);
		tasteRating.setRating(Float.parseFloat((datasource.getSetting(Setting.TasteFactor))));
		
		RatingBar costRating = (RatingBar) findViewById(R.id.ratingBar3);
		costRating.setRating(Float.parseFloat((datasource.getSetting(Setting.CostFactor))));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meal_settings, menu);
		return true;
	}

}
