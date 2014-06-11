package com.challengepost.flashdelivery;

import java.util.ArrayList;

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

public class HomeActivity extends FragmentActivity implements OrderFragment.HomeActivity {
	
	// nav bar stuffs
    private ViewPager viewPager;
	private ToggleButton navBtnMap[];
	private TabPagerAdapter tabAdapter;
	private Context c = this;
	private ArrayList<String> tabName = new ArrayList<String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Home.setHomeContext(c);
        
        tabAdapter = new TabPagerAdapter(getSupportFragmentManager());
	    viewPager = (ViewPager) findViewById(R.id.pager);
	    Home.setPager(viewPager);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
            	setSelectedTab(position);
            	Home.updatePager();
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
//    	BaseFragment fragment = (BaseFragment) getTabFragment(viewPager.getCurrentItem());
//    	fragment.onHideFragment();
    	
        int position = Integer.parseInt(view.getTag().toString());
        viewPager.setCurrentItem(position, true);
        setSelectedTab(position);
        Home.updatePager();
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
        	if (Home.isOrder())
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

	public static class Home{
		private static Context con;
		private static boolean login = false;
		private static boolean order = false;
		private static String userName = "";
		private static String pass = "";
		private static ViewPager v;
		
		public static void setHomeContext(Context c) {con = c;}
		public static Context getHomeContext() {return con;}

		public static void setLoginTag(boolean b) {login = b;}
		public static boolean getLoginTag() {return login;}

		public static void setOrder(boolean s) {order = s;}
		public static boolean isOrder() {return order;}
		
		public static void setUserName(String u) {userName = u;}
		public static String getUserName() {return userName;}
		
		public static void setPass(String p) {pass = p;}
		public static String getPass() {return pass;}
		
		public static void setPager(ViewPager view) {v = view;}
		public static void updatePager() {v.getAdapter().notifyDataSetChanged();}
	}
}
