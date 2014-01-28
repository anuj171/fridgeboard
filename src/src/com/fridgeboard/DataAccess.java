package com.fridgeboard;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.util.Log;

public class DataAccess {
	private static final String DATABASE_NAME = "meals_database.db";
	private static final int DATABASE_VERSION = 19;
	
	  public enum RecipeCategory{
		  BreakFast,
		  LunchOrDinnerMainDish,
		  Snacks,
		  Desserts,
		  Drinks,
		  LunchOrDinnerSideDish,
	  }
	  
	  public enum Setting {
		  NoOfPortions,
		  SkipBreakfast,
		  SkipLunch,
		  SkipDinner,
		  SkipDrinks,
		  SkipSnacks,
		  HealthFactor,
		  TasteFactor,
		  CostFactor
	  }
	  
public class DataHelper extends SQLiteOpenHelper {

	  public static final String TABLE_RECIPE = "recipe";
	  public static final String RECIPE_COLUMN_ID = "_id";
	  public static final String RECIPE_COLUMN_NAME = "name";
	  public static final String RECIPE_COLUMN_DESCRIPTION = "desc";
	  public static final String RECIPE_COLUMN_IMAGE = "image";
	  public static final String RECIPE_COLUMN_PREP_TIME = "prep_time";
	  public static final String RECIPE_COLUMN_COOKING_TIME = "cooking_time";
	  public static final String RECIPE_COLUMN_TOTAL_TIME = "total_time";
	  public static final String RECIPE_COLUMN_TASTE_RATING = "taste_rating";
	  public static final String RECIPE_COLUMN_HEALTH_RATING = "health_rating";
	  public static final String RECIPE_COLUMN_INGREDIENTS = "ingredients";
	  public static final String RECIPE_COLUMN_INSTRUCTIONS = "instructions";
	  public static final String RECIPE_COLUMN_LINKS = "links";
	  public static final String RECIPE_COLUMN_CATEGORY = "category";


	  // Database creation sql statement
	  private static final String RECIPE_DATABASE_CREATE = "create table " + TABLE_RECIPE + "(" 
		  + RECIPE_COLUMN_ID + " integer primary key autoincrement, " 
		  + RECIPE_COLUMN_NAME + " text not null, "
		  + RECIPE_COLUMN_DESCRIPTION + " text, "
	      + RECIPE_COLUMN_IMAGE + " text, "
	      + RECIPE_COLUMN_PREP_TIME + " text, "
	      + RECIPE_COLUMN_COOKING_TIME + " text, "
	      + RECIPE_COLUMN_TOTAL_TIME + " text, "
	      + RECIPE_COLUMN_TASTE_RATING + " float, "
	      + RECIPE_COLUMN_HEALTH_RATING + " float, "
	      + RECIPE_COLUMN_INGREDIENTS + " text, "
	      + RECIPE_COLUMN_INSTRUCTIONS + " text, "
	      + RECIPE_COLUMN_LINKS + " text, "
	      + RECIPE_COLUMN_CATEGORY + " integer "
	      + ");";

	  public static final String TABLE_MEALS = "meals";
	  public static final String MEALS_COLUMN_ID = "_id";
	  public static final String MEALS_COLUMN_DATE = "date";
	  public static final String MEALS_COLUMN_CATEGORY = "category";
	  public static final String MEALS_COLUMN_RECIPE_ID = "recipe_id";
	  
	 // Database creation sql statement
	  private static final String MEALS_DATABASE_CREATE = "create table " + TABLE_MEALS + "(" 
	         + MEALS_COLUMN_ID + " integer primary key autoincrement, " 
	         + MEALS_COLUMN_DATE + " text not null, " 
	         + MEALS_COLUMN_CATEGORY + " text not null, " 
	         + MEALS_COLUMN_RECIPE_ID + " INTEGER REFERENCES "+TABLE_RECIPE+" ("+ RECIPE_COLUMN_ID +"));";
//	         + MEALS_COLUMN_RECIPE_ID + " text not null);";
	  
	  public static final String INGREDIENTS_TABLE = "ingredients";
	  public static final String RECIPE_INGRED_TABLE = "recipe_ingred";

	  
	  public static final String TABLE_SETTINGS = "settings";
	  public static final String SETTINGS_COLUMN_ID = "id";
	  public static final String SETTINGS_COLUMN_TYPE = "type";
	  public static final String SETTINGS_COLUMN_VALUE = "value";
	  
	  // Database creation sql statement
	  private static final String SETTINGS_DATABASE_CREATE = "create table " + TABLE_SETTINGS + "("
		  + SETTINGS_COLUMN_ID + " integer primary key autoincrement, "
		  + SETTINGS_COLUMN_TYPE + " integer, "
		  + SETTINGS_COLUMN_VALUE + " text"
	      + ");";

	  DataSource source;
		  
	  public DataHelper(Context context, DataSource src) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    source = src;
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(RECIPE_DATABASE_CREATE);
	    database.execSQL(MEALS_DATABASE_CREATE);
		database.execSQL("CREATE TABLE "+ INGREDIENTS_TABLE +" ( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) NOT NULL, cat_id INTEGER, unit VARCHAR(30) );");
		database.execSQL("CREATE TABLE "+ RECIPE_INGRED_TABLE +" ( rec_id INTEGER REFERENCES "+TABLE_RECIPE+" ("+ RECIPE_COLUMN_ID +"), ing_id INTEGER REFERENCES Ingredients (id), quantity VARCHAR(30) );");
		database.execSQL(SETTINGS_DATABASE_CREATE);
		
		source.fillData(database);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DataHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    dropTables(db);
	    onCreate(db);
	  }
	  
	  private void dropTables(SQLiteDatabase db)
	  {
		  db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
		  db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
		  db.execSQL("DROP TABLE IF EXISTS " + INGREDIENTS_TABLE);
		  db.execSQL("DROP TABLE IF EXISTS " + RECIPE_INGRED_TABLE);
		  db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
	  }
	} 

public class DataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private DataHelper dbHelper;
	  
	  private String[] recipeAllColumns = { 
			  DataHelper.RECIPE_COLUMN_ID,
			  DataHelper.RECIPE_COLUMN_NAME,
			  DataHelper.RECIPE_COLUMN_DESCRIPTION,
			  DataHelper.RECIPE_COLUMN_IMAGE,
			  DataHelper.RECIPE_COLUMN_PREP_TIME,
			  DataHelper.RECIPE_COLUMN_COOKING_TIME,
			  DataHelper.RECIPE_COLUMN_TOTAL_TIME,
			  DataHelper.RECIPE_COLUMN_TASTE_RATING,
			  DataHelper.RECIPE_COLUMN_HEALTH_RATING,
			  DataHelper.RECIPE_COLUMN_INGREDIENTS,
			  DataHelper.RECIPE_COLUMN_INSTRUCTIONS,
			  DataHelper.RECIPE_COLUMN_LINKS,
			  DataHelper.RECIPE_COLUMN_CATEGORY};
	  
	  private String[] mealsAllColumns = { 
			  DataHelper.MEALS_COLUMN_ID,
			  DataHelper.MEALS_COLUMN_DATE,
			  DataHelper.MEALS_COLUMN_CATEGORY,
			  DataHelper.MEALS_COLUMN_RECIPE_ID,
	      };
	  
	  private String[] settingsAllColumns = { 
			  DataHelper.SETTINGS_COLUMN_ID,
			  DataHelper.SETTINGS_COLUMN_TYPE,
			  DataHelper.SETTINGS_COLUMN_VALUE,
	      };  

	  public DataSource(Context context) {
	    dbHelper = new DataHelper(context, this);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void fillData(SQLiteDatabase databse)
	  {
		  database = databse;
		  fillRecipe();
		  fillIngredients();
		  fillRecipeIngrRelation();
		  fillSettings();
	  }
	  
	  public void fillIngredients()
	  {
		  int category = 0;
		  		  
		  // Vegetables
		  createIngredient("Onion", category, "gm"); //1
		  createIngredient("Potatoes", category, "gm");
		  createIngredient("Capsicum", category, "gm");
		  createIngredient("Spinach", category, "gm");
		  createIngredient("Methi Leaves", category, "gm");
		  createIngredient("Red Carrot", category, "gm"); //6
		  createIngredient("Tomatoes", category, "gm");
		  createIngredient("Mushrooms", category, "gm");
		  createIngredient("Coriander", category, "gm");
		  createIngredient("Green Chillies", category, "gm");
		  createIngredient("Cucumber", category, "gm"); //11
		  createIngredient("Cauliflower", category, "gm");
		  
		  // Staples
		  category = 1;
		  createIngredient("Garam Masala Powder", category, "gm"); //13
		  createIngredient("Kasoori methi", category, "gm");
		  createIngredient("Salt", category, "gm");
		  createIngredient("Jeera", category, "gm");
		  createIngredient("Vegetable Oil", category, "lt"); //17
		  
		  // Pulses
		  category = 2;
		  createIngredient("Rajma", category, "gm"); //18
		  createIngredient("Tur Daal", category, "gm");
		  createIngredient("Chhole", category, "gm");
		  createIngredient("Chane", category, "gm"); //21
		  createIngredient("Chana Daal", category, "gm");
		  createIngredient("Urad Daal", category, "gm");
		  createIngredient("Moong Daal", category, "gm"); //24
		  
		  // Dairy
		  category = 3;
		  createIngredient("Milk", category, "ml"); //25
		  createIngredient("Curd", category, "gm");
		  createIngredient("Paneer", category, "gm");
		  createIngredient("Cheese", category, "gm");
		  createIngredient("Butter", category, "gm"); //29
		  
		  // Other
		  category = 4;
		  createIngredient("Wheat Bread", category, "no"); //30
		  createIngredient("Pav", category, "no");
		  createIngredient("Biscuits", category, "no");
		  createIngredient("Bread Sticks", category, "no");
		  
		  
		  createIngredient("Sugar", 1, "gm"); //34
		  createIngredient("Wheat Flour", 1, "gm");
		  createIngredient("Eggs", 4, "no");
		  createIngredient("Basmati Rice", 1, "gm"); //37
		  createIngredient("Wheat Flour", 1, "gm");
		  createIngredient("French Beans", 0, "gm");
		  createIngredient("Green Peas", 0, "gm");
	  }
	  
	  public void fillRecipeIngrRelation()
	  {
		  // rajma masala
		  createRelation(1, 18, 500);
		  createRelation(1, 13, 100);
		  createRelation(1, 14, 50);
		  createRelation(1, 15, 25);
		  createRelation(1, 16, 25);
		  createRelation(1, 17, 50);
		  createRelation(1, 9, 25);
		  createRelation(1, 10, 10);
		  createRelation(1, 1, 100);
		  
		  // chole
		  createRelation(2, 17, 100);
		  createRelation(2, 1, 200);
		  createRelation(2, 7, 100);
		  createRelation(2, 10, 50);
		  createRelation(2, 9, 25);
		  createRelation(2, 20, 250);
		  
		  // naan
		  createRelation(3, 34, 50);
		  createRelation(3, 35, 500);
		  createRelation(3, 15, 50);		
		  
		  // eggs
		  createRelation(4, 36, 2);
		  createRelation(4, 1, 50);
		  
		  // jeera rice
		  createRelation(5, 37, 200);
		  createRelation(5, 17, 50);
		  createRelation(5, 1, 50);
		  createRelation(5, 16, 50);
		  createRelation(5, 9, 10);
		  
		  // aloo paratha
		  createRelation(6, 38, 200);
		  createRelation(6, 15, 20);
		  createRelation(6, 17, 50);
		  createRelation(6, 2, 100);
		  createRelation(6, 16, 10);
		  createRelation(6, 13, 20);
		  createRelation(6, 9, 50);
		  
		  // mixveg
		  createRelation(7, 12, 100);
		  createRelation(7, 6, 50);
		  createRelation(7, 2, 100);
		  createRelation(7, 39, 50);
		  createRelation(7, 3, 50);
		  createRelation(7, 40, 100);
		  createRelation(7, 1, 50);
		  createRelation(7, 10, 20);
		  createRelation(7, 9, 20);
		  createRelation(7, 13, 20);
		  createRelation(7, 16, 20);
		  createRelation(7, 7, 50);
		  createRelation(7, 27, 50);
	  }
	  	  
	  public void fillRecipe()
	  {
		  	  // 1 Rajma Masala
			  createRecipeItem(
					  "Rajma Masala", "Red kidney beans cooked in tomatoes, onions and spices.",
					  "punjabirajma",
					  "9 mins", "45 mins", "54 mins",
					  (float)4.5, (float)3.5,
					  "Rajma(Red Kidney Bean) - 3/4 cup\nGaram Masala powder- 1/4 tsp(optional)\nKasoori Methi - 1 generous pinch\nCream / Milk - 1 tbsp(optional)\nCoriander leaves - 2 tsp chopped\nSalt - to taste\nOil - 2 tsp\nJeera - 1/2 tsp\nCoriander seeds - 2 tsp\nRed Chillies - 2\nOnion - 1 medium sized\nTomatoes - 2 medium sized\nGarlic - 4 cloves\nGinger - 1/2 inch piece\nCinnamon - 1/4 inch piece\nCloves - 2",
					  "1. Soak rajma overnight atleast for 8 hrs, rinse it in water for 2-3 times.Then pressure cook along with water till immersing level until soft(I did for 7 whistles, depends on variety of rajma), Set aside.Reserve the drained rajma cooked water for later use.Heat oil in a pan add the ingredients listed under to saute and grind.\n"
					  + "2. Cook till raw smell of tomatoes leave and is slightly mushy. Cool down and then transfer it to a mixer.\n"
					  + "3. Grind it to smooth paste without adding water,set aside. Heat oil in a pan - temper jeera, let it splutter.Then add the onion tomato paste.\n"
					  + "4. Then add garam masala and saute for 2mins then add reserved rajma cooked water and let it boil for mins. Dilute it well as it has to cook for more time.Then add cooked rajma and required salt.\n"
					  + "5. Cover with a lid and let the gravy thicken and let rajma absorb the gravy well.Add milk/cream, give a quick stir and cook for 2mins. Finally garnish with coriander leaves and kasoori methi, quick stir and switch off.",
					  "http://www.vegrecipesofindia.com/rajma-masala-recipe-restaurant-style\nhttp://cooks.ndtv.com/recipe/show/rajma-233367",
					  RecipeCategory.LunchOrDinnerMainDish
					  );
			  
			  // 2 Chole
			  createRecipeItem(
					  "Punjabi Chole Masala", "Chickpeas in tomatoes, onions and spices.",
					  "chole", "50 mins", "45 mins", "95 mins",
					  (float)4, (float)3.5,
					  "2 tablespoons vegetable oil\n1 teaspoon cumin seeds\n1 medium yellow onion, small dice\n4 teaspoons peeled, finely chopped fresh ginger (from about a 2-inch piece)\n4 medium garlic cloves, finely chopped\n2 serrano chiles, stemmed and finely chopped\n1 (28-ounce) can whole peeled tomatoes and their juices\n2 teaspoons garam masala\n1 teaspoon ground coriander\n1 teaspoon kosher salt, plus more for seasoning\n1/2 teaspoon turmeric\n2 (15-ounce) cans chickpeas, also known as garbanzo beans, drained and rinsed\n1/2 cup water",
					  "1. Heat the oil in a large frying pan over medium heat until shimmering. Add the cumin seeds and cook, stirring occasionally, until fragrant, about 1 minute. Add the onion, ginger, garlic, and chiles and season with kosher salt. Cook, stirring occasionally, until the onions have softened, about 6 minutes.\n"
					  + "2. Meanwhile, set a fine-mesh strainer over a medium bowl. Strain the tomatoes and reserve the juices. Coarsely chop the tomatoes into 1-inch pieces; set aside.\n"
					  + "3. When the onions have softened, add the garam masala, coriander, measured salt, and turmeric to the frying pan and stir to coat the onion mixture. Cook, stirring occasionally, until fragrant, about 1 minute.\n"
					  + "4. Add the chopped tomatoes, their reserved juices, the chickpeas, and the water. Stir to combine, scraping up any browned bits from the bottom of the pan, and bring to a simmer. Reduce the heat to medium low and simmer, stirring occasionally, until the flavors have melded and the sauce has thickened slightly, about 20 minutes.\n",
					  "http://www.chow.com/recipes/30267-chole-chana-masala",
					  RecipeCategory.LunchOrDinnerMainDish);

			  // 3
			  createRecipeItem(
					  "Naan", "Oven baked flatbread", "naan", 
					  "30 mins", "7 mins", "37 mins", 
					  (float)4, (float)2, 
					  "1 (.25 ounce) package active dry yeast \n1 cup warm water \n1/4 cup white sugar \n3 tablespoons milk \n1 egg, beaten \n2 teaspoons salt \n4 1/2 cups bread flour \n2 teaspoons minced garlic (optional) \n1/4 cup butter, melted", 
					  "1. In a large bowl, dissolve yeast in warm water. Let stand about 10 minutes, until frothy. Stir in sugar, milk, egg, salt, and enough flour to make a soft dough. Knead for 6 to 8 minutes on a lightly floured surface, or until smooth. Place dough in a well oiled bowl, cover with a damp cloth, and set aside to rise. Let it rise 1 hour, until the dough has doubled in volume.\n"
					  + "2. Punch down dough, and knead in garlic. Pinch off small handfuls of dough about the size of a golf ball. Roll into balls, and place on a tray. Cover with a towel, and allow to rise until doubled in size, about 30 minutes.\n"
					  + "3. During the second rising, preheat grill to high heat.\n"
					  + "4. At grill side, roll one ball of dough out into a thin circle. Lightly oil grill. Place dough on grill, and cook for 2 to 3 minutes, or until puffy and lightly browned. Brush uncooked side with butter, and turn over. Brush cooked side with butter, and cook until browned, another 2 to 4 minutes. Remove from grill, and continue the process until all the naan has been prepared.\n", 
					  "http://allrecipes.com/Recipe/Naan/Detail.aspx", 
					  RecipeCategory.LunchOrDinnerSideDish);
			  
			  // 4	
			  createRecipeItem(
					  "Boiled Eggs", "Eggs cut into half, with salt & onions", "eggs",
					  "1 mins", "9 mins", "10 mins",
					  (float)3, (float)4,
					  "Eggs - 2\nSalt & Onion to taste",
					  "1. Place eggs in saucepan large enough to hold them in single layer. Add cold water to cover eggs by 1 inch. Heat over high heat just to boiling. Remove from burner. Cover pan.\n 2. Let eggs stand in hot water about 12 minutes for large eggs (9 minutes for medium eggs; 15 minutes for extra large).\n 3. Drain immediately and serve warm. Or, cool completely under cold running water or in bowl of ice water, then refrigerate.\n",
					  "http://www.incredibleegg.org/recipes/recipe/easy-hard-boiled-eggs",
					  RecipeCategory.BreakFast);
			  
			  // 5
			  createRecipeItem(
					  "Jeera Rice", "Rice with cumin", "jeerarice", 
					  "5 mins", "15 mins", "20 mins", 
					  (float)3, (float)3,
					  "1 cup Basmati rice (a long grain Indian rice) \n3 cups water \nSalt to taste \n2 tbsps vegtable, sunflower or canola oil/ghee \n1 large onion chopped fine \n2 tsps cumin seeds \n1/2 cup water \nCoriander leaves to garnish", 
					  "Wash the Basmati rice well in running water. \nAdd the 3 cups of water and salt to taste to the rice and set it up to boil. \nOnce the rice is almost cooked (test a few grains often to check - they will feel soft on the outside but very slightly hard on the inside), remove from fire and drain the water by straining the rice through a sieve or colander. Set aside. \nIn another pan, heat the oil/ghee till hot and add onions. \nFry till light brown and then add the cumin seeds. The seeds will splutter and sizzle to show they are done. \nNow add the rice and stir well. \nAdd 1/2 a cup of water to the rice and cover. \nSimmer till all the water dries up. \nAllow the rice to stand for another 2-3 minutes and then serve garnished with coriander leaves.", 
					  "http://indianfood.about.com/od/ricerecipes/r/jeerarice.htm", 
					  RecipeCategory.LunchOrDinnerSideDish);

			  // 6
			  createRecipeItem(
					  "Aloo Paratha", "One of the most popular paratha recipe from punjab", "alooparatha", 
					  "10 mins", "30 mins", "40 mins", 
					  (float)4, (float)2.5, 
					  "2 cups atta / whole wheat flour \nJust over 1/2 cup water \n1 tsp salt (or to taste) \nA few drops of oil \n2 medium sized potatoes \n1 tsp red chilli powder \n1/2 tsp jeera / cumin powder \n1/4 tsp ajwain / omam / carom seeds \n1/2 tsp chaat masala (or garam masala) \n1/2 tsp salt \nA handful of coriander leaves, chopped fine", 
					  "Sieve the flours with the salt. \nAdd the ghee and mix well. Add enough water and make a semi-soft dough. Keep aside. \nMash the potatoes coarsely or cut into very small pieces. Keep aside. \nHeat the ghee in a vessel, add the cumin seeds and onion; and fry for at least 3 minutes. \nAdd the green chillies and fry again for 1 minute. \nAdd the potatoes, salt, coriander, chilli powder and amchur powder. Mix well and cook for 1 minute. \nCook the mixture. Keep aside. \nKnead the dough and divide into 10 portions. \nRoll out one portion of the dough into a circle of 125 mm. diameter. \nPlace one portion of the prepared filling in the center of the dough circle. \nBring togeather all the sides in the center and seal tightly. \nRoll out again into a circle of 125 mm. diameter with the help of a little flour. \nCook the paratha on a tava (griddle), using a little ghee until both sides are golden brown. \nRepeat with the remaining dough and filling to make more parathas. \nServe hot.", 
					  "http://www.cookingandme.com/2011/02/aloo-paratha-step-by-step-recipe.html", 
					  RecipeCategory.BreakFast);
			  
			  // 7
			  createRecipeItem(
					  "Mix Vegetable", "mix vegetables cooked in indian style", "mixveg",
					  "20 mins", "30 mins", "50 mins",
					  (float) 4, (float) 4, 
					  "2 cups of mix chopped veggies – cauliflower, carrots, potatoes, french beans, capsicum, peas. \n1 onion chopped finely \n2 tomatoes chopped finely \n1 green chili chopped finely \n1 tsp ginger-garlic paste \n2 tsp coriander powder \n1/2 tsp turmeric powder \n1/4 tsp chilli powder (use more if you want it to be spicy) \n1/2 tsp garam masala powder \n1 tsp cumin seeds \n8-10 paneer cubes (optional) \n2 cups water \n2 tbsp cream or malai \n2 tbsp oil \na few sprigs of cilantro/coriander leaves chopped \nsalt as per taste", 
					  "In a kadhai or thick bottomed pan, heat oil. \nAdd cumin seeds. Once they splutter, add the chopped onions. \nFry the onions till they become transparent. \nAdd the ginger-garlic paste. Fry for a minute or till the raw smell disappears. \nAdd the tomatoes. Keep on stirring till the tomatoes become soft and pulpy. When the mixture becomes smooth and one, then add all the spice powders mentioned above. \nThe process of frying the tomatoes takes a little longer. If you want to quicken the process, add some salt to the onion-tomato mixture. Fry the tomatoes on a low flame as you don’t want the tomatoes to get burnt. \nNow add all the spice powders one by one. \nStir the spice powders with the onion-tomato mixture. Add the green chili. \nMix in the chopped veggies, salt and water. \nCover and let the veggies cook. \nOnce the veggies are semi cooked…… that is they are half cooked. Add the cream. \nGive a stir. \nCover again and simmer the veggies till they are done. \nDon’t forget to check the veggies after occasionally. \nAdd more water if the water dries up and if the veggies are still to be cooked. \nIf using paneer, then add the paneer once the veggies are cooked. Simmer without the lid for 2 minutes. \nYou can also garnish mix vegetable dish with fried paneer cubes. Otherwise simply garnish with chopped coriander leaves. \nServe mix vegetables dish hot with pooris, parathas, kulcha or chapatis.", 
					  "http://www.vegrecipesofindia.com/mix-veg-recipe-indian/", 
					  RecipeCategory.LunchOrDinnerMainDish);
	  }
	  
	  private void createIngredient(String name, int category, String unit) {
		  ContentValues values = new ContentValues();
		  values.put("name", name);
		  values.put("cat_id", category);
		  values.put("unit", unit);
		  
		  database.insert(DataHelper.INGREDIENTS_TABLE, null, values);
	  }
	  
	  private void createRelation(int rec_id, int ing_id, int quantity) {
		  ContentValues values = new ContentValues();
		  values.put("rec_id", rec_id);
		  values.put("ing_id", ing_id);
		  values.put("quantity", quantity);
		  
		  database.insert(DataHelper.RECIPE_INGRED_TABLE, null, values);
	  }

	  public void fillSettings()
	  {
		  setSetting(Setting.NoOfPortions, "2");
		  setSetting(Setting.SkipBreakfast, "false");
		  setSetting(Setting.SkipLunch, "false");
		  setSetting(Setting.SkipDinner, "false");
		  setSetting(Setting.SkipSnacks, "true");
		  setSetting(Setting.SkipDrinks, "true");
		  setSetting(Setting.HealthFactor, "3.5");
		  setSetting(Setting.TasteFactor, "4");
		  setSetting(Setting.CostFactor, "1500");
	  }
	  
	  public void setSetting(Setting setting, String value) {
		  // clear any existing setting first
		  if (getSetting(setting) != null)
			  deleteSetting(setting);
		  
		  // now add the setting
		  ContentValues values = new ContentValues();
		    values.put(DataHelper.SETTINGS_COLUMN_TYPE, setting.ordinal());
		    values.put(DataHelper.SETTINGS_COLUMN_VALUE, value);
		    
		    database.insert(DataHelper.TABLE_SETTINGS, null, values);
	  }
	  
	  public String getSetting(Setting setting) {	  
		  String value = null;
		    
		  try {
			    Cursor cursor = database.query(DataHelper.TABLE_SETTINGS,
			    	settingsAllColumns, DataHelper.SETTINGS_COLUMN_TYPE + " = " + setting.ordinal(), null, null, null, null);
		
			    cursor.moveToFirst();
	
				if (!cursor.isAfterLast())
				{
					value = cursor.getString(2); 
			    }
				
			    // make sure to close the cursor
			    cursor.close();    
		  }
		  catch (Exception ex)
		  {
			  return null;
		  }
		  
	    return value;
	 }
	  
	  public void deleteSetting(Setting setting) {
	    database.delete(DataHelper.TABLE_SETTINGS, DataHelper.SETTINGS_COLUMN_TYPE
	        + " = " + setting.ordinal(), null);
	  }

	  public RecipeItem createRecipeItem(
			  String name,
			  String desc,
			  String image,
			  String prepTime,
			  String cookingTime,
			  String totalTime,
			  float tasteRating,
			  float healthRating,
			  String ingredients,
			  String instructions,
			  String links,
			  RecipeCategory category) {
		  
		    ContentValues values = new ContentValues();
		    values.put(DataHelper.RECIPE_COLUMN_NAME, name);
		    values.put(DataHelper.RECIPE_COLUMN_DESCRIPTION, desc);
		    values.put(DataHelper.RECIPE_COLUMN_IMAGE, image);
		    values.put(DataHelper.RECIPE_COLUMN_PREP_TIME, prepTime);
		    values.put(DataHelper.RECIPE_COLUMN_COOKING_TIME, cookingTime);
		    values.put(DataHelper.RECIPE_COLUMN_TOTAL_TIME, totalTime);
		    values.put(DataHelper.RECIPE_COLUMN_TASTE_RATING, tasteRating);
		    values.put(DataHelper.RECIPE_COLUMN_HEALTH_RATING, healthRating);
		    values.put(DataHelper.RECIPE_COLUMN_INGREDIENTS, ingredients);
		    values.put(DataHelper.RECIPE_COLUMN_INSTRUCTIONS, instructions);
		    values.put(DataHelper.RECIPE_COLUMN_LINKS, links);
		    values.put(DataHelper.RECIPE_COLUMN_CATEGORY, category.ordinal());
		    
		    long insertId = database.insert(DataHelper.TABLE_RECIPE, null,
		        values);
		    Cursor cursor = database.query(DataHelper.TABLE_RECIPE,
		        recipeAllColumns, DataHelper.RECIPE_COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    RecipeItem newRecipeItem = cursorToRecipe(cursor);
		    cursor.close();
		    return newRecipeItem;
	  }

	  public void deleteRecipeItem(RecipeItem recipeItem) {
	    long id = recipeItem.getId();
	    System.out.println("Recipe deleted with id: " + id);
	    database.delete(DataHelper.TABLE_RECIPE, DataHelper.RECIPE_COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<RecipeItem> getRecipesByCriteria(String selectionCriteria) {
		    List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();

		    try {
			    Cursor cursor = database.query(DataHelper.TABLE_RECIPE,
			        recipeAllColumns, selectionCriteria, null, null, null, null);
		
			    cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			      RecipeItem recipeItem = cursorToRecipe(cursor);
			      recipeItems.add(recipeItem);
			      cursor.moveToNext();
			    }
			    // make sure to close the cursor
			    cursor.close();
			 }
			 catch (Exception ex)
			 {
				 return null;
			 }
		    return recipeItems;
		  }

	  
	  public List<RecipeItem> getAllRecipeItems() {
	    List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();

	    try {
		    Cursor cursor = database.query(DataHelper.TABLE_RECIPE,
		        recipeAllColumns, null, null, null, null, null);
	
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      RecipeItem recipeItem = cursorToRecipe(cursor);
		      recipeItems.add(recipeItem);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		 }
		 catch (Exception ex)
		 {
			 return null;
		 }
	    return recipeItems;
	  }
	  

	  public RecipeItem getRecipeItem(long id) {
		  RecipeItem recipeItem = null;
		    
		  try {
			    Cursor cursor = database.query(DataHelper.TABLE_RECIPE,
			        recipeAllColumns, DataHelper.RECIPE_COLUMN_ID + " = " + id, null, null, null, null);
		
			    cursor.moveToFirst();
	
				if (!cursor.isAfterLast())
				{
			      recipeItem = cursorToRecipe(cursor); 
			    }
				
			    // make sure to close the cursor
			    cursor.close();    
		  }
		  catch (Exception ex)
		  {
			  return null;
		  }
		  
	    return recipeItem;
	  }

	  private RecipeItem cursorToRecipe(Cursor cursor) {
	    RecipeItem recipeItem = 
	    	new RecipeItem(
    			cursor.getLong(0),
    			cursor.getString(1),
    			cursor.getString(2),
    			cursor.getString(3),
    			cursor.getString(4),
    			cursor.getString(5),
    			cursor.getString(6),
    			cursor.getFloat(7),
    			cursor.getFloat(8),
    			cursor.getString(9),
    			cursor.getString(10),
    			cursor.getString(11),
    			RecipeCategory.values()[cursor.getInt(12)]
    			);

	    return recipeItem;
	  }
	
	  public MealItem createMealItem(String date, String category, int recipe_id) {
		    ContentValues values = new ContentValues();
		    values.put(DataHelper.MEALS_COLUMN_DATE, date);
		    values.put(DataHelper.MEALS_COLUMN_CATEGORY, category);
		    values.put(DataHelper.MEALS_COLUMN_RECIPE_ID, recipe_id);
		    long insertId = database.insert(DataHelper.TABLE_MEALS, null,
		        values);
		    System.out.println(insertId);
		    Cursor cursor = database.query(DataHelper.TABLE_MEALS,
		        mealsAllColumns, DataHelper.MEALS_COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    MealItem newMealItem = cursorToMeal(cursor);
		    cursor.close();
		    return newMealItem;
		  }
		  
		  public MealItem getMealItemByID(long meal_id){
			    Cursor cursor = database.query(DataHelper.TABLE_MEALS,
				        mealsAllColumns, DataHelper.MEALS_COLUMN_ID + " = " + meal_id, null,
				        null, null, null);
			    cursor.moveToFirst();
			    MealItem newMealItem = cursorToMeal(cursor);
			    cursor.close();
			    return newMealItem;
		  }

		  public void deleteMealItem(MealItem mealItem) {
		    long id = mealItem.getId();
		    System.out.println("Meal deleted with id: " + id);
		    database.delete(DataHelper.TABLE_MEALS, DataHelper.MEALS_COLUMN_ID
		        + " = " + id, null);
		  }

		  public List<MealItem> getAllMealItemsForADate(String date) {
		    List<MealItem> mealItems = new ArrayList<MealItem>();

		    Cursor cursor = database.query(DataHelper.TABLE_MEALS,
		        mealsAllColumns, DataHelper.MEALS_COLUMN_DATE +" = \""+date+"\"", null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      MealItem mealItem = cursorToMeal(cursor);
		      mealItems.add(mealItem);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    System.out.println("Meal numbers: " + mealItems.size());
		    return mealItems;
		  }

		  private MealItem cursorToMeal(Cursor cursor) {
		    MealItem mealItem = new MealItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
		    return mealItem;
		  }
		  
		  private Cursor execSql(String sql) {
			  Cursor c = null;

			  try {
				  c = database.rawQuery(sql, null);		  
			  }
			  catch(SQLException e) {
				  String x = e.toString();
			  }
			  return c;
		  }

		  public Cursor getGroceryItems(int option) {
			  String sql = "select i.id as _id, i.name as name, sum(m.quantity) || ' ' || i.unit as value, 'used in ' || r.name as desc from "+DataHelper.TABLE_RECIPE+" r, "+DataHelper.RECIPE_INGRED_TABLE+" m, "+DataHelper.INGREDIENTS_TABLE+" i where r."+DataHelper.RECIPE_COLUMN_ID+" = m.rec_id and i.id = m.ing_id and i.cat_id = %s group by i.id";
			  return execSql(String.format(sql, option));
		  }
		  	  
		  public Cursor getAllGroceryItems() {
			  String sql = "select i.id as _id from ingredients i";
			  return execSql(sql);
		  }
	} 


public class RecipeItem {
	  private long _id;
	  private String _name;
	  private String _desc;
	  private String _image;
	  private String _prepTime;
	  private String _cookingTime;
	  private String _totalTime;
	  private float _tasteRating;
	  private float _healthRating;
	  private String _ingredients;
	  private String _instructions;
	  private String _links;
	  private RecipeCategory _category;
	  
	  public RecipeItem(
		  long id,
		  String name,
		  String desc,
		  String image,
		  String prepTime,
		  String cookingTime,
		  String totalTime,
		  float tasteRating,
		  float healthRating,
		  String ingredients,
		  String instructions,
		  String links,
		  RecipeCategory category)
	  {
		 _id = id;
		 _name = name;
		 _desc = desc;
		 _image = image;
		 _prepTime = prepTime;
		 _cookingTime = cookingTime;
		 _totalTime = totalTime;
		 _tasteRating = tasteRating;
		 _healthRating = healthRating;
		 _ingredients = ingredients;
		 _instructions = instructions;
		 _links = links;
		 _category = category;
	  }
	  
	  public long getId() { return _id; }
	  public String getName() { return _name; }
	  public String getDescription() { return _desc; }
	  public String getImage() { return _image; }
	  public String getPrepTime() { return _prepTime; }
	  public String getCookingTime() { return _cookingTime; }
	  public String getTotalTime() { return _totalTime; }
	  public float getTasteRating() { return _tasteRating; }
	  public float getHealthRating() { return _healthRating; }
	  public String getIngredients() { return _ingredients; }
	  public String getInstructions() { return _instructions; }
	  public String getLinks() { return _links; }
	  public RecipeCategory getCategory() { return _category; }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return _name;
	  }
	} 

public class MealItem {
	  public long id;
	  public String date;
	  public String category;
	  public int recipe_id;

	  public MealItem(long id, String date, String category, int recipe_id) {
		    this.id = id;
		    this.date = date;
		    this.category = category;
		    this.recipe_id = recipe_id;
		  }

	  public long getId() {
	    return id;
	  }
	  public void setId(long id) {
	    this.id = id;
	  }
	} 

}
