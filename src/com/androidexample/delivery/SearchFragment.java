package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.HomeActivity.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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
	private String address = "";
    private Button btnSearch;	
    private ImageButton toggle_location;    
    private boolean gpsTag = false;
    private EditText addr;
    

	@Override
	public void init(View view) {		
		// buttons
		btnSearch = (Button)view.findViewById(R.id.search_btn);
		
		addr = (EditText) view.findViewById(R.id.search_box);
		final EditText zip = (EditText) view.findViewById(R.id.search_box_zip);
		
		// checkboxes
        final CheckBox delivery = (CheckBox) view.findViewById(R.id.delivery);
        final CheckBox pickup = (CheckBox) view.findViewById(R.id.pickup);
		
        delivery.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (((CheckBox) v).isChecked()) {
        			if (pickup.isChecked())
        				pickup.setChecked(false);
        			MerchantData.setOrderType("delivery");
        		}
        		else {
        			if (!pickup.isChecked())
        				delivery.setChecked(true);
        			MerchantData.setOrderType("delivery");
        		}
        	}
        });
        
        pickup.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (((CheckBox) v).isChecked()) {
        			if (delivery.isChecked())
        				delivery.setChecked(false);
        			MerchantData.setOrderType("pickup");
        		}
        		else {
        			if (!delivery.isChecked())
        				pickup.setChecked(true);
        			MerchantData.setOrderType("pickup");
        		}
        	}
        });

		// Set the button the listen the click event		
		btnSearch.setOnClickListener(new OnClickListener() {
			// Calling the event
			@Override
			public void onClick(View v) {
				btnSearchPressed(addr, zip);
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
	
	// handle toggle location
	private void viewToggleLocation(View v) {		
		if (v.isSelected()){
			v.setSelected(false);
			addr.setText("");
			gpsTag = false;
			//...location off		
			Toast.makeText(getApplicationContext(), "Turn off GPS, input manually", Toast.LENGTH_SHORT).show();
		} else {
			v.setSelected(true);
			LocationManager locManager = (LocationManager) Home.getHomeContext().getSystemService(Context.LOCATION_SERVICE);
			boolean enabled = locManager
			  .isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!enabled) {
				gpsTag = false;
				v.setSelected(false);
				AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
				alert.setTitle("GPS not enable!");
				alert.setMessage("Please go to settings and enable GPS or manually enter your address!");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}      
				});
				alert.show();
			}
			else {
				Criteria criteria = new Criteria();
			    String provider = locManager.getBestProvider(criteria, false); // can change gps only
			    LocationListener mLocationListener = new LocationListener() {
			        public void onLocationChanged(Location location) {}
			        public void onStatusChanged(String provider, int status, Bundle extras) {}
			        public void onProviderEnabled(String provider) {}
			        public void onProviderDisabled(String provider) {}
			    };

				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
				locManager.removeUpdates(mLocationListener);
			    Location location = locManager.getLastKnownLocation(provider);
				//... location on
			    if (location != null) {
			    	addr.setText("Estimated Location...");
					gpsTag = true;
			    	ServerInteract.setLat(location.getLatitude());
			    	ServerInteract.setLong(location.getLongitude());
			    }
			    else {
					gpsTag = false;
					v.setSelected(false);
					AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
					alert.setTitle("No Location Providers!");
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}      
					});
					alert.show();
			    }
			}
		}
	}

	// handle search button
	public void btnSearchPressed(EditText addr, EditText zip) {	    
		// Calling async task 
		address = addr.getText().toString() + zip.getText().toString();
		Log.i("address = " + address, "**********************************");
		if (address.equals("") && !gpsTag) {
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
	
    public static ArrayList<Merchant> createMerList(JSONObject result) {

        ArrayList<Merchant> m = new ArrayList<Merchant>();
        try {
            JSONObject searchResult = result;
            JSONArray mArray = searchResult.getJSONArray("merchants");

            // variables that hold info. for a merchant
            String name, cuisine;
            boolean status;
            int id, rating;
            double distance;
            // create array of merchants
            for (int i = 0; i < mArray.length(); i++)
            {
                if (mArray.getJSONObject(i).getJSONObject("summary").get("type").equals("R")) {
                    cuisine = "";
                    name = mArray.getJSONObject(i).getJSONObject("summary").getString("name");
                    id = mArray.getJSONObject(i).getInt("id");
                    rating = mArray.getJSONObject(i).getJSONObject("summary").getInt("overall_rating");
                    status = mArray.getJSONObject(i).getJSONObject("ordering").getBoolean("is_open");
                    try {
                        JSONArray cui = mArray.getJSONObject(i).getJSONObject("summary")
                                .getJSONArray("cuisines");
                        int size = cui.length();
                        for (int j = 0; j < size; j++)
                            if (j < size - 1)
                                cuisine = cuisine + cui.getString(j) + ", ";
                            else
                                cuisine = cuisine + cui.getString(j);
                    } catch (JSONException e) {
                        cuisine = "No tags";
                    }
                    distance = mArray.getJSONObject(i).getJSONObject("location").getDouble("distance");
                    m.add(new Merchant(name, id, rating, status, cuisine, distance));
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
        return m;
    }
	
	/**
	 * The viewResult methods call the DisplayRestaurant activity 
	 * to display the list of merchants in the proper format 
	 * @param
	 */
	public void viewResult() {
		JSONObject searchResult = MerchantData.getResult();
		try {
			if (searchResult.getJSONArray("message").length() == 0) {
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
				AlertDialog.Builder alert = new AlertDialog.Builder(Home.getHomeContext());
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
			String search = "";
			// SearchMerchants search = new SearchMerchants("input here");
			if (gpsTag) {
				address = "";
				search = ServerInteract.search(address, 2);
				MerchantData.setResult(search);
			}
			else {
				search = ServerInteract.search(address, 0);
				MerchantData.setResult(search);
			}
			try {
				if (MerchantData.getResult().getJSONArray("message").length() == 0 && !search.equals("")) {
					MerchantData.setMerchantList(createMerList(MerchantData.getResult()));
					MerchantData.setAddress();
				}
			} catch (JSONException e) {e.printStackTrace();}
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
			// Showing custom progress view (background not disabled)
			// hideLoading();
			
			// custom dialog
			viewResult();
		}

	}
}
