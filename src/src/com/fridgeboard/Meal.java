package com.fridgeboard;

public class Meal {
	public long meal_id;
	public int icon;
    public String title;
    public String desc;
    public String duration;
    public int recipe_id;
    public Meal(){
        super();
    }

    public Meal(long meal_id, int icon, String title, String desc, String duration, int recipe_id) {
        super();
        this.meal_id = meal_id;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.duration = duration;
        this.recipe_id = recipe_id;
    }

}
