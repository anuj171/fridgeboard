package com.fridgeboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;


// this is the main java activity
public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		switch(item.getItemId()) {
		case R.id.action_groceries:
			//Toast.makeText(this, "Ordering on Big Basket", Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(this)
		    .setTitle("Generate Grocery List")
		    .setMessage("Generating groceries list for your meal plan.")
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .show();
			break;
		case R.id.action_settings:
			new AlertDialog.Builder(this)
		    .setTitle("Meal Preferences")
		    .setMessage("using default settings.")
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .show();
			break;
		}
		return false;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String[] dateList;
				
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			// setting time
			Calendar rightNow = Calendar.getInstance();
					
			DateFormat df = new SimpleDateFormat("EEE, d MMM");
			
		    dateList = new String[]{"","","","","","",""};
		    dateList[0] = df.format(rightNow.getTime());
		    for (int i=1;i<7;i++){
		    	rightNow.add(Calendar.DAY_OF_MONTH, 1);
		    	dateList[i] = df.format(rightNow.getTime());
		    }
		
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new RecipeListFragment();
			Bundle args = new Bundle();
			args.putInt(RecipeListFragment.ARG_CATEGORY, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 7 total pages.
			return 7;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position<7){	
				return dateList[position];
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class RecipeListFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_CATEGORY = "CATEGORY";

		String[][] recipeList;
		
		public RecipeListFragment() {
			recipeList = new String[][]
		    		{
		    			{ "Boiled Eggs 1", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 2", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 3", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 4", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 5", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 6", "Khichdi", "Samosa", "Biryani" },
		    			{ "Boiled Eggs 7", "Khichdi", "Samosa", "Biryani" },
		    		};
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    	    
		    int option = getArguments().getInt(ARG_CATEGORY);
		    
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_checked, recipeList[option]);
		    setListAdapter(adapter);
		}
	}
    
}
