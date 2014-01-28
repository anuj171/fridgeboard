package com.fridgeboard;

import com.fridgeboard.DataAccess.DataSource;
import com.fridgeboard.DataAccess.RecipeItem;
import com.fridgeboard.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Recipe extends Activity {

	public static String RECIPE_ID = "RECIPE_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recipe);

		//final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		long recipeId = 1;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getLong(RECIPE_ID);
		}
		
		LoadView(recipeId);
	}
	
	private void LoadView(long recipeId)
	{
		DataAccess dataAccess = new DataAccess();
    	DataSource datasource = dataAccess.new DataSource(this);
        datasource.open();
        RecipeItem recipeItem = datasource.getRecipeItem(recipeId);
        datasource.close();
        
        TextView nameView = (TextView) findViewById(R.id.recipe_name);
        nameView.setText(recipeItem != null ? recipeItem.getName(): "Rajma Masala");
        
        TextView descView = (TextView) findViewById(R.id.recipe_desc);
        descView.setText(recipeItem != null ? recipeItem.getDescription() : "Red kidney beans cooked in tomatoes, onions and spices.");
        
        ImageView recipeImageView = (ImageView) findViewById(R.id.recipe_image);
        recipeImageView.setImageResource(
        		getResources().getIdentifier(
    				recipeItem != null ? recipeItem.getImage() : "punjabirajma", 
    				"drawable", getApplicationContext().getPackageName()));
        
		TextView prepsTextView = (TextView) findViewById(R.id.preptime_text);
		prepsTextView.setText(recipeItem != null ? recipeItem.getPrepTime() : "9 mins" );
		
		TextView cookTextView = (TextView) findViewById(R.id.cooktime_text);
		cookTextView.setText(recipeItem != null ? recipeItem.getCookingTime() : "45 mins" );
		
		TextView totalTextView = (TextView) findViewById(R.id.totaltime_text);
		totalTextView.setText(recipeItem != null ? recipeItem.getTotalTime() : "54 mins" );
		
		RatingBar tasteRating = (RatingBar) findViewById(R.id.tasteRating);
		tasteRating.setRating(recipeItem != null ? recipeItem.getTasteRating() : 4);
		
		RatingBar healthRating = (RatingBar) findViewById(R.id.healthRating);
		healthRating.setRating(recipeItem != null ? recipeItem.getHealthRating() : (float)3.5);
		
		ExpandableTextView ingredientsTextView = (ExpandableTextView) findViewById(R.id.ingredients_text);
		ingredientsTextView.setText(recipeItem != null ? recipeItem.getIngredients() : "Rajma(Red Kidney Bean) - 3/4 cup\nGaram Masala powder- 1/4 tsp(optional)\nKasoori Methi - 1 generous pinch\nCream / Milk - 1 tbsp(optional)\nCoriander leaves - 2 tsp chopped\nSalt - to taste\nOil - 2 tsp\nJeera - 1/2 tsp\nCoriander seeds - 2 tsp\nRed Chillies - 2\nOnion - 1 medium sized\nTomatoes - 2 medium sized\nGarlic - 4 cloves\nGinger - 1/2 inch piece\nCinnamon - 1/4 inch piece\nCloves - 2");
        
		ExpandableTextView instructionsTextView = (ExpandableTextView) findViewById(R.id.instructions_text);
		instructionsTextView.setText(recipeItem != null ? recipeItem.getInstructions()
				: "1. Soak rajma overnight atleast for 8 hrs, rinse it in water for 2-3 times.Then pressure cook along with water till immersing level until soft(I did for 7 whistles, depends on variety of rajma), Set aside.Reserve the drained rajma cooked water for later use.Heat oil in a pan add the ingredients listed under to saute and grind.\n"
				  + "2. Cook till raw smell of tomatoes leave and is slightly mushy. Cool down and then transfer it to a mixer.\n"
				  + "3. Grind it to smooth paste without adding water,set aside. Heat oil in a pan - temper jeera, let it splutter.Then add the onion tomato paste.\n"
				  + "4. Then add garam masala and saute for 2mins then add reserved rajma cooked water and let it boil for mins. Dilute it well as it has to cook for more time.Then add cooked rajma and required salt.\n"
				  + "5. Cover with a lid and let the gravy thicken and let rajma absorb the gravy well.Add milk/cream, give a quick stir and cook for 2mins. Finally garnish with coriander leaves and kasoori methi, quick stir and switch off.");
		
		TextView linksTextView = (TextView) findViewById(R.id.links_text);
		linksTextView.setText(recipeItem != null ? recipeItem.getLinks() : "http://www.vegrecipesofindia.com/rajma-masala-recipe-restaurant-style\nhttp://cooks.ndtv.com/recipe/show/rajma-233367");	
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

}
