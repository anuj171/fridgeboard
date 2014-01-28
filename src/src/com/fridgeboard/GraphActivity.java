package com.fridgeboard;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.actionbar);
        
        ImageButton searchButton = (ImageButton) findViewById(R.id.share_button);
        searchButton.setVisibility(View.VISIBLE);
        
        TextView titleText = (TextView) findViewById(R.id.titletext);
        titleText.setText(R.string.title_activity_graph);
        
		setContentView(R.layout.activity_graph);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

    public void onShareClicked(View v)
    {
    	Toast.makeText(this, R.string.share, Toast.LENGTH_LONG).show();
    }
}
