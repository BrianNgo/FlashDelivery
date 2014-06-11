package com.challengepost.flashdelivery;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.challengepost.flashdelivery.DisplayMerchantsActivity.MenuData;

public class DisplayMenuActivity extends FragmentActivity {

	Button btnBack, btnFilter;
    private ViewPager viewPager;
    private TabsAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Information", "Menu"};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_menu);    
		// top bar
        actionBar = getActionBar(); 
	    actionBar.setCustomView(R.layout.actionbar_top_display_menu_activity);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
	    
	    // fix flipping tabs and action bar
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        
		// set actionbar to navigation mode to add tabs  
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsAdapter(getSupportFragmentManager(), this, viewPager, 0);       
        for (String tab_name : tabs) {
        	mAdapter.addTab(tab_name);
        }
	    
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
	    
	    if (MenuData.getMenu().has("menu") || !MenuData.getInfo().equals(""))
	    	if (!MenuData.getInfo().equals(""))
	    		actionBar.setSelectedNavigationItem(0);
	    	else
	    		actionBar.setSelectedNavigationItem(1);
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

}
