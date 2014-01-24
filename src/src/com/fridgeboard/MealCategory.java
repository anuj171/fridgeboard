package com.fridgeboard;

import java.util.ArrayList;

public class MealCategory {
	public String category;
	public ArrayList<Meal> mealList = new ArrayList<Meal>();
	
    public MealCategory(){
        super();
    }

    public MealCategory(String category, ArrayList<Meal> mealList) {
        super();
        this.category = category;
        this.mealList = mealList;
    }

}
