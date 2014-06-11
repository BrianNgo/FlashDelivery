package com.challengepost.flashdelivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.challengepost.flashdelivery.DisplayMerchantsActivity.MenuData;

public class DishFragment extends Fragment {
	private ArrayList<JSONObject> dishList = new ArrayList<JSONObject>();
	private View rootView;
	DishAdapter adapter;
    ListView list;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        
	    try {
	    	JSONArray dList = MenuData.getDishes();
	    	dishList = new ArrayList<JSONObject>();
		    for (int i = 0; i < dList.length(); i++)
		    	dishList.add(dList.getJSONObject(i));
	    } catch (JSONException e) {e.printStackTrace();}
		
	    adapter = new DishAdapter(getActivity(), R.layout.list_dishes, dishList);
		list = (ListView) rootView.findViewById(R.id.listView);
		list.setAdapter(adapter);	
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {		    
				String option = "", itemId = "", desc = "", price = "", itemName = "", 
						maxQty = "", minQty = "";
				try {
					option = dishList.get(position).getString("children");
					itemId = dishList.get(position).getString("id");
					desc = dishList.get(position).getString("description");
					price = dishList.get(position).getString("price");
					itemName = dishList.get(position).getString("name");
					maxQty = dishList.get(position).getString("max_qty");
					minQty = dishList.get(position).getString("min_qty");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(getActivity().getApplicationContext(), ItemOptionActivity.class);
				intent.putExtra("name", itemName);
				intent.putExtra("option", option);
				intent.putExtra("id", itemId);
				intent.putExtra("desc", desc);
				intent.putExtra("price", price);
				intent.putExtra("maxQty", maxQty);
				intent.putExtra("minQty", minQty);
				startActivity(intent);
			}
		});
        return rootView;
    }          
    public void updateDish() {
	    try {
	    	JSONArray dList = MenuData.getDishes();
	    	dishList = new ArrayList<JSONObject>();
		    for (int i = 0; i < dList.length(); i++)
		    	dishList.add(dList.getJSONObject(i));
	    } catch (JSONException e) {e.printStackTrace();}  	
	    adapter = new DishAdapter(getActivity(), R.layout.list_dishes, dishList);
		list = (ListView) rootView.findViewById(R.id.listView);
		list.setAdapter(adapter);	
    }
}