package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SingleMenuActivity extends BaseActivity {
	
	final ArrayList<String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		Intent in = getIntent();
		String menu = in.getStringExtra("menu");

		
//		Need another listview for single menu
	    TextView textView = (TextView) findViewById(R.id.name);
	    textView.setTextSize(12);
	    textView.setText(menu);
	    
//	    try {
//	    	JSONObject singleMenu = new JSONObject(item);
//		    JSONArray itemList = MenuData.getResult().getJSONArray("menu");
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    if (list.isEmpty()) {
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle("Error");
//			alert.setMessage("This restaurant has no menu!");
//			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//				}      
//			});
//			alert.show();
//	    }
//		ListView mList = (ListView) findViewById(R.id.listView);		
//		mList.setItemsCanFocus(false);
//		MenuCustomAdapter adapter = new MenuCustomAdapter(
//					this, R.layout.list_menu, list);
//        mList.setAdapter(adapter);
	}
}
