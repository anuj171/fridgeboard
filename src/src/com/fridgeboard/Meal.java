package com.fridgeboard;

public class Meal {
	public long meal_id;
	public int icon;
    public String title;
    public String desc;
    public String duration;
    public Meal(){
        super();
    }

    public Meal(long meal_id, int icon, String title, String desc, String duration) {
        super();
        this.meal_id = meal_id;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.duration = duration;		
    }

}
