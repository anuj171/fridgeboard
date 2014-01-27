package com.fridgeboard;

import com.fridgeboard.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Recipe extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recipe);

		//final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		ExpandableTextView ingredientsTextView = (ExpandableTextView) findViewById(R.id.ingredients_text);
		ingredientsTextView.setText(R.string.ingredients_text);
        
		ExpandableTextView instructionsTextView = (ExpandableTextView) findViewById(R.id.instructions_text);
		instructionsTextView.setText(R.string.instructions_text);
		
		TextView linksTextView = (TextView) findViewById(R.id.links_text);
		linksTextView.setText(R.string.links_text);
		
		TextView prepsTextView = (TextView) findViewById(R.id.preptime_text);
		prepsTextView.setText(R.string.preptime_text);
		
		TextView cookTextView = (TextView) findViewById(R.id.cooktime_text);
		cookTextView.setText(R.string.cooktime_text);
		
		TextView totalTextView = (TextView) findViewById(R.id.totaltime_text);
		totalTextView.setText(R.string.totaltime_text);
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

}
