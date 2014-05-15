package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;

public class MenuFragment extends Fragment {
	private ArrayList<JSONObject> menuList = new ArrayList<JSONObject>();
	MenuAdapter adapter;
    ListView list;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        
        // Populate list 	
	    try {
		    JSONArray itemList = MenuData.getMenu().getJSONArray("menu");
		    for (int i = 0; i < itemList.length(); i++) {
		    	menuList.add(new JSONObject(itemList.getString(i)));
		    }
		} catch (JSONException e) {e.printStackTrace();}
	    
	    if (menuList.isEmpty()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Error");
			alert.setMessage("This restaurant has no menu!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}      
			});
			alert.show();
	    }
	    list = (ListView) rootView.findViewById(R.id.listView);
		adapter = new MenuAdapter(getActivity(), R.layout.list_menu, menuList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
				MenuData.setSubMenu(menuList.get(position));
				Intent intent = new Intent(getActivity(), SubMenuActivity.class);
				startActivity(intent);
			}
        });
        return rootView;
    }          
}