package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;

public class MenuFragment extends Fragment {
	private ArrayList<String> menuList = new ArrayList<String>();
	MenuCustomAdapter adapter;
    ExpandableListView expanLV;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        
        // Populate list 	
	    try {
		    JSONArray itemList = MenuData.getResult().getJSONArray("menu");
		    for (int i = 0; i < itemList.length(); i++) {
		    	menuList.add(itemList.getString(i));
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	    expanLV = (ExpandableListView) rootView.findViewById(R.id.lvExp);
		adapter = new MenuCustomAdapter(getActivity(),
				R.layout.list_menu, R.layout.list_child_menu, menuList);
        expanLV.setAdapter(adapter);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        expanLV.setIndicatorBounds(size.x - 35, size.x - 40);    
        expanLV.setOnChildClickListener(new OnChildClickListener()
        {
        	@Override
        	public boolean onChildClick(ExpandableListView parent, View v,
        			int groupPosition, int childPosition, long id) {
        		Intent intent = new Intent(getActivity(), SingleMenuActivity.class);
        		String singleMenu = "", name;	
    			try {
					JSONObject itemGroup = new JSONObject(menuList.get(groupPosition));
					JSONArray itemArray = itemGroup.getJSONArray("children");
					if (itemArray.getJSONObject(childPosition).getString("type").equals("menu")) {
						singleMenu = itemArray.getJSONObject(childPosition).getString("children");
						name = itemArray.getJSONObject(childPosition).getString("name");
						if (itemArray.length() != 0 && itemArray != null) {
							intent.putExtra("menu", singleMenu);
							intent.putExtra("name", name);
							startActivity(intent);  
						}
					}
					else if (itemArray.getJSONObject(childPosition).getString("type").equals("item")) {
						singleMenu = itemGroup.getString("children");
						name = itemGroup.getString("name");
						intent.putExtra("menu", singleMenu);
						intent.putExtra("name", name);
						startActivity(intent);  
					}
					else {
						AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
						alert.setTitle("Error");
						alert.setMessage("This menu has no detailed information!");
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}      
						});
						alert.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     		
        		return false;
        	}
        });
        return rootView;
    }
  

//        Intent intent = new Intent(getActivity(), SingleMenuActivity.class);
//        intent.putExtra("menu", menuList.get(position));
//        startActivity(intent);             
}