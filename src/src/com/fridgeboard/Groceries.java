package com.fridgeboard;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class Groceries extends FragmentActivity {

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
	
	static TextView mRemainingLabel;
	static HashMap selected;
	static Boolean labelSet;

	public static String[] Categories = {"Vegetables", "Staples", "Pulses", "Dairy", "Other"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groceries);
		
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) findViewById(R.id.titletext);
        titleText.setText(R.string.title_activity_groceries);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mRemainingLabel = (TextView) findViewById(R.id.remaining_items_label);
		
	    selected = new HashMap();
	    for(int j=0; j<Categories.length; j++) {
	    	selected.put(j, 0);
	    }
	    
	    labelSet = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groceries, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		switch(item.getItemId()) {
		case R.id.item0:
			//Toast.makeText(this, "Ordering on Big Basket", Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(this)
		    .setTitle("Shop on BigBasket.com")
		    .setMessage("We will pass on your grocery list to BigBasket. You'll be able to make changes before checkout.")
		    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .show();
			break;
		case R.id.item1:
		case R.id.item2:
			Toast.makeText(this, "Updating list", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(this, "Selected", Toast.LENGTH_SHORT).show();
			break;			
		}
		return false;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new GroceryListFragment();
			Bundle args = new Bundle();
			args.putInt(GroceryListFragment.ARG_CATEGORY, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return Categories.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return Categories[position].toUpperCase(l);
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class GroceryListFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_CATEGORY = "CATEGORY";

		public GroceryListFragment() {
		}

		static int total;
		
		@SuppressWarnings("deprecation")
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);

		    final int option = getArguments().getInt(ARG_CATEGORY);

		    GroceriesDb groceries = new GroceriesDb(getActivity());
		    Cursor list = groceries.getListForCategory(option);
		    getActivity().startManagingCursor(list);
		    
		    total = groceries.getTotalCount();
		    
		    if(!labelSet) {
		    	mRemainingLabel.setText(total + " items remaining");
		    	labelSet = true;
		    }
 
            String[] columns = new String[] { "name", "value", "desc", "name" };
            int[] to = new int[] { R.id.grocery_name, R.id.grocery_value, R.id.grocery_desc, R.id.grocery_img};
 
            try {
            	GroceriesData adapter = new GroceriesData(getActivity(), R.layout.groceries_item, list, columns, to, 0);
            	setListAdapter(adapter);
            }
            catch(Exception e) {
            	String x = e.toString();
            }            
		    
		    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		    getListView().setOnItemClickListener(new OnItemClickListener() {
		        @Override
		        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		        	CheckedTextView cb = (CheckedTextView) view.findViewById(R.id.grocery_namecb);
		        	cb.setChecked(!cb.isChecked());
		        	
		        	int x = getListView().getCheckedItemCount();		        	
		        	selected.put(option, x);
		        	
		        	int currentSel = 0;
		        	for(int j=0; j<Categories.length; j++) {
		        		currentSel += (Integer) selected.get(j);
		        	}
		        	int remaining = total - currentSel;
		        	
		        	mRemainingLabel.setText(remaining + " items remaining");
		        }
		    });
		    
		    groceries.close();
		}
	}
}