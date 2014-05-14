package com.androidexample.delivery;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * This class display the search screen where the app
 * receives users' input and login information
 * @author brian & lam & quang
 *
 */
public class SearchFragment extends BaseFragment {
	
	public SearchFragment() {
		super("Search", R.layout.fragment_search,R.layout.actionbar_top_search, 0); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
	}
	
	public static SearchFragment fragment;
	// Hold the user's input
    final static String SEARCH_ADDRESS = "1330 1st Ave, 10021";
	private String address = "";
	// Search button
    private Button btnSearch;	
    private ImageButton toggle_location;    
    

	@Override
	public void init(View view) {		
		// buttons
		btnSearch = (Button)view.findViewById(R.id.search_btn);
		
		// Set the button the listen the click event
		final EditText text = (EditText)view.findViewById(R.id.search_box);
		btnSearch.setOnClickListener(new OnClickListener() {
			// Calling the event
			@Override
			public void onClick(View v) {
				btnSearchPressed(text);
			}
		});
		
		// toggle location
		toggle_location = (ImageButton)view.findViewById(R.id.toogle_arrow);
		toggle_location.setBackground(null);
		toggle_location.setOnClickListener(new OnClickListener() {
			// Calling the event
			@Override
			public void onClick(View v) {
				viewToggleLocation(v);
			}
		});
		
		
	}
	
    /**
     * The onCreate method displays the search screen,
     * sets the search button to listen to the click event
     * which will then trigger the Async task
     */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.home_activity); 
//		
//		// top bar
//		ActionBar actionbar = getActionBar();
//	    actionbar.setCustomView(R.layout.actionbar_top_delivery_main_activity);
//	    actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//	    
//	    progressIcon =  (ImageView)findViewById(R.id.icon);
//	    Animation a = AnimationUtils.loadAnimation(this, R.anim.progress_anim);
//	    a.setInterpolator(new Interpolator() {
//	        private final int frameCount = 8;
//
//	        @Override
//	        public float getInterpolation(float input) {
//	            return (float)Math.floor(input*frameCount)/frameCount;
//	        }
//	    });
//	    a.setDuration(1000);
//	    
//	    
//		
//	    // share pref to check if app is opened 1st time
//	    SharedPreferences settings = getSharedPreferences("prefs", 0);
//	    SharedPreferences.Editor editor = settings.edit();
//	    editor.putBoolean("firstRun", false);
//	    editor.commit();
//
//	    boolean firstRun = settings.getBoolean("firstRun", true);
//	    Log.d("TAG1", "firstRun: " + Boolean.valueOf(firstRun).toString());
//		
//		// buttons
//		btnSearch = (Button)findViewById(R.id.search_btn);
//		
//		// Set the button the listen the click event
//		btnSearch.setOnClickListener(new OnClickListener() {
//			// Calling the event
//			@Override
//			public void onClick(View v) {
//				btnSearchPressed();
//			}
//		});
//		
//		// toggle location
//		toggle_location = (ImageButton)findViewById(R.id.toogle_arrow);
//		toggle_location.setBackground(null);
//		toggle_location.setOnClickListener(new OnClickListener() {
//			// Calling the event
//			@Override
//			public void onClick(View v) {
//				viewToggleLocation(v);
//			}
//		});
//		
//	}
	
	// handle toggle location
	private void viewToggleLocation(View v) {		
		if (v.isSelected()){
			v.setSelected(false);			
			//...location off		
			Toast.makeText(getApplicationContext(), "Turn off location, input manually", Toast.LENGTH_SHORT).show();
		} else {
			v.setSelected(true);
			//... location on	
			Toast.makeText(getApplicationContext(), "Turn on location, address is shown", Toast.LENGTH_SHORT).show();
		}
	}

	
	// handle search button
	public void btnSearchPressed(EditText text) {	    
		// Calling async task 
		
		address = text.getText().toString();
		Log.i("address = " + address, "**********************************");
		if (address.equals("")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
			alert.setTitle("Error");
			alert.setMessage("Please enter an address!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}      
			});
			alert.show();
		} else {	
			new GetContacts().execute();
		}	
	}
	
	/**
	 * The MerchantData class holds the search result as a string
	 * for easy access from other activity
	 * @param 
	 */
	public static class MerchantData {
		private static String orderType = "delivery";
		private static JSONObject searchResult;
		private static ArrayList<Merchant> merchants;
		private static JSONObject geoCodedLocation;


		public static void setOrderType(String s) {orderType = s;}
		public static String getOrderType() {return orderType;}
		
		public static void setResult(String s) {
			try {
				searchResult = new JSONObject(s);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static JSONObject getResult() {return searchResult;}
		
		public static void setMerchantList(ArrayList<Merchant> m) {merchants = m;}
		public static ArrayList<Merchant> getMerchantList() {return merchants;}
		
		public static void setAddress() {
			try {
				geoCodedLocation = searchResult.getJSONObject("search_address");
			} catch (JSONException e) {e.printStackTrace();}
		}
		public static JSONObject getAddress() {return geoCodedLocation;}
	}
	
	/**
	 * The viewResult methods call the DisplayRestaurant activity 
	 * to display the list of merchants in the proper format 
	 * @param
	 */
	public void viewResult() {
		JSONObject searchResult = MerchantData.getResult();
		try {
			if (searchResult.getJSONArray("merchants").length() != 0) {
				Intent i = new Intent(getApplicationContext(), DisplayMerchantsActivity.class); 
				startActivity(i);	
			}
			else {

				// Display error message
				String msg = "Unknown Error";
				try {
					msg = searchResult.getJSONArray("message").getJSONObject(0).getString("user_msg");
				} catch (JSONException a) {
					// TODO Auto-generated catch block
					a.printStackTrace();
				}
				AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
				alert.setTitle("Error");
				alert.setMessage(msg);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}      
				});
				alert.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * The getContacts method is a Async task which will 
	 * create searchRestaurant object to get data from
	 * delivery server
	 * @author brian & lam
	 *
	 */
	private class GetContacts extends AsyncTask<Void, Void, Void> {
		
		// custom dialog
		final Dialog dialog = new Dialog(getHomeActivity());
		
		/**
		 * The onPreExecute method display the waiting message
		 * while the program is executing the search function
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing custom progress view (background not disabled)
			//showLoading();
			
			// custom dialog
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_progress_dialog);
			dialog.setCancelable(false);
			dialog.show();
		}

		/**
		 * The doInBackGround method creates the searchRestaurant
		 * object and gets the list of available merchants 
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			
			// SearchMerchants search = new SearchMerchants("input here");
			MerchantData.setResult(ServerInteract.search(SEARCH_ADDRESS, 0));
			MerchantData.setMerchantList(ServerInteract.createMerList(MerchantData.getResult()));
			MerchantData.setAddress();
			return null;
		}

		/**
		 * The onPostExecute views the list of merchants found
		 * after the searching method had successfully executed
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Showing custom progress view (background not disabled)
			// hideLoading();
			
			// custom dialog
			dialog.hide();
			viewResult();
		}

	}
	
//	@Override
//	public void onBackPressed() {
//		// back button on device
//		// empty is disabled
//		//finish();
//	}

}
