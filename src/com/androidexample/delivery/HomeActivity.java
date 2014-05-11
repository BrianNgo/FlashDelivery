package com.androidexample.delivery;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ToggleButton;

public class HomeActivity extends FragmentActivity implements OrderFragment.Home {
	
	// nav bar stuffs
    private ViewPager viewPager;
	private ToggleButton navBtnMap[];
	private TabPagerAdapter tabAdapter;
	private Context c = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        HomeContext.setHomeContext(c);
        
        tabAdapter = new TabPagerAdapter(getSupportFragmentManager());
	    viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
            		setSelectedTab(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        
	    Animation a = AnimationUtils.loadAnimation(this, R.anim.progress_anim);
	    a.setInterpolator(new Interpolator() {
	        private final int frameCount = 8;

	        @Override
	        public float getInterpolation(float input) {
	            return (float)Math.floor(input*frameCount)/frameCount;
	        }
	    });
	    a.setDuration(1000);
	    
	    // share pref to check if app is opened 1st time
	    SharedPreferences settings = getSharedPreferences("prefs", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("firstRun", false);
	    editor.commit();
	    
	    boolean firstRun = settings.getBoolean("firstRun", true);
	    Log.d("TAG1", "firstRun: " + Boolean.valueOf(firstRun).toString());        
        

        initNavToggleToggleButtons();
        setSelectedTab(0);   
    }
    
    public void changeTab(View view) {
    	BaseFragment fragment = (BaseFragment)getTabFragment(viewPager.getCurrentItem());
    	fragment.onHideFragment();
    	
        int position = Integer.parseInt(view.getTag().toString());
        viewPager.setCurrentItem(position, true);
        setSelectedTab(position);
    }
     
    public void setSelectedTab(int position) {
        BaseFragment currentTab = (BaseFragment) getTabFragment(position);
        if (currentTab != null) {
            currentTab.setActionBar();
            currentTab.onPageSelected();
        }
        for (int i = 0; i < navBtnMap.length; i++) {
            navBtnMap[i].setChecked(i == position);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        try {
        	Intent in = getIntent();
        	int order = Integer.parseInt(in.getStringExtra("order"));
        	if (order == 1)
        		viewPager.setCurrentItem(1, true);
        } catch (NumberFormatException e) {}
    }
    
    private Fragment getTabFragment(int position) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
    
    public int getCurrentTabIndex() {
        return viewPager.getCurrentItem();
    }
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private void initNavToggleToggleButtons() {
        navBtnMap = new ToggleButton[3];
        navBtnMap[0] = (ToggleButton) findViewById(R.id.tab_search);
        navBtnMap[1] = (ToggleButton) findViewById(R.id.tab_orders);
        navBtnMap[2] = (ToggleButton) findViewById(R.id.tab_account);
    }
	
    public void setActionBar(View view) {
        getActionBar().setCustomView(view);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

	@Override
	public void onBackPressedHome() {
		viewPager.setCurrentItem(0);	
	}

	public static class HomeContext{
		private static Context con;
		
		public static void setHomeContext(Context c) {con = c;}
		public static Context getHomeContext() {return con;}
	}
}
