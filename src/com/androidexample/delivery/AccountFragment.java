package com.androidexample.delivery;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AccountFragment extends BaseFragment {
	
	public AccountFragment() {
		super("Orders", R.layout.account_fragment,R.layout.actionbar_top_account, 2); // 0 is index of tab, next tabs will be 1 and 2
		fragment = this;
	}
	
	public static AccountFragment fragment;
	
	// button here
	private Button logIn, signUp;	

	@Override
	public void init(View view) {	
		
		// buttons
		logIn = (Button)view.findViewById(R.id.btnLogin);		
		logIn.setOnClickListener(new OnClickListener() {
			// Calling the event
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Login button pressed", Toast.LENGTH_SHORT).show();
			}
		});
		

		signUp = (Button)view.findViewById(R.id.btnSignup);
		signUp.setOnClickListener(new OnClickListener() {
			// Calling the event
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Signup button pressed", Toast.LENGTH_SHORT).show();
			}
		});
		
	}


	





}
