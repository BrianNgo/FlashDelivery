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
import android.widget.TextView;
import android.widget.Toast;

public class OrdersFragment extends BaseFragment {
	
	public OrdersFragment() {
		super("Orders", R.layout.orders_fragment,R.layout.actionbar_top_orders, 1); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
	}
	
	public static OrdersFragment fragment;
	
	// button here
	private TextView txt;	

	@Override
	public void init(View view) {	
		
		// buttons
		txt = (TextView)view.findViewById(R.id.textView1);
		
		txt.setText("View Customer Orders");
		
		
	}


	





}
