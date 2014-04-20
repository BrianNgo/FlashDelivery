package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;;

public class MenuFragment extends ListFragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;
	final ArrayList<String> list = new ArrayList<String>();
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list 	
	    try {
		    JSONArray itemList = MenuData.getResult().getJSONArray("menu");
		    for (int i = 0; i < itemList.length(); i++) {
		    	list.add(itemList.getString(i));
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (list.isEmpty()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Error");
			alert.setMessage("This restaurant has no menu!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}      
			});
			alert.show();
	    }

		MenuCustomAdapter adapter = new MenuCustomAdapter(
					getActivity(), R.layout.list_menu, list);
        setListAdapter(adapter);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailMenuFrame = getActivity().findViewById(R.id.detailMenu);
        mDualPane = detailMenuFrame != null && detailMenuFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
        	// In dual-pane mode, the list view highlights the selected item.
        	getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        	// Make sure our UI is in the correct state.
        	showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailMenuFragment details = (DetailMenuFragment)
            		getFragmentManager().findFragmentById(R.id.detailMenu);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailMenuFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.detailMenu, details);
                } else {
                    ft.replace(R.id.detailMenu, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), SingleMenuActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}