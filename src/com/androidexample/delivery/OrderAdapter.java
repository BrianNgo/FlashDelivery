package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.OrderFragment.EditOrder;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
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
		String finalOption = "";
		JSONArray topOpt = null;
		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(layoutResourceId, parent, false);
			holder = new OrderHolder();
			holder.itemName = (TextView) view.findViewById(R.id.name);
			holder.quantity = (TextView) view.findViewById(R.id.quantity);
			holder.option = (TextView) view.findViewById(R.id.option);
			holder.instr = (TextView) view.findViewById(R.id.instr);
			holder.price = (TextView) view.findViewById(R.id.price);
			holder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
			view.setTag(holder);
		} else
			holder = (OrderHolder) view.getTag();
		
		try {
			holder.itemName.setText(Html.fromHtml(data.get(position).getJSONObject("item").getString("name")));
			holder.quantity.setText("Quantity: " + data.get(position).getJSONObject("item").getInt("quantity"));
			holder.price.setText("Price: $" + data.get(position).getJSONObject("item").getDouble("price"));
			holder.instr.setText(data.get(position).getString("instruction"));

			topOpt = data.get(position).getJSONObject("item").getJSONArray("options");
		
			for (int i = 0; i < topOpt.length(); i++) {
				try {
					JSONArray botOpt = topOpt.getJSONObject(i).getJSONArray("options");
					for (int j = 0; j < botOpt.length(); j++) {
						double p = 0;
						try {
							p = botOpt.getJSONObject(j).getDouble("price");
						} catch (JSONException e) {}
						finalOption += botOpt.getJSONObject(j).getString("name") 
								+ ((p==0)?(". "):(" $" + p));
					}
				} catch (JSONException e) {}
			}
		} catch (JSONException e) {}

		holder.option.setText(finalOption);
		holder.btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (data.size() != 0) {
					int key = pos;
					try {
						key = data.get(pos).getJSONObject("item").getInt("item_key");
					} catch (JSONException e) {e.printStackTrace();}
					EditOrder.removeOrder(pos, key);
				}
			}
		});
		return view;
	}
	
	static class OrderHolder {
		TextView itemName;
		TextView quantity;
		TextView option;
		TextView instr;
		TextView price;
		Button btnRemove;
	}

	@Override
	public int getCount() {
		return data.size();
	}
}
