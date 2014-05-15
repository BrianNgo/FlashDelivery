package com.androidexample.delivery;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SubMenuActivity extends FragmentActivity implements SubMenuFragment.SubMenuActivity {
    private String[] tabs = {"Sub-Menu", "Dishes"};
	Button btnBack;
    private ViewPager viewPager;
    private TabsAdapter mAdapter;
    private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_menu);
		
		// top bar
		actionBar = getActionBar();
	    actionBar.setCustomView(R.layout.actionbar_top_sub_menu_activity);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
	    
	    // fix flipping tabs and action bar
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        
		// set actionbar to navigation mode to add tabs  
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsAdapter(getSupportFragmentManager(), this, viewPager, 1);       
        for (String tab_name : tabs) {
        	mAdapter.addTab(tab_name);
        }
	    
	    // buttons in this activity
	    btnBack = (Button) findViewById(R.id.btnBack);
	    
	    // handle events for buttons
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	   		
	    
		JSONObject subMenu = MenuData.getSubMenu();
		TextView text = (TextView) findViewById(R.id.menuName);
		try
		{
			text.setText(subMenu.getString("name"));
		}
		catch (JSONException e) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Error");
			alert.setMessage("This menu has no more information!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}      
			});
			alert.show();
		}
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

	@Override
	public void changeTab(int t) {
		DishFragment frag = (DishFragment) mAdapter.getFragment(1);
        frag.updateDish();
		actionBar.setSelectedNavigationItem(t);
	}
}
