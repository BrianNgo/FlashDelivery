package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.OrderFragment.EditList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layoutResourceId;
	ArrayList<JSONObject> data = null;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public OrderAdapter(Context cont, int layout,
			ArrayList<JSONObject> d) {
		super(cont, layout, d);
		context = cont;
		layoutResourceId = layout;
		data = d;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		View view = convertView;
		OrderHolder holder = null;
		try {			
			if (view == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				view = inflater.inflate(layoutResourceId, parent, false);
				holder = new OrderHolder();
				holder.itemName = (TextView) view.findViewById(R.id.name);
				holder.option = (TextView) view.findViewById(R.id.option);
				holder.price = (TextView) view.findViewById(R.id.price);
				holder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
				
				holder.itemName.setText(data.get(position).getString("name"));
				holder.price.setText("Total price: $" + data.get(position).getDouble("price"));

				String[] opt = data.get(position).getString("option").split("- ");
				String finalOption = "- ";
				for (int i = 0; i < opt.length; i++) {
					String temp;
					if (opt[i].indexOf("Price") != -1)
						temp = opt[i].substring(0, opt[i].indexOf("Price"));
					else
						temp = opt[i];
					if (i != opt.length-1)
						finalOption += temp + ", ";
					else
						finalOption += temp;
				}
				holder.option.setText(finalOption);
				
				holder.btnRemove.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditList.removeOrder(pos);
					}
				});
				view.setTag(holder);
			} else
				holder = (OrderHolder) view.getTag();
				
			holder.itemName.setText(data.get(position).getString("name"));
			holder.price.setText("Price: $" + data.get(position).getDouble("price"));

			String[] opt = data.get(position).getString("option").split("- ");
			String finalOption = "- ";
			for (int i = 0; i < opt.length; i++) {
				String temp;
				if (opt[i].indexOf("Price") != -1)
					temp = opt[i].substring(0, opt[i].indexOf("Price"));
				else if (opt[i].indexOf("\n") != -1)
					temp = opt[i].substring(0, opt[i].indexOf("\n"));
				else
					temp = opt[i];
				if (!temp.equals("")) {
					if (i != opt.length-1)
						finalOption += temp + ", ";
					else
						finalOption += temp;
				}
			}
			holder.option.setText(finalOption);
			
		} catch (JSONException e) {e.printStackTrace();}
		return view;
	}
	
	static class OrderHolder {
		TextView itemName;
		TextView option;
		TextView price;
		Button btnRemove;
	}

	@Override
	public int getCount() {
		return data.size();
	}
}
