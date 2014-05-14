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

public class OptionAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layoutResourceId;
	ArrayList<JSONObject> data = null;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public OptionAdapter(Context cont, int layout, ArrayList<JSONObject> d) {
		super(cont, layout, d);
		context = cont;
		layoutResourceId = layout;
		data = d;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		OptionHolder holder = null;
		int min, max;
		
		try {
			JSONArray temp = data.get(position).getJSONArray("children");
			if (temp.length() == 0) return null;
			
			if (view == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				view = inflater.inflate(layoutResourceId, parent, false);
				holder = new OptionHolder();
				holder.optionName = (TextView) view.findViewById(R.id.name);
				holder.choice = (TextView) view.findViewById(R.id.choice);
				min = data.get(position).getInt("min_selection");
				max = data.get(position).getInt("max_selection");
				if (min == 1 && max >= min) {
					int p = temp.getJSONObject(0).getInt("price");
					holder.choice.setText("Default: "
								+ temp.getJSONObject(0).getString("name")
								+ ((p == 0)?"":"\t Price: $" + p));	
				}
				else if (min > 1 && max >= min) {
					String str = "Default: ";
					for (int i = 0; i < min; i++) {
						int p = temp.getJSONObject(i).getInt("price");
						str += "\n " + temp.getJSONObject(i).getString("name")
								+ ((p == 0)?"":"\t - Price: $" + p);	
					}
					holder.choice.setText(str);	
				}
				view.setTag(holder);
			} else
				holder = (OptionHolder) view.getTag();
			if (!holder.optionName.getText().toString().equals(data.get(position).getString("name"))) {
				min = data.get(position).getInt("min_selection");
				max = data.get(position).getInt("min_selection");
				if (min == 1) {
					int p = temp.getJSONObject(0).getInt("price");
					holder.choice.setText("Default: "
							+ temp.getJSONObject(0).getString("name")
							+ ((p == 0)?"":"\t Price: $" + p));		
				}
				else if (min > 1 && max >= min) {
					String str = "Default: ";
					for (int i = 0; i < min; i++) {
						int p = temp.getJSONObject(i).getInt("price");
						str += "\n " + temp.getJSONObject(i).getString("name")
								+ ((p == 0)?"":"\t - Price: $" + p);	
					}
					holder.choice.setText(str);	
				}
				else
					holder.choice.setText("Not select an option yet!");
			}
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
		return data.size();
	}
}
