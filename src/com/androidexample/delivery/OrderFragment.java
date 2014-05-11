package com.androidexample.delivery;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.HomeActivity.HomeContext;
import com.androidexample.delivery.ItemOptionActivity.Item;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderFragment extends BaseFragment {
	private TextView txt;
    Button btnBack, btnCheckOut;
    private ActionBar actionBar;
    private Home listener;
    private static ArrayList<JSONObject> orderList = new ArrayList<JSONObject>();;
    private static OrderAdapter adapter;
    private ListView list;
	private Context c = HomeContext.getHomeContext();
    
	public OrderFragment() {
		super("Orders", R.layout.orders_fragment,R.layout.actionbar_top_orders, 1); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
        adapter = new OrderAdapter(c, R.layout.list_order, orderList);
	}
	
	public static OrderFragment fragment;
	
    public interface Home {
        public void onBackPressedHome();
    }
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
        try {
            listener = (Home) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Home");
        }
    }
    
	@Override
	public void onResume() {
		super.onResume();		
        list = (ListView) this.fragmentView.findViewById(R.id.listView);
        list.setAdapter(adapter);
        
        actionBar = getActivity().getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        
        btnBack = (Button) this.actionBarView.findViewById(R.id.btnBack);
        btnCheckOut = (Button) this.actionBarView.findViewById(R.id.btnCheckOut);
        
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
        if (Item.getName() != null) {
        	ArrayList<String> item = Item.getItem();
        	String name = Item.getName(); 
        	double totalPrice = Item.getPrice();
        	
        	String option = "";
        	for (int i = 0; i < item.size(); i++)
        		if (!item.get(i).equals(""))
        			option += item.get(i);
        	String[] result = option.split("\\$");
        	for (int i = 1; i < result.length; i++) {
        		if (result[i].indexOf(" ") != -1)
        			totalPrice += Double.parseDouble(result[i]
        					.substring(0, result[i].indexOf("-")));		
        		else
        			totalPrice += Double.parseDouble(result[i]);		
        	}
        	
        	totalPrice *= Item.getQuantity();
        	name = "{\"name\":\"" + name + "\",\"price\":" + totalPrice
        			+ ",\"option\":\"" + option + "\"}";
        	try {
        		JSONObject temp = new JSONObject(name);
        		orderList.add(temp);
        		Log.i("Success adding order", "*****");
        	} catch (JSONException e) {e.printStackTrace();}
        }
        
		txt = (TextView) this.fragmentView.findViewById(R.id.cart);
	    ImageView v = (ImageView) this.fragmentView.findViewById(R.id.imageView1);
		if (orderList.size() != 0) {				
		    v.setVisibility(View.GONE);
			txt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
		else {
		    v.setVisibility(View.VISIBLE);
			txt.setVisibility(View.VISIBLE);
			txt.setText("No Orders");
		}
	}
	
	public static class EditList {
		public static void removeOrder(int position) {
			orderList.remove(position);
			adapter.notifyDataSetChanged();
		}
	}
}
