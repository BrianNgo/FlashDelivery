package com.androidexample.delivery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new SearchFragment();
		case 1:
			return new OrderFragment();
		case 2: 
			return new AccountFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
