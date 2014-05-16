package com.androidexample.delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.androidexample.delivery.SearchFragment.MerchantData;

/**
 * This class displays the list of restaurants
 * based on the search address input
 * @author brian & lam
 *
 */
public class DisplayMerchantsActivity extends BaseActivity {
	
	Button btnBack, btnFilter, btnCart, btnAccount;
	private String[] sort = {"Closest Distance", "The Best Ratings", "Now Open", "Name, A-z", "Name, Z-a"};
	public static final int FILTER_MERCHANTS = 5;
	
	private String merchantID;	
	// arraylist of merchants
	private static ArrayList<Merchant> merchantArray = new ArrayList<Merchant>();
	private static MerchantCustomAdapter adapter = null;

	/**
	 * The onCreate method displays the list of available 
	 * merchants and calls the SingleMerchantActivity whenever
	 * a specific merchant is chosen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_merchants);
		
		// top bar
		ActionBar actionBarTop = getActionBar();
	    actionBarTop.setCustomView(R.layout.actionbar_top_display_merchants_activity);
	    actionBarTop.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    
	    
	    // buttons in this activity
	    btnBack = (Button) findViewById(R.id.btnBack);
	    btnFilter = (Button) findViewById(R.id.btnFilterMerchants);


	    
	    // handle events for buttons
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	    
	    
	    // filter button
	    btnFilter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
		        AlertDialog.Builder alert = new AlertDialog.Builder(DisplayMerchantsActivity.this);
		        alert.setTitle("Sort By ");
		        alert.setSingleChoiceItems(sort, -1,
		                new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
	            		sortMerchants(which);
	            		dialog.cancel();
		            }
		        });
		        alert.show();
			}
		});

	    // Get the search result
	    merchantArray = MerchantData.getMerchantList();
	    adapter = new MerchantCustomAdapter(this, R.layout.list_merchant,
				 merchantArray);

		ListView mList = (ListView) findViewById(R.id.listView);
		
		mList.setItemsCanFocus(false);
		mList.setAdapter(adapter);	


		// Listview on item click listener
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				merchantID = merchantArray.get(position).getID() + "";
				MenuData.setMerchantId(merchantID);
				new GetMenu().execute();
			}
		});
	}		
		
	private class GetMenu extends AsyncTask<Integer, Void, Void> {
		// custom dialog
		final Dialog dialog = new Dialog(DisplayMerchantsActivity.this);
	    private boolean found = false;
		/**
		 * The onPreExecute method display the waiting message
		 * while the program is executing the search function
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// custom dialog
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_progress_dialog);
			dialog.setCancelable(false);
			dialog.show(); 
			//showLoading();
		}

		/**
		 * The doInBackGround method creates the searchRestaurant
		 * object and gets the list of available merchants 
		 */
		@Override
		protected Void doInBackground(Integer... arg) {
			MenuData.setMenu(ServerInteract.search(merchantID, 1));
			try {
				JSONArray mArray = MerchantData.getResult().getJSONArray("merchants");
				if (MenuData.getMenu().has("menu") || mArray.length() != 0) {
					String merchantInfo = "";
					found = false;
					for (int i = 0; i < mArray.length(); i++)
					{
						int temp = mArray.getJSONObject(i).getInt("id");
						if (Integer.parseInt(merchantID) == temp) {
							merchantInfo = mArray.getJSONObject(i).toString();
							found = true;
							break;
						}
					}
					MenuData.setInfo(merchantInfo);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * The onPostExecute views the list of merchants found
		 * after the searching method had successfully executed
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (found) {
				Intent in = new Intent(getApplicationContext(), DisplayMenuActivity.class); 
				startActivity(in);	
				overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
			}
			else { // Display error message, need to get merchant error message too
				String msg = "";
				try {
					msg = MenuData.getMenu().getJSONObject("message").getJSONObject("user_msg").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AlertDialog.Builder alert = new AlertDialog.Builder(DisplayMerchantsActivity.this);
				alert.setTitle("Error");
				alert.setMessage(msg);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}      
				});
				alert.show();
			}
		}
	}
	
	public static class MenuData {
		private static JSONObject menu, subMenu;
		private static JSONArray dishes;
		private static String merchantInfo;
		private static String merchantId;
		
		public static void setMenu(String s) {
			try {
				menu = new JSONObject(s);
			} catch (JSONException e) {e.printStackTrace();}
		}
		public static JSONObject getMenu() {return menu;}
		

		public static void setSubMenu(JSONObject s) {subMenu = s;}
		public static JSONObject getSubMenu() {return subMenu;}
		
		public static void setDishes(JSONArray s) {dishes = s;}
		public static JSONArray getDishes() {return dishes;}
		
		public static void setInfo(String s) {merchantInfo = s;}
		public static String getInfo() {return merchantInfo;}
		
		public static void setMerchantId(String s) {merchantId = s;}
		public static String getMerchantId() {return merchantId;}
	}
	
	private void sortMerchants(int selection) {
		switch (selection) {
			case 0:	Collections.sort(merchantArray);
					break;
			case 1:	Collections.sort(merchantArray, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m2.rating - m1.rating;
						}
					});
					break;
			case 2: Collections.sort(merchantArray, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							if (m1.status && m2.status) return 0;
							else if (m1.status) return -1;
							else if (m2.status) return 1;
							else return 0;
						}
					});
					break;
			case 3:	Collections.sort(merchantArray, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m1.name.toLowerCase().compareTo(m2.name.toLowerCase());
						}
    				});
					break;
			case 4: Collections.sort(merchantArray, new Comparator<Merchant>() {
						@Override
						public int compare(Merchant  m1, Merchant  m2) {
							return  m2.name.toLowerCase().compareTo(m1.name.toLowerCase());
						}
					});
					break;
			default: break;
		}
		adapter.notifyDataSetChanged();
	}
}