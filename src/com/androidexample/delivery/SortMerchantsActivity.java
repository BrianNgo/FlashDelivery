package com.androidexample.delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;
import com.androidexample.delivery.HomeActivity.MerchantData;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SortMerchantsActivity extends BaseActivity {
	private String[] itemsCuisines = {"All", "American","Indian","Chinese"};
	private String[] itemsNames = {"Alpha", "Beta","Gamma"};
	private String[] itemsDistance = {"5 miles", "10 miles", "20 miles", "30 miles", "All"};
	
	private String selectedCuisine;
	private String selectedName;
	private int selectedDistance;

	private ArrayList<Merchant> temp = new ArrayList<Merchant>();
	
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchants_filter);
		
		getActionBar().setCustomView(R.layout.actionbar_top_sort_merchant_activity);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		selectedCuisine = itemsCuisines[1]; ///app.getPreference(ChatApp.PREF_MOMENTS_FILTER_SEARCH_TYPE).toString();
		selectedName = itemsNames[0]; //app.getPreference(ChatApp.PREF_MOMENTS_FILTER_SORT_BY).toString();
		selectedDistance = itemsDistance[2].equals("All")? 10000: Integer.parseInt(itemsDistance[2].replace(",", "").split(" ")[0]);; //Integer.parseInt(app.getPreference(ChatApp.PREF_MOMENTS_FILTER_MAX_DISTANCE).toString());
		
		final TextView txtCuisine = (TextView)findViewById(R.id.txt_cuisine);
		final TextView txtName = (TextView)findViewById(R.id.txt_name);
		final TextView txtDistance = (TextView)findViewById(R.id.txt_distance);
		
		txtCuisine.setText(selectedName);
		txtName.setText(selectedCuisine);
		
		//back btn
		btnBack = (Button) findViewById(R.id.btnBackSortMerchant);
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	  
		
		Button btnSortByCuisine = (Button)findViewById(R.id.btn_sort_by_cuisine);
		Button btnSortByName = (Button)findViewById(R.id.btn_sort_by_name);
		Button btnSortByDistance = (Button)findViewById(R.id.btn_sort_by_distance);
		Button btnApply = (Button)findViewById(R.id.btn_apply);
		
		btnSortByCuisine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SortMerchantsActivity.this)
				.setTitle("Select cuisine:")
				.setSingleChoiceItems(itemsCuisines, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtCuisine.setText(itemsCuisines[which]);
						selectedCuisine = itemsCuisines[which];
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();   
				alert.show();
			}
		});
		
		btnSortByName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SortMerchantsActivity.this)
				.setTitle("Select sort order:")
				.setSingleChoiceItems(itemsNames, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtName.setText(itemsNames[which]);
						selectedName = itemsNames[which];
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();   
				alert.show();
				// sort by name in alphabet order
				if (selectedName.equals(itemsNames[0])) {
					temp = MerchantData.getMerchantList();
					Collections.sort(temp, new Comparator<Merchant>() {
				        @Override
				        public int compare(Merchant  m1, Merchant  m2)
				        {
				            return  m1.name.compareTo(m2.name);
				        }
				    });
					MenuData.changeList(temp);
				}
			}
		});
		
		btnSortByDistance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SortMerchantsActivity.this)
				.setTitle("Select max distance:")
				.setSingleChoiceItems(itemsDistance, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtDistance.setText(itemsDistance[which]);
						selectedDistance = itemsDistance[which].equals("All")? 10000: Integer.parseInt(itemsDistance[which].replace(",", "").split(" ")[0]);
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();   
				alert.show();
			}
		});
		
		btnApply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				apply();
			}
		});
	}
	
	private void apply() {
		setResult(RESULT_OK);
		finish(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
