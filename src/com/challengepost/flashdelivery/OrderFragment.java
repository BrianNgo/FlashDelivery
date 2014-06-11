package com.challengepost.flashdelivery;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.challengepost.flashdelivery.HomeActivity.Home;
import com.challengepost.flashdelivery.ItemOptionActivity.Item;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderFragment extends BaseFragment {
	private TextView cartText, subTotal;
	private ImageView v;
    Button btnBack, btnCheckOut, btnClear;
    private ActionBar actionBar;
    private HomeActivity listener;
    private static ArrayList<JSONObject> orderList = new ArrayList<JSONObject>();
    private static ArrayList<String> instrList = new ArrayList<String>();
    private static OrderAdapter adapter;
    private ListView list;
    private double totalPrice = 0;
	private Context c = Home.getHomeContext();
    
	public OrderFragment() {
		super("Orders", R.layout.fragment_order,R.layout.actionbar_top_orders, 1); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
        adapter = new OrderAdapter(c, R.layout.list_order, orderList);
	}
	
	public static OrderFragment fragment;
	
    public interface HomeActivity {
        public void onBackPressedHome();
    }
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
        try {
            listener = (HomeActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Home");
        }
    }
    
	@Override
	public void onResume() {
		super.onResume();		
        list = (ListView) this.fragmentView.findViewById(R.id.listView);
        list.setAdapter(adapter);
 		cartText = (TextView) this.fragmentView.findViewById(R.id.cart);
 		subTotal = (TextView) this.fragmentView.findViewById(R.id.subTotal);
	    v = (ImageView) this.fragmentView.findViewById(R.id.imageView1);
        
        actionBar = getActivity().getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        
        btnBack = (Button) this.actionBarView.findViewById(R.id.btnBack);
        btnCheckOut = (Button) this.actionBarView.findViewById(R.id.btnCheckOut);
        btnClear = (Button) this.fragmentView.findViewById(R.id.btnClear);
        
        btnBack.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
            	if (!getActivity().isTaskRoot())
            		getActivity().onBackPressed();
            	else
            		listener.onBackPressedHome();
            }
        });

        btnCheckOut.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // call fragment order
                Intent in = new Intent(getApplicationContext(),CheckOutActivity.class); 
                startActivity(in);
            }
        });
        if (Home.isOrder()) {
            instrList.add(Item.getInstr());
    		Home.setOrder(false);
        	new AddToCart().execute();
        }
        
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditOrder.setKey(0);
    			orderList.removeAll(orderList);
        		instrList.removeAll(instrList);
            	fragment.new RemoveOrder().execute();
            }
        });
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	public static class EditOrder {
		private static String guestToken = "";
		private static int key = 0;
		
		public static void setGuestToken(String t) {guestToken = t;}
		public static String getGuestToken() {return guestToken;}
		
		public static int getKey() {return key;}
		public static void setKey(int k) {key = k;}
		
		public static void removeOrder(int position, int k) {
			orderList.remove(position);
    		instrList.remove(position);
    		key = k;
			fragment.new RemoveOrder().execute();
		}
	}
	
	private class AddToCart extends AsyncTask<Void, Void, Void> {
		
		// custom dialog
		final Dialog dialog = new Dialog(c);
		private JSONObject message, cart;
		private JSONArray msg, cartArray;
		
		/**
		 * The onPreExecute method display the waiting message
		 * while the program is executing add to cart function
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
		 * The doInBackGround method interacts with the server to
		 * add to customer's cart their orders
		 */
		@Override
		protected Void doInBackground(Void... arg0) {	
		    v.setVisibility(View.GONE);
			cartText.setVisibility(View.GONE);
	        if (Item.getName() != null) {     
	        	totalPrice = 0;
	        	try {
	        		orderList.removeAll(orderList);
	        		if (EditOrder.getGuestToken().equals(""))
	        			EditOrder.setGuestToken(ServerInteract.getGuestToken(""));
	        		String temp = ServerInteract.addToCart(EditOrder.getGuestToken());
	        		message = new JSONObject(temp);
        			Log.i("Message" + message.toString(), "*******");
	        		msg = message.getJSONArray("message");
	        		
	        		cart = new JSONObject(ServerInteract.viewCart(EditOrder.getGuestToken(),""));
	        		if (cart.getJSONArray("message").length() == 0) {
	        			cartArray = cart.getJSONArray("cart");
	        			Log.i("Instr: " + instrList.size(), "(((())))");
	        			for (int i = 0; i < cartArray.length(); i++) {
	        				totalPrice = message.getDouble("subtotal");
	        				JSONObject item = new JSONObject();
	        				item.put("instruction", instrList.get(i));
	        				item.put("item", cartArray.getJSONObject(i));
	        				orderList.add(item);
	        			}
	        		}
	        	} catch (Exception e) {e.printStackTrace();}
	        }
			return null;
		}

		/**
		 * Display successed order in customer's cart
		 *
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Showing custom progress view (background not disabled)
			// hideLoading();
			// custom dialog
        	subTotal.setText("Sub-total: $" + totalPrice);
			try {
				if (msg.length() != 0) {
					AlertDialog.Builder alert = new AlertDialog.Builder(c);
					alert.setTitle("Error");
					alert.setMessage(msg.getJSONObject(0).getString("user_msg"));
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}      
					});
					alert.show();
				}
				else if (cart.getJSONArray("message").length() != 0) {
					AlertDialog.Builder alert = new AlertDialog.Builder(c);
					alert.setTitle("Error");
					alert.setMessage(cart.getJSONArray("message").getJSONObject(0).getString("user_msg"));
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}      
					});
					alert.show();
				}
			} catch (JSONException e) {e.printStackTrace();}
			
			if (orderList.size() != 0) {				
			    v.setVisibility(View.GONE);
				cartText.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
			else {
			    v.setVisibility(View.VISIBLE);
				cartText.setVisibility(View.VISIBLE);
				cartText.setText("No Items in Cart");
			}
			dialog.dismiss();
		}
	}
		
	private class RemoveOrder extends AsyncTask<Void, Void, Void> {		
		final Dialog dialog = new Dialog(c);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_progress_dialog);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			int key = EditOrder.getKey();
			try {
				JSONObject temp = new JSONObject(ServerInteract.removeOrder(key, EditOrder.getGuestToken()));
				if (key != 0)
					totalPrice = temp.getDouble("subtotal");
				else
					totalPrice = 0;
			} catch (JSONException e) {e.printStackTrace();}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
        	subTotal.setText("Sub-total: $" + totalPrice);
			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	}
}
