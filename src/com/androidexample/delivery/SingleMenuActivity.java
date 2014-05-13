package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SingleMenuActivity extends BaseActivity {
	
	Button btnBack;
	private SingleMenuAdapter adapter = null;
	private ArrayList<JSONObject> menuList = new ArrayList<JSONObject>();
	JSONArray singleMenu = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		// top bar
		ActionBar actionBarTop = getActionBar();
	    actionBarTop.setCustomView(R.layout.actionbar_top_single_menu_activity);
	    actionBarTop.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    
	    // buttons in this activity
	    btnBack = (Button) findViewById(R.id.btnBack);
	    
	    // handle events for buttons
	    btnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});	   		
	    
		Intent in = getIntent();
		String menu = in.getStringExtra("menu");
		String menuName = in.getStringExtra("name");
		
	    TextView textView = (TextView) findViewById(R.id.menuName);
	    textView.setText(menuName);
		try
		{
			singleMenu = new JSONArray(menu);
		}
		catch (JSONException e) {e.printStackTrace();}
		
		for (int i = 0; i < singleMenu.length(); i++) {
			try {
				menuList.add(singleMenu.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	    adapter = new SingleMenuAdapter(this, R.layout.list_single_menu,
				 menuList);
		ListView mList = (ListView) findViewById(R.id.listView);
		mList.setAdapter(adapter);	
		
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String option = "", itemId = "", desc = "", price = "", itemName = "", 
						maxQty = "", minQty = "";
				try {
					option = menuList.get(position).getString("children");
					itemId = menuList.get(position).getString("id");
					desc = menuList.get(position).getString("description");
					price = menuList.get(position).getString("price");
					itemName = menuList.get(position).getString("name");
					maxQty = menuList.get(position).getString("max_qty");
					minQty = menuList.get(position).getString("min_qty");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(getApplicationContext(), ItemOptionActivity.class);
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
		
	}
}
