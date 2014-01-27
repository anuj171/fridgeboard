package com.fridgeboard;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.view.ViewPager.OnPageChangeListener;


public class MealPlanAdapter extends BaseExpandableListAdapter {

    Context context; 
    int layoutResourceId;  
    ArrayList<MealCategory> categoryList;
    
    public MealPlanAdapter(Context context, ArrayList<MealCategory> categoryList) {
//        super(context, layoutResourceId, data);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
     return categoryList.get(groupPosition).mealList.get(childPosition);
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
     return childPosition;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
      View view, ViewGroup parent) {
      
     Meal meal = (Meal) getChild(groupPosition, childPosition);
     LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     view = infalInflater.inflate(R.layout.widget_recipe_item, parent, false);

     MyPagerAdapter pagerAdapter = new MyPagerAdapter();
     pagerAdapter.meal = meal;
     pagerAdapter.groupPosition = groupPosition;
     pagerAdapter.childPosition = childPosition;

     ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
     pager.setAdapter (pagerAdapter);
     pager.setCurrentItem(1);
     pager.setOnPageChangeListener(pagerAdapter);
     return view;
     
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
    	return categoryList.get(groupPosition).mealList.size();
    }
    
    @Override
    public Object getGroup(int groupPosition) {
     return categoryList.get(groupPosition);
    }
    
    @Override
    public int getGroupCount() {
     return categoryList.size();
    }
    
    @Override
    public long getGroupId(int groupPosition) {
     return groupPosition;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
      ViewGroup parent) {
      
     MealCategory category = (MealCategory) getGroup(groupPosition);
     if (view == null) {
      LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inf.inflate(R.layout.meal_plan_category, null);
     }
      
     ((TextView) view.findViewById(R.id.category)).setText(category.category.trim());
	 ImageButton add = (ImageButton) view.findViewById(R.id.buttonAdd);
	 int[] tag_array = new int[1]; 
	 tag_array[0] = groupPosition;
	 add.setTag(tag_array);
      
     return view;
    }
    
    @Override
    public boolean hasStableIds() {
     return true;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
     return true;
    }
    
    private class MyPagerAdapter extends PagerAdapter implements OnPageChangeListener {

        public View my_collection;
        public View refreshView, removeView;
    	public Meal meal;
        public int groupPosition;
        public int childPosition;


        public int getCount() {
                return 3;
        }

        public Object instantiateItem(View collection, int position) {

                my_collection = collection;
        		LayoutInflater inflater = (LayoutInflater) collection.getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = null;
                switch (position) {
                case 0:
                    // Create an initial view to display; must be a subclass of FrameLayout.
                    view = inflater.inflate (R.layout.widget_recipe_item_remove, null);
                    
	               	 removeView = (TextView) view.findViewById(R.id.removeView);
	               	 int[] tag_array = new int[2]; 
	               	 tag_array[0] = groupPosition;
	               	 tag_array[1] = childPosition;
	               	 removeView.setTag(tag_array);
                        break;

                case 1:
                    // Create an initial view to display; must be a subclass of FrameLayout.
                    view = inflater.inflate (R.layout.widget_recipe_item_one, null);
                    TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
               	 	txtTitle.setText(meal.title);
  //             	 	Toast.makeText(collection.getContext(), "creating view_1 for "+meal.title, Toast.LENGTH_SHORT).show();
               	 	View midLayout = (View)view.findViewById(R.id.recipeMidLayout);
	               	 int[] tagT_array = new int[2]; 
	               	 tagT_array[0] = groupPosition;
	               	 tagT_array[1] = childPosition;
	               	 midLayout.setTag(tagT_array);
	               	 ((TextView)view.findViewById(R.id.txtDesc)).setText(meal.desc);
	               	 ((TextView)view.findViewById(R.id.txtDuration)).setText(meal.duration);
	               	 ImageView imgIcon = (ImageView)view.findViewById(R.id.imgIcon);
	               	 imgIcon.setImageResource(meal.icon);
	               	 int[] tagI_array = new int[2]; 
	               	 tagI_array[0] = groupPosition;
	               	 tagI_array[1] = childPosition;
	               	 imgIcon.setTag(tagI_array);
                        break;
                case 2:
                    view = inflater.inflate (R.layout.widget_recipe_item_refresh, null);
//               	 	Toast.makeText(collection.getContext(), "creating view_2 for "+meal.title, Toast.LENGTH_SHORT).show();
	               	 refreshView = (TextView) view.findViewById(R.id.refreshView);
	               	 int[] tag2_array = new int[2]; 
	               	 tag2_array[0] = groupPosition;
	               	 tag2_array[1] = childPosition;
	               	 refreshView.setTag(tag2_array);
                        break;
                }

        
                ((ViewPager) collection).addView(view, 0);

                return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == ((View) arg1);

        }

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			switch (position){
			case 0:
				removeView.performClick();
//	     	 	Toast.makeText(my_collection.getContext(), "page 1", Toast.LENGTH_SHORT).show();
	            break;
			case 1:
//	     	 	Toast.makeText(my_collection.getContext(), "page 2", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				refreshView.performClick();
//				Toast.makeText(my_collection.getContext(), "page 2", Toast.LENGTH_SHORT).show();
				break;
			}
			
		}



    }
}
