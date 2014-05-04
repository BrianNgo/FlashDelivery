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
	JSONArray singleMenu;
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
		String name = in.getStringExtra("name");
		
	    TextView textView = (TextView) findViewById(R.id.menuName);
	    textView.setText(name);
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
				// need spinner to select options from option group
				try {
					String child = menuList.get(position).getString("children");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
