package com.androidexample.delivery;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
 
public class TabsAdapter extends FragmentPagerAdapter 
		implements OnPageChangeListener, TabListener {
	private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    
    public TabsAdapter(FragmentManager fm, Activity activity, ViewPager pager) {
        super(fm);
        mContext = activity;
        mActionBar = activity.getActionBar();
        mViewPager = pager;
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(this);
    }

 
    @Override
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            // Info fragment activity
            return Fragment.instantiate(mContext, InfoFragment.class.getName(), null);
        case 1:
            // Menu fragment activity
            return Fragment.instantiate(mContext, MenuFragment.class.getName(), null);
        }
        return null;
    }
    
	
    public void addTab(String name) {
    	mActionBar.addTab(mActionBar.newTab().setText(name)
                    .setTabListener(this)); 
    }
     
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
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
        mActionBar.setSelectedNavigationItem(position);
	}
    
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

}