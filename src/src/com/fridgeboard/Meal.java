package com.fridgeboard;

public class Meal {
	public int icon;
    public String title;
    public String desc;
    public String duration;
    public Meal(){
        super();
    }

    public Meal(int icon, String title, String desc, String duration) {
        super();
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.duration = duration;		
    }

}
