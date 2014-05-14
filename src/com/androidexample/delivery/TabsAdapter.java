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
import android.util.SparseArray;
import android.view.ViewGroup;
 
public class TabsAdapter extends FragmentPagerAdapter 
		implements OnPageChangeListener, TabListener {
	private SparseArray<Fragment> fragments = new SparseArray<Fragment>();   
	private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final int which;
    
    public TabsAdapter(FragmentManager fm, Activity activity, ViewPager pager, int which) {
        super(fm);
        mContext = activity;
        mActionBar = activity.getActionBar();
        mViewPager = pager;
        this.which = which;
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(this);
    }
 
    public Fragment getItem(int index) {
    	if (which == 0)
    		switch (index) {
    		case 0:
    			// Info fragment activity
    			return Fragment.instantiate(mContext, InfoFragment.class.getName(), null);
    		case 1:
    			// Menu fragment activity
    			return Fragment.instantiate(mContext, MenuFragment.class.getName(), null);
    	}
    	else if (which == 1)
       		switch (index) {
    		case 0:
    			Fragment f1 = Fragment.instantiate(mContext, SubMenuFragment.class.getName(), null);
    			fragments.put(index, f1);
    			return f1;
    		case 1:
    			Fragment f2 = Fragment.instantiate(mContext, DishFragment.class.getName(), null);
    			fragments.put(index, f2);
    			return f2;
    	}
    	else if (which == 2)
       		switch (index) {
    		case 0:
    			return Fragment.instantiate(mContext, ItemFragment.class.getName(), null);
    		case 1:
    			return Fragment.instantiate(mContext, OptionFragment.class.getName(), null);
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
    
    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

}