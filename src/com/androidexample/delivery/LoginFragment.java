package com.androidexample.delivery;

import com.androidexample.delivery.HomeActivity.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends BaseFragment {

    public LoginFragment() {
        super("Account", R.layout.fragment_login,R.layout.actionbar_top_account, 2); // 0 is index of tab, next tabs will be 1 and 2
        fragment = this;
    }

    public static LoginFragment fragment;
    private static String username = "";
    private static String message = "";
    private static String password = "";
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
    
    
    
    // handle search button
    public void btnLoginPressed(EditText t1, EditText t2) {
    // Calling async task

        username = t1.getText().toString();
        password = t2.getText().toString();
        Log.i("username = " + username, "**********************************");
        if (username.equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
            alert.setTitle("Error");
            alert.setMessage("Please enter a valid username!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alert.show();
        } else if (password.equals("")){
        	 AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
             alert.setTitle("Error");
             alert.setMessage("Please enter a valid password!");
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
                message = ServerInteract.userLogin(username, password);
                //message = ServerInteract.getAccessToken();
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
                if (message.equals("Not Connected\n")) {
                	AlertDialog.Builder alert = new AlertDialog.Builder(getHomeActivity());
                    alert.setTitle("Error");
                    alert.setMessage("Invalid username or password");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                } else
                	viewResult();
            }

        }

    public void viewResult() {
    	
    }


}