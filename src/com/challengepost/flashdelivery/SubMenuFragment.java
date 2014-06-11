package com.challengepost.flashdelivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.challengepost.flashdelivery.DisplayMerchantsActivity.MenuData;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SubMenuFragment extends Fragment {
	private ArrayList<JSONObject> subMenuList = new ArrayList<JSONObject>();
	MenuAdapter adapter;
    ListView list;
    private boolean hasSub;
    private JSONObject menu;
    private SubMenuActivity listener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
		
//      Populate list 	
	    try {
	    	menu = MenuData.getSubMenu();
		    hasSub = menu.getJSONArray("children").getJSONObject(0)
					.getString("type").equals("menu");
		    if (hasSub) {
		    	JSONArray subMenu = menu.getJSONArray("children");
		    	for (int i = 0; i < subMenu.length(); i++) {
		    		subMenuList.add(subMenu.getJSONObject(i));
		    		MenuData.setDishes(subMenuList.get(i).getJSONArray("children"));
		    	}
		    }
		    else {
		    	subMenuList.add(menu);
				MenuData.setDishes(subMenuList.get(0).getJSONArray("children"));
		    }
	    } catch (JSONException e) {e.printStackTrace();}
		
	    adapter = new MenuAdapter(getActivity(), R.layout.list_menu, subMenuList);
		list = (ListView) rootView.findViewById(R.id.listView);
		list.setAdapter(adapter);	
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {		    
				try {
					MenuData.setDishes(subMenuList.get(position).getJSONArray("children"));
				} catch (JSONException e) {e.printStackTrace();}
        		listener.changeTab(1);
				// change viewPager to tab dishes
			}
		});
		return rootView;	
	}
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
        try {
            listener = (SubMenuActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SubMenu");
        }
    }
    
    public interface SubMenuActivity {
        public void changeTab(int t);
    }
}
