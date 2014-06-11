package com.challengepost.flashdelivery;

import com.challengepost.flashdelivery.HomeActivity.Home;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class CheckOutActivity extends Activity {
	Button btnBack;
    private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		
		Home.setOrder(false);
		// top bar
        actionBar = getActionBar(); 
	    actionBar.setCustomView(R.layout.actionbar_top_display_menu_activity);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
	    
	    // fix flipping tabs and action bar
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        
		TextView text = (TextView) findViewById(R.id.textViewGeneral);
		text.setText("Check Out");
        
	    // buttons in this activity
	    btnBack = (Button) findViewById(R.id.btnBackMerchant);
	    
	    // handle events for buttons
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
				overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_out, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
