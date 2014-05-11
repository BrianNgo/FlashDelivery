package com.androidexample.delivery;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.SearchFragment.MerchantData;

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
    public static String username = "buu1989@yahoo.com";
    public static String message = "Hello World";
    final static String password = "13121989";
    final static String EXTRA = "";

    // button here
    private Button logIn, signUp;

    @Override
    public void init(View view) {

        final EditText text1 = (EditText)view.findViewById(R.id.username_box);
        final EditText text2 = (EditText)view.findViewById(R.id.password_box);
        // buttons
        logIn = (Button)view.findViewById(R.id.btnLogin);
        logIn.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Login button pressed", Toast.LENGTH_SHORT).show();
                btnLoginPressed(text1, text2);
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

    // handle search button
    public void btnLoginPressed(EditText t1, EditText t2) {
    // Calling async task

        username = t1.getText().toString();
        Log.i("address = " + username, "**********************************");
        if (username.equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
            alert.setTitle("Error");
            alert.setMessage("Please enter an address!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alert.show();
        } else {
            new login().execute();
        }
    }

    private class login extends AsyncTask<Void, Void, Void> {

            // custom dialog
            final Dialog dialog = new Dialog(getHomeActivity());

            /**
             * The onPreExecute method display the waiting message
             * while the program is executing the search function
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing custom progress view (background not disabled)
                //showLoading();

                // custom dialog
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_progress_dialog);
                dialog.setCancelable(false);
                dialog.show();
            }

            /**
             * The doInBackGround method creates the searchRestaurant
             * object and gets the list of available merchants
             */
            @Override
            protected Void doInBackground(Void... arg0) {
                message = SearchMerchants.userLogin(username, password);
                return null;
            }

            /**
             * The onPostExecute views the list of merchants found
             * after the searching method had successfully executed
             */
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                // Showing custom progress view (background not disabled)
                // hideLoading();

                // custom dialog
                dialog.hide();
                viewResult();
            }

        }

    public void viewResult() {
        Intent i = new Intent(getApplicationContext(), DisplayLoginActivity.class);
        i.putExtra(EXTRA, message);
        startActivity(i);
    }


}