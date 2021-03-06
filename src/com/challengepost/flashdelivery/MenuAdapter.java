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

public class MenuAdapter extends ArrayAdapter<JSONObject> {
	Context context;
	int layout;
	ArrayList<JSONObject> data;
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public MenuAdapter(Context cont, int layout, ArrayList<JSONObject> d) {
		super(cont, layout, d);
		context = cont;
		this.layout = layout;
		data = d;
	}

	static class MenuHolder {
		TextView menuName;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View group = convertView;
		MenuHolder holder = null;
		
		if (group == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			group = inflater.inflate(layout, parent, false);
			holder = new MenuHolder();
			holder.menuName = (TextView) group.findViewById(R.id.name);
			group.setTag(holder);
		} else {
			holder = (MenuHolder) group.getTag();
		}
		try {
			if (!data.get(position).getString("name").equals(""))
				holder.menuName.setText(Html.fromHtml(data.get(position).getString("name")).toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return group;
	}
}