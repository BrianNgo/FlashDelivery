package com.androidexample.delivery;

import com.androidexample.delivery.HomeActivity.Home;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends BaseFragment {

    public LoginFragment() {
        super("Account", R.layout.fragment_login,R.layout.actionbar_top_account, 2); // 0 is index of tab, next tabs will be 1 and 2
        fragment = this;
    }

    public static LoginFragment fragment;
    final static String EXTRA_MESSAGE = "";

    // button here
    private Button btnCC, btnChangePass, btnFav, btnAddress, btnLogout;

    @Override
    public void init(View view) {
        // Set the title to the string content
        TextView abTitle = (TextView) view.findViewById(R.id.loginAs);
        abTitle.setText("Signed in as: " + Home.getUserName());
        
        
        // Button in this activity 
        btnCC = (Button) view.findViewById(R.id.btnCC);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePass);
        btnFav = (Button) view.findViewById(R.id.btnFavorite);
        btnAddress = (Button) view.findViewById(R.id.btnAddress);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
                
        btnCC.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CC Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnFav.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Favorite Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnChangePass.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Change Password Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnAddress.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Address Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnLogout.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
            	Home.setLoginTag(false);
            	Home.updatePager();
            }
        });
    }



}