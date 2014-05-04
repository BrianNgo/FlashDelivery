package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class SingleMenuAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layoutResourceId;
	ArrayList<JSONObject> data = null;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public SingleMenuAdapter(Context cont, int layout,
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
			holder.itemPrice = (TextView) row.findViewById(R.id.price);
			row.setTag(holder);
		} else {
			holder = (MenuHolder) row.getTag();
		}
		try {
			holder.itemName.setText(data.get(position).getString("name"));
			holder.itemPrice.setText("price: $" + data.get(position).getDouble("price"));
		} catch (JSONException e) {e.printStackTrace();}
		return row;
	}
	
	static class MenuHolder {
		TextView itemName;
		TextView itemPrice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
}
