package com.challengepost.flashdelivery;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DishAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layoutResourceId;
	ArrayList<JSONObject> data = null;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public DishAdapter(Context cont, int layout,
			ArrayList<JSONObject> d) {
		super(cont, layout, d);
		context = cont;
		layoutResourceId = layout;
		data = d;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MenuHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new MenuHolder();
			holder.itemName = (TextView) row.findViewById(R.id.name);
			holder.basePrice = (TextView) row.findViewById(R.id.price);
			holder.maxPrice = (TextView) row.findViewById(R.id.maxPrice);
			row.setTag(holder);
		} else
			holder = (MenuHolder) row.getTag();
			
		try {
			holder.itemName.setText(Html.fromHtml(data.get(position).getString("name")).toString());
			holder.basePrice.setText("Base Price: $" + data.get(position).getDouble("price"));
			holder.maxPrice.setText("Max price: $" + data.get(position).getDouble("max_price"));
		} catch (JSONException e) {e.printStackTrace();}
		return row;
	}
	
	static class MenuHolder {
		TextView itemName;
		TextView basePrice;
		TextView maxPrice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
}
