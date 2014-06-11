package com.challengepost.flashdelivery;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class SignupActivity extends BaseActivity {

	private Button btnSignup;
	private String fName = "";
	private String lName = "";
	private String emailAddress= "";
	private String passwordForEmail = "";
	private String message;
	final static String EXTRA_MESSAGE = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		final EditText firstName = (EditText) findViewById(R.id.firstname_box);
		final EditText lastName = (EditText) findViewById(R.id.lastname_box);
		final EditText email = (EditText) findViewById(R.id.email_box);
		final EditText password = (EditText) findViewById(R.id.passwd_box);
		
		btnSignup = (Button) findViewById(R.id.btnSignup);
		btnSignup.setOnClickListener(new OnClickListener() {
            // Calling the event
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Create Account", Toast.LENGTH_SHORT).show();
            	fName = firstName.getText().toString();
            	lName = lastName.getText().toString();
            	emailAddress = email.getText().toString();
            	passwordForEmail = password.getText().toString();
            	if (lName.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("Please enter a valid last name!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
            	} else if (fName.equals("")) {
            		AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("Please enter a valid first name!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
            	} else if (emailAddress.equals("")) {
            		AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("Please enter a valid email address!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
            	} else if (passwordForEmail.equals("")) {
            		AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("Please enter a valid password!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
            	}
            	else 
            		new createAccount().execute();
            }
        });
	}
	
private class createAccount extends AsyncTask<Void, Void, Void> {

        // custom dialog
        final Dialog dialog = new Dialog(SignupActivity.this);

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
            message = ServerInteract.accountCreate(fName, lName, emailAddress, passwordForEmail);
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

            dialog.hide();
            if (message.equals("Not Connected\n")) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                alert.setTitle("Error");
                alert.setMessage("Existing account with this email");
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_signup,
					container, false);
			return rootView;
		}
	}

}
