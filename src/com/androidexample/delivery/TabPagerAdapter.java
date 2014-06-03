package com.androidexample.delivery;

import java.util.ArrayList;

import com.androidexample.delivery.HomeActivity.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<String> t;
	
	public TabPagerAdapter(FragmentManager fm, ArrayList<String> tab) {
		super(fm);
		t = tab;
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new SearchFragment();
		case 1:
			return new OrderFragment();
		case 2:
			if (Home.getLoginTag())
				return new LoginFragment();
			else
				return new AccountFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return t.size();
	}
	
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
