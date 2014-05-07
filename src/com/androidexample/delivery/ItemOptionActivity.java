package com.androidexample.delivery;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ItemOptionActivity extends BaseActivity {

	Button btnBack, btnAddCart, btnPlus, btnMinus;
	private ItemOptionAdapter adapter = null;
	private ArrayList<JSONObject> optionList = new ArrayList<JSONObject>();
	JSONArray itemOption = null;
	private Context c = this;
	private int maxQty, minQty, quantity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_option);
		
		// top bar
		ActionBar actionBarTop = getActionBar();
	    actionBarTop.setCustomView(R.layout.actionbar_top_item_option_activity);
	    actionBarTop.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		final TextView textView1 = (TextView) findViewById(R.id.quantity);
	    TextView textView2 = (TextView) findViewById(R.id.desc);
	    TextView textView3 = (TextView) findViewById(R.id.price);
	    TextView textView4 = (TextView) findViewById(R.id.name);
	    
	    // buttons in this activity
	    btnBack = (Button) findViewById(R.id.btnBack);
	    btnAddCart = (Button) findViewById(R.id.btnAddCart);
	    btnPlus = (Button) findViewById(R.id.btnPlus);
	    btnMinus = (Button) findViewById(R.id.btnMinus);
	    
	    // handle events for buttons
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	   		
	    
	    btnAddCart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// call fragment order
			}
		});
	    
	    btnPlus.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (quantity < maxQty)
					quantity++;
			    textView1.setText(quantity + "");
			}
		});
	    
	    btnMinus.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (quantity > minQty)
					quantity--;
			    textView1.setText(quantity + "");
			}
		});
	    
		Intent in = getIntent();
		String option = in.getStringExtra("option");
		String itemName = in.getStringExtra("name");
		String desc = in.getStringExtra("desc");
		String price = in.getStringExtra("price");
		maxQty = Integer.parseInt(in.getStringExtra("maxQty"));
		minQty = Integer.parseInt(in.getStringExtra("minQty"));
		quantity = minQty;
	    
	    textView1.setText(minQty + "");
	    textView2.setText(desc.equals("")?"No Description":"Description: " + desc);
	    textView3.setText("Base price: $" + price);
	    textView4.setText(itemName);
	    Cart.setPrice(Double.parseDouble(price));
	    
		try	{
			itemOption = new JSONArray(option);
			for (int i = 0; i < itemOption.length(); i++)
				optionList.add(itemOption.getJSONObject(i));
		}
		catch (JSONException e) {e.printStackTrace();}
		
		
		if (!optionList.isEmpty()) {
			adapter = new ItemOptionAdapter(this, R.layout.list_option,
					optionList);
			ListView list = (ListView) findViewById(R.id.listView);
			list.setAdapter(adapter);
			
			list.setOnItemClickListener(new OnItemClickListener() {

				ArrayList<Integer> mSelectedOptions;
				int singleSelection;
				int min, max;
				ArrayList<String> option;
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					final View v = view;
					JSONArray oList;
					try {
						min = optionList.get(position).getInt("min_selection");
						max = optionList.get(position).getInt("max_selection");
						oList = optionList.get(position).getJSONArray("children");
						option = new ArrayList<String>();
						for (int i = 0; i < oList.length(); i++) {
							option.add(oList.getJSONObject(i).getString("name")
									+ "\t Price: $" + oList.getJSONObject(i).getInt("price"));
						}

						AlertDialog.Builder alert = new AlertDialog.Builder(c);
						alert.setTitle("Select | Min: " + min + " | Max: " + max);
						Log.i("MAx: " + max, "the freak");
						if (max > 1) {
							mSelectedOptions = new ArrayList<Integer>();
							String myList[] = option.toArray(new String[option.size()]);
							alert.setMultiChoiceItems(myList, null,
									new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {
									if (isChecked) {
										// If the user checked the item, add it to the selected items
										mSelectedOptions.add(which);
									} else if (mSelectedOptions.contains(which)) {
										// Else, if the item is already in the array, remove it 
										mSelectedOptions.remove(Integer.valueOf(which));
									}
								}
							});
						}
						else {
							String myList[] = option.toArray(new String[option.size()]);
							singleSelection = -1;
							if (min > 0) {
								alert.setSingleChoiceItems(myList, 0,
										new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										singleSelection = which;
									}
								});
							}
							else {
								alert.setSingleChoiceItems(myList, -1,
										new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										singleSelection = which;
									}
								});
							}
						}

						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String temp = "";
								if (max > 1) {
									for (int i = 0; i < mSelectedOptions.size(); i++)
										temp = temp + "\n - " + option.get(mSelectedOptions.get(i));
								}
								else {
									if (singleSelection == -1)
										temp = "";
									else
										temp = option.get(singleSelection);
								}
								
								TextView textView5 = (TextView) v.findViewById(R.id.choice);
								textView5.setText("Choice: " + temp);
								dialog.cancel();
							}      
						});
						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}      
						});
						alert.show();
					}	catch (Exception e) {e.printStackTrace();}
				} 
			});
			
		}
	}
	
	public static class Cart {
		private static String opt = "";
		private static double totalPrice = 0;
		
		public static void setOption(String s) {opt = s;}
		public static String getOption(String s) {return opt;}
		
		public static void setPrice(double p) {totalPrice = p;}
		public static double getPrice() {return totalPrice;}
	}
}
