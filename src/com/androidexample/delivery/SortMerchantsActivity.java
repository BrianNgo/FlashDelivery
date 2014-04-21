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
	private String selectedCuisine = "";
	private int sortCase = 0; // standard case

	private ArrayList<Merchant> temp = new ArrayList<Merchant>();
	
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchants_filter);
		
		getActionBar().setCustomView(R.layout.actionbar_top_sort_merchant_activity);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		//back btn
		btnBack = (Button) findViewById(R.id.btnBackSortMerchant);
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	  

	    final Button btnSortByStandard = (Button)findViewById(R.id.btn_sort_by_standard);
		final Button btnSortByBest = (Button) findViewById(R.id.btn_sort_by_rating);
		final Button btnSortByClosest = (Button)findViewById(R.id.btn_sort_by_distance);
		final Button btnSortByStatus = (Button) findViewById(R.id.btn_sort_by_status);
		final Button btnSortByNameAZ = (Button)findViewById(R.id.btn_sort_by_nameAZ);
		final Button btnSortByNameZA = (Button)findViewById(R.id.btn_sort_by_nameZA);		
		final Button btnSortByCuisine = (Button)findViewById(R.id.btn_sort_by_cuisine);
		final Button btnApply = (Button)findViewById(R.id.btn_apply);
	
		btnSortByStandard.setSelected(true);
		btnSortByStandard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					btnSortByStandard.setSelected(true);
					btnSortByBest.setSelected(false);
					btnSortByClosest.setSelected(false);
					btnSortByStatus.setSelected(false);
					btnSortByNameAZ.setSelected(false);
					btnSortByNameZA.setSelected(false);
			}
		});
		
		btnSortByBest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortCase = 1;
				btnSortByStandard.setSelected(false);
				btnSortByBest.setSelected(true);
				btnSortByClosest.setSelected(false);
				btnSortByStatus.setSelected(false);
				btnSortByNameAZ.setSelected(false);
				btnSortByNameZA.setSelected(false);
			}
		});
				
		btnSortByClosest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortCase = 2;
				btnSortByStandard.setSelected(false);
				btnSortByBest.setSelected(false);
				btnSortByClosest.setSelected(true);
				btnSortByStatus.setSelected(false);
				btnSortByNameAZ.setSelected(false);
				btnSortByNameZA.setSelected(false);
			}
		});

		btnSortByStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					sortCase = 3;
					btnSortByStandard.setSelected(false);
					btnSortByBest.setSelected(false);
					btnSortByClosest.setSelected(false);
					btnSortByStatus.setSelected(true);
					btnSortByNameAZ.setSelected(false);
					btnSortByNameZA.setSelected(false);
			}
		});		
		
		btnSortByNameAZ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortCase = 4;
				btnSortByStandard.setSelected(false);
				btnSortByBest.setSelected(false);
				btnSortByClosest.setSelected(false);
				btnSortByStatus.setSelected(false);
				btnSortByNameAZ.setSelected(true);
				btnSortByNameZA.setSelected(false);
			}
		});
		
		btnSortByNameZA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortCase = 5;
				btnSortByStandard.setSelected(false);
				btnSortByBest.setSelected(false);
				btnSortByClosest.setSelected(false);
				btnSortByStatus.setSelected(false);
				btnSortByNameAZ.setSelected(false);
				btnSortByNameZA.setSelected(true);
			}
		});

		final TextView txtCuisine = (TextView) findViewById(R.id.txt_cuisine);
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
		
		btnApply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				apply();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void apply() {
		temp = MerchantData.getMerchantList();
		if (!selectedCuisine.equals("")) {
			boolean found = false;
			for (int i = 0; i < temp.size(); i++) {
				String[] cuisine = temp.get(i).getCuisine().split(",");
				for (String s: cuisine)
					if (s.equals(selectedCuisine))
						found = true;
				if (!found)
					temp.remove(i);
			}
		}
		switch (sortCase) {
			case 1:	Collections.sort(temp, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m2.rating - m1.rating;
						}
					});
					break;
			case 2:	Collections.sort(temp);
					break;
			case 3: Collections.sort(temp, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							if (m1.status && m2.status) return 0;
							else if (m1.status) return -1;
							else if (m2.status) return 1;
							else return 0;
						}
					});
			break;
			case 4:	Collections.sort(temp, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m1.name.toLowerCase().compareTo(m2.name.toLowerCase());
						}
	    			});
					break;
			case 5: Collections.sort(temp, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m2.name.toLowerCase().compareTo(m1.name.toLowerCase());
						}
					});
					break;
			default: break;
		}
		MenuData.changeList(temp);
		setResult(RESULT_OK);
		finish(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
