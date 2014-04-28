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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuCustomAdapter extends BaseExpandableListAdapter {
	Context context;
	int parentLayout, childLayout;
	ArrayList<String> data = new ArrayList<String>();
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public MenuCustomAdapter(Context cont, int parentLayout, int childLayout,
			ArrayList<String> d) {
		context = cont;
		this.parentLayout = parentLayout;
		this.childLayout = childLayout;
		data = d;
	}

	static class MenuHolder {
		TextView parentName;
		TextView parentDesc;
//		TextView textExtra1;
//		TextView textExtra2;
	}
	
	static class ChildHolder {
		TextView childName;
		TextView childDesc;
	}
	
	@Override
	public JSONObject getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		JSONObject child = null;
		try {
			JSONObject item = new JSONObject(data.get(groupPosition));
			JSONArray itemArray = item.getJSONArray("children");
			child = itemArray.getJSONObject(childPosition);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View child = convertView;
		ChildHolder holder = null;
		
		if (child == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			child = inflater.inflate(childLayout, parent, false);
			holder = new ChildHolder();
			holder.childName = (TextView) child.findViewById(R.id.name);
			holder.childDesc = (TextView) child.findViewById(R.id.description);
//			holder.textExtra1 = (TextView) row.findViewById(R.id.field3);
//			holder.textExtra2 = (TextView) row.findViewById(R.id.field4);
			child.setTag(holder);
		} else {
			holder = (ChildHolder) child.getTag();
		}
		try {
			JSONObject item = new JSONObject(data.get(groupPosition));
			JSONArray itemArray = item.getJSONArray("children");
			if (!itemArray.getJSONObject(childPosition).getString("name").equals("")) {
				holder.childName.setText(itemArray.getJSONObject(childPosition).getString("name"));
				String desc = itemArray.getJSONObject(childPosition).getString("description");
				holder.childDesc.setText(desc.equals("")?
						"No description":desc);
		//		holder.textExtra1.setText("text");
		//		holder.textExtra2.setText("text");
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return child;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = new JSONObject(data.get(groupPosition));
			JSONArray itemArray = item.getJSONArray("children");
			return itemArray.length();
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View group = convertView;
		MenuHolder holder = null;
		
		if (group == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			group = inflater.inflate(parentLayout, parent, false);
			holder = new MenuHolder();
			holder.parentName = (TextView) group.findViewById(R.id.name);
			holder.parentDesc = (TextView) group.findViewById(R.id.description);
//			holder.textExtra1 = (TextView) row.findViewById(R.id.field3);
//			holder.textExtra2 = (TextView) row.findViewById(R.id.field4);
			group.setTag(holder);
		} else {
			holder = (MenuHolder) group.getTag();
		}
		try {
			JSONObject item = new JSONObject(data.get(groupPosition));
			if (!item.getString("name").equals("")) {
				holder.parentName.setText(item.getString("name"));
				holder.parentDesc.setText((item.getString("description")
						.equals(""))?"No description":item.getString("description"));
		//		holder.textExtra1.setText("text");
		//		holder.textExtra2.setText("text");
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		if (isExpanded) {
			ImageView icon=(ImageView)group.findViewById(R.id.indicator);
			icon.setImageResource(R.drawable.arrow_up);
		}
		else {
			ImageView icon=(ImageView)group.findViewById(R.id.indicator);
			icon.setImageResource(R.drawable.arrow_down);
		}
		return group;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}