package com.fridgeboard;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

	public static String[] Categories = {"Dairy", "Bakery", "Vegetables", "Cereals", "Pulses"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groceries);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mRemainingLabel = (TextView) findViewById(R.id.remaining_items_label);
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
		    .setTitle("Shop for groceries on BigBasket.com")
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
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);

		    int option = getArguments().getInt(ARG_CATEGORY);

		    GroceriesData groceries = new GroceriesData();
		    List<String> list = groceries.getListForRecipesAndCategory(null, option);
		    
		    total = groceries.getTotalCount();

		    mRemainingLabel.setText(total + " items remaining");
		    
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_checked, list);
		    
		    setListAdapter(adapter);
		    
		    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		    getListView().setOnItemClickListener(new OnItemClickListener() {
		        @Override
		        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		        	int x = getListView().getCheckedItemCount();
		        	int remaining = total - x;
		        	mRemainingLabel.setText(remaining + " items remaining");
		        }
		    });
		}
	}
}
