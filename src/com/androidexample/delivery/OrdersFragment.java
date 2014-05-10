package com.androidexample.delivery;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.ItemOptionActivity.Item;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrdersFragment extends BaseFragment {
	private TextView txt;
	
	public OrdersFragment() {
		super("Orders", R.layout.orders_fragment,R.layout.actionbar_top_orders, 1); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
	}
	
	public static OrdersFragment fragment;
	
	@Override
	public void onResume() {
		super.onResume();
		txt = (TextView) this.fragmentView.findViewById(R.id.order);
		if (Item.getName() != null) {				
		    ImageView v = (ImageView) this.fragmentView.findViewById(R.id.imageView1);
		    v.setVisibility(View.GONE);
			ArrayList<String> item = Item.getItem();
			String name = Item.getName();
			double totalPrice = Item.getPrice();
			String order = "";
			for (int i = 0; i < item.size(); i++)
				if (!item.get(i).equals(""))
					order += item.get(i) + "\n";
			String[] result = order.split("\\$");
			for (int i = 1; i < result.length; i++) {
				if (result[i].indexOf(" ") != -1)
					totalPrice += Double.parseDouble(result[i]
							.substring(0, result[i].indexOf(' ') + 1));		
				else
					totalPrice += Double.parseDouble(result[i]);		
			}
			name += "\n" + order;
			name += "\n Total Price: $" + totalPrice;
			txt.setText(name);
		}
		else
			txt.setText("No Orders");
	}


}
