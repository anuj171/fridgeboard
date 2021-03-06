package com.fridgeboard;

public class Meal {
	public long meal_id;
	public int icon;
	public int imageTime;
    public String title;
    public String desc;
    public String nutrition;
    public String duration;
    public int recipe_id;
    public float health_rating;
    public float taste_rating;
    public float cost_rating;
    public boolean isSideDish;
    
    public Meal(){
        super();
    }

    public Meal(long meal_id, int icon, String title, String desc, String nutrition, String duration, int recipe_id, float health_rating, float taste_rating, float cost_rating, boolean isSideDish, int imageTime) {
        super();
        this.meal_id = meal_id;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.nutrition = nutrition;
        this.duration = duration;
        this.recipe_id = recipe_id;
        this.health_rating = health_rating;
        this.taste_rating = taste_rating;
        this.cost_rating = cost_rating;
        this.isSideDish = isSideDish;
        this.imageTime = imageTime;
    }

}
