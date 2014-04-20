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

public class MenuCustomAdapter extends ArrayAdapter<String> {
	Context context;
	int layoutResourceId;
	ArrayList<String> data = new ArrayList<String>();
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public MenuCustomAdapter(Context cont, int layout,
			ArrayList<String> d) {
		super(cont, layout, d);
		context = cont;
		layoutResourceId = layout;
		data = d;
	}
	
	/**
	 * The getView function provides the layout for the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MenuHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new MenuHolder();
			holder.textName = (TextView) row.findViewById(R.id.name);
			holder.textDescription = (TextView) row.findViewById(R.id.description);
//			holder.textExtra1 = (TextView) row.findViewById(R.id.field3);
//			holder.textExtra2 = (TextView) row.findViewById(R.id.field4);

			row.setTag(holder);
		} else {
			holder = (MenuHolder) row.getTag();
		}
		try {
			JSONObject item = new JSONObject(data.get(position));
			if (!item.getString("name").equals("")) {
				holder.textName.setText(item.getString("name"));
				holder.textDescription.setText((item.getString("description")
						.equals(""))?"No description":item.getString("description"));
		//		holder.textExtra1.setText("text");
		//		holder.textExtra2.setText("text");
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return row;
	}
	
	static class MenuHolder {
		TextView textName;
		TextView textDescription;
//		TextView textExtra1;
//		TextView textExtra2;
	}
}