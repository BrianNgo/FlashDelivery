package com.androidexample.delivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseActivity extends FragmentActivity {


//	public boolean onCreateOptionsMenu(Menu menu) {
//		return true;
//	}
	
	public void startActivity(Intent intent, boolean animate) {
		super.startActivity(intent);
		if(animate)
			overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
	}
	
	public void startActivityForResult(Intent intent, int requestCode, boolean animate) {
		super.startActivityForResult(intent, requestCode);
		if(animate)
			overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
	}
	
	public void finish(boolean animate) {
		super.finish();
		if(animate)
			overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish(true);
	}
	
	public void back(View v) {
		onBackPressed();
	}
	
	public void showLoading() {
		View loading = (View)findViewById(R.id.loading_indicator);
		loading.setVisibility(View.VISIBLE);
	}
	
	public void hideLoading() {
		View loading = (View)findViewById(R.id.loading_indicator);
		loading.setVisibility(View.GONE);
	}
	
	public void showKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}
	
	public void hideKeyboard(IBinder token) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(token, 0);
	}
	
	public void showSeriesMessages(final Context context, final String title, final String[] messages, final int messageIndex) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(messages[messageIndex]);
		alertBuilder.setPositiveButton(messageIndex == messages.length-1? "Ok": "Next", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(messageIndex < messages.length-1)
					showSeriesMessages(context, title, messages, messageIndex+1);
			}
		});
		alertBuilder.create().show();
	}

}
