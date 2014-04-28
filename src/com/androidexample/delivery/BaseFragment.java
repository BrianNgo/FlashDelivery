package com.androidexample.delivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class BaseFragment extends Fragment implements OnPageSelectedListener {
	protected String title;
	protected HomeActivity homeActivtiy;
	protected View fragmentView, actionBarView;
	protected int index;
	protected int layoutId, actionBarId;
	protected boolean stacked;

	public BaseFragment(String title, int layoutId, int actionBarId, int index) {
		this.title = title;
		this.layoutId = layoutId;
		this.actionBarId = actionBarId;
		this.index = index;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		homeActivtiy = (HomeActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public Context getApplicationContext() {
		return homeActivtiy.getApplicationContext();
	}

	public void startActivity(Intent intent, boolean animate) {
		super.startActivity(intent);
		if (animate)
			homeActivtiy.overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
	}

	public void startActivityForResult(Intent intent, int requestCode, boolean animate) {
		super.startActivityForResult(intent, requestCode);
		if (animate)
			homeActivtiy.overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(layoutId, container, false);
		actionBarView = inflater.inflate(actionBarId, null);
		init(fragmentView);
		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (homeActivtiy.getCurrentTabIndex() == index) {
			setActionBar();
		}
	}
	
	public void onHideFragment() {
		
	}

	public void setActionBar() {
		homeActivtiy.setActionBar(actionBarView);
	}

	public void init(View view) {

	}

	public void click(int id) {

	}

	public HomeActivity getHomeActivity() {
		return homeActivtiy;
	}

	public void showLoading() {
		try {
			View loading = (View) getView().findViewById(R.id.loading_indicator);
			loading.setVisibility(View.VISIBLE);
		} catch (Exception ex) {
		}
	}

	public void hideLoading() {
		try {
			View loading = (View) getView().findViewById(R.id.loading_indicator);
			loading.setVisibility(View.GONE);
		} catch (Exception ex) {
		}
	}

	public void showKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) homeActivtiy.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

	public void hideKeyboard(IBinder token) {
		InputMethodManager imm = (InputMethodManager) homeActivtiy.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(token, 0);
	}

	@Override
	public void onPageSelected() {
		
	}
}
