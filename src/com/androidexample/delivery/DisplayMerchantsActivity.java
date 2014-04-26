package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.HomeActivity.MerchantData;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * This class displays the list of restaurants
 * based on the search address input
 * @author brian & lam
 *
 */
public class DisplayMerchantsActivity extends BaseActivity {
	
	Button btnBack, btnFilter;
	private String[] sortTypes = {"Distance", "Name", "Ratings"};
	public static final int FILTER_MERCHANTS = 5;
	
	private String merchantID;	
	// arraylist of merchants
	private static ArrayList<Merchant> merchantArray = MerchantData.getMerchantList();
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
		ActionBar actionbar = getActionBar();
	    actionbar.setCustomView(R.layout.actionbar_top_display_merchants_activity);
	    actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    
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
				filterMerchants();
			}
		});

	    // Get the search result
//		merchantArray = MerchantData.getMerchantList();
//		adapter = new MerchantCustomAdapter(this, R.layout.list_merchant,
//				 merchantArray);
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
			// SearchMerchants search = new SearchMerchants("input here");
			MenuData.setResult(SearchMerchants.search(merchantID, 1));
			try {
				JSONArray mArray = MerchantData.getResult().getJSONArray("merchants");
				if (MenuData.getResult().has("menu") || !merchantArray.isEmpty()) {
					String merchantInfo = "";
					found = true;
					if (!(merchantArray.size() == 0))
					{
						for (int i = 0; i < merchantArray.size(); i++)
						{
							int temp = merchantArray.get(i).getID();
							if (Integer.parseInt(merchantID)== temp)
								merchantInfo = mArray.getJSONObject(i).toString();
						}
						MenuData.setInfo(merchantInfo);
					}
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
			// Dismiss the progress dialog
			dialog.hide();
			//hideLoading();

			if (found) {
				Intent in = new Intent(getApplicationContext(), DisplayMenuActivity.class); 
				startActivity(in);	
				overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left);
			}
			else { // Display error message, need to get merchant error message too
				String msg = "";
				try {
					msg = MenuData.getResult().getJSONObject("message").getJSONObject("user_msg").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AlertDialog.Builder adb = new AlertDialog.Builder(DisplayMerchantsActivity.this);
				adb.setTitle("Error");
				adb.setMessage(msg);
				adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}      
				});
				adb.show();
			}
		}
	}
	
	public static class MenuData {
		private static JSONObject result;
		private static String merchantInfo;
		
		public static void setResult(String s) {
			try {
				result = new JSONObject(s);
			} catch (JSONException e) {e.printStackTrace();}
		}
		public static JSONObject getResult() {return result;}
		
		public static void setInfo(String s) {merchantInfo = s;}
		public static String getInfo() {return merchantInfo;}
		
	    public static void changeList(ArrayList<Merchant> newOne) {
	    	merchantArray = newOne;
	    	adapter.notifyDataSetChanged();
	    }
	}
	
    // filter button
    private void filterMerchants() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMerchantsActivity.this)
//		.setTitle("Sort merchants by:")
//		.setSingleChoiceItems(sortTypes, -1, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
////				txtMaxDistance.setText(itemsMaxDistance[which]); // text to show current choice
////				selectedMaxDistance = itemsMaxDistance[which].equals("All")? 10000: Integer.parseInt(itemsMaxDistance[which].replace(",", "").split(" ")[0]);
//				dialog.dismiss();
//			}
//		});
//		AlertDialog alert = builder.create();   
//		alert.show();
    	
    	Intent i = new Intent(getApplicationContext(), SortMerchantsActivity.class);
        startActivityForResult(i, FILTER_MERCHANTS, true);
	
    }
}