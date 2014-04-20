package com.androidexample.delivery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SingleMenuActivity extends BaseActivity {
	
	final ArrayList<String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		Intent in = getIntent();
		String item = in.getStringExtra("item");
		 // Create the text view
	    TextView textView = (TextView) findViewById(R.id.textView1);
	    textView.setTextSize(12);
	    textView.setText(item);
	}
}
