package com.fridgeboard;

public class Meal {
    public boolean is_category;
	public int icon;
    public String title;
    public String desc;
    public String duration;
    public Meal(){
        super();
    }

    public Meal(String category) {
        super();
        this.is_category = true;
        this.title = title;
    }

    public Meal(int icon, String title, String desc, String duration) {
        super();
        this.is_category = false;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.duration = duration;		
    }

}
