package com.androidexample.delivery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class MerchantCustomAdapter extends ArrayAdapter<Merchant> {
	Context context;
	int layoutResourceId;
	ArrayList<Merchant> data = new ArrayList<Merchant>();
	
	/**
	 * Constructor of the class with 3 arguments
	 */
	public MerchantCustomAdapter(Context cont, int layout,
			ArrayList<Merchant> d) {
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
		MerchantHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new MerchantHolder();
			holder.textName = (TextView) row.findViewById(R.id.name);
			holder.textName.setSelected(true);
			holder.textName.requestFocus();
			holder.textCuisine = (TextView) row.findViewById(R.id.cuisine);
			holder.textStatus = (TextView) row.findViewById(R.id.status);
			holder.textDistance = (TextView) row.findViewById(R.id.distance);
			holder.rating = (RatingBar) row.findViewById(R.id.rtbMerchantRating);
			
//			holder.btnRate = (Button) row.findViewById(R.id.button2);
			row.setTag(holder);
		} else {
			holder = (MerchantHolder) row.getTag();
		}
		Merchant m = data.get(position);
		holder.textName.setText(m.getName());
		holder.textName.setSelected(true);
		holder.textName.requestFocus();
		holder.textCuisine.setText(m.getCuisine());
		holder.textStatus.setText("Now " + (m.getStatus()?"Open":"Closed"));
		holder.textDistance.setText(m.getDistance());	
		holder.rating.setMax(5);
		holder.rating.setNumStars(5);
		holder.rating.setStepSize((float)0.1);
		holder.rating.setRating((float)(m.getRating()) / 100 * 5);
//		holder.btnRate.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.i("Rate Button Clicked", "**********");
//				Toast.makeText(context, "Rate button Clicked",
//						Toast.LENGTH_LONG).show();
//			}
//		});
		return row;
	}
	
	static class MerchantHolder {
		TextView textName;
		TextView textCuisine;
		TextView textStatus;
		TextView textDistance;
		RatingBar rating;
//		Button btnRate;
	}
}