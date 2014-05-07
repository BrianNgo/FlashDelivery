package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemOptionAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layoutResourceId;
	ArrayList<JSONObject> data = null;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public ItemOptionAdapter(Context cont, int layout,
			ArrayList<JSONObject> d) {
		super(cont, layout, d);
		context = cont;
		layoutResourceId = layout;
		data = d;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		OptionHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(layoutResourceId, parent, false);
			holder = new OptionHolder();
			holder.optionName = (TextView) view.findViewById(R.id.name);
			holder.choice = (TextView) view.findViewById(R.id.choice);
			view.setTag(holder);
		} else {
			holder = (OptionHolder) view.getTag();
		}
		try {
			holder.optionName.setText(data.get(position).getString("name"));
		} catch (JSONException e) {e.printStackTrace();}
		return view;
	}
	
	static class OptionHolder {
		TextView optionName;
		TextView choice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
}
