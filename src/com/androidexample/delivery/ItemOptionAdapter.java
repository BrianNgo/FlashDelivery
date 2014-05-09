package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
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
		try {
			JSONArray temp = data.get(position).getJSONArray("children");
			
			if (view == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				view = inflater.inflate(layoutResourceId, parent, false);
				holder = new OptionHolder();
				holder.optionName = (TextView) view.findViewById(R.id.name);
				TextView choice = (TextView) view.findViewById(R.id.choice);
				if (data.get(position).getInt("min_selection") == 1 &&
						data.get(position).getInt("max_selection") <= 1) {
					choice.setText("Default: "
							+ temp.getJSONObject(0).getString("name")
							+ "\t Price: $" + temp.getJSONObject(0).getInt("price"));
				}
				view.setTag(holder);
			} else
				holder = (OptionHolder) view.getTag();
								
			holder.optionName.setText(data.get(position).getString("name"));
			if (temp.length() == 0) return null;
		} catch (JSONException e) {e.printStackTrace();}
		return view;
	}
	
	static class OptionHolder {
		TextView optionName;
	}

	@Override
	public int getCount() {
		return data.size();
	}
}
