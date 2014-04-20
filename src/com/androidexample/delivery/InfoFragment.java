package com.androidexample.delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

		Log.i("the freak ", MenuData.getInfo());
		TextView lbl1 = (TextView) rootView.findViewById(R.id.a_label);
		TextView lbl2 = (TextView) rootView.findViewById(R.id.b_label);
		TextView lbl3 = (TextView) rootView.findViewById(R.id.c_label);
		TextView lbl4 = (TextView) rootView.findViewById(R.id.d_label);
		TextView lbl5 = (TextView) rootView.findViewById(R.id.e_label);
		TextView lbl6 = (TextView) rootView.findViewById(R.id.f_label);
		try {
			JSONObject mer = new JSONObject(MenuData.getInfo());  
			lbl1.setText(mer.getJSONObject("summary").getString("name"));
        	lbl2.setText("Address: " + mer.getJSONObject("location").getString("street")
			+ ", " + mer.getJSONObject("location").getString("city") + ", "
			+ mer.getJSONObject("location").getString("state")
			+ mer.getJSONObject("location").getString("zip"));
        	lbl3.setText("Phone: " + mer.getJSONObject("summary").getString("phone"));
        	String desc = mer.getJSONObject("summary").getString("description");
        	lbl4.setText("Overview: " + ((desc.equals(""))?"No description.":desc));
        	try {
        		JSONArray special = mer.getJSONObject("ordering").getJSONArray("specials");
        		String s = "";
        		for (int i = 0; i < special.length(); i++)
        			if (i == special.length() - 1)
        				s = s + special.getString(i);
        			else
        				s = s + special.getString(i) + ", ";
        		lbl5.setText("Special offer: " + s);
        	} catch (JSONException e) {
        		lbl5.setText("Specials: No special offers available");
        	}
	
        	lbl6.setText("Website: " + mer.getJSONObject("summary").getJSONObject("url")
        			.getString("complete"));
		} 
		catch (JSONException e) {
        	e.printStackTrace();
        }   
        return rootView;
    }
}
