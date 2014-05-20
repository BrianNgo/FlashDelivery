package com.androidexample.delivery;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class DisplayLoginActivity extends BaseActivity {

	private ActionBar actionBar;
	private Button btnCC, btnChangePassword, btnFavorite, btnAddress, btnLogout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_login);
        
        // Action bar in this activity
        actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_top_login);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        
        // Get the string from itent
        Intent i = getIntent();
        String message = i.getStringExtra(AccountFragment.EXTRA_MESSAGE);
        
        // Set the title to the string content
        TextView abTitle = (TextView) findViewById(R.id.login_top_txt);
        abTitle.setText(message);
        
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
//        setContentView(textView);
        
        // Button in this activity 
        btnCC = (Button) findViewById(R.id.btnCC);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        btnAddress = (Button) findViewById(R.id.btnAddress);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        
        btnCC.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CC Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnFavorite.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Favorite Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnChangePassword.setOnClickListener(new OnClickListener() {
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
                Toast.makeText(getApplicationContext(), "Logout Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_login,
                    container, false);
            return rootView;
        }
    }

}