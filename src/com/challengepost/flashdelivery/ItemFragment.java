package com.challengepost.flashdelivery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.challengepost.flashdelivery.ItemOptionActivity.Item;

public class ItemFragment extends Fragment {
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        if (container == null) {
        	return null;
        }

        TextView textView1 = (TextView) rootView.findViewById(R.id.desc);
        TextView textView2 = (TextView) rootView.findViewById(R.id.price);
        TextView textView3 = (TextView) rootView.findViewById(R.id.name);

        String desc = Item.getDesc();
        textView1.setText(desc.equals("")?"No Description":"Description: " + desc);
        textView2.setText("Base price: $" + Item.getPrice());
        textView3.setText(Item.getName());

        return rootView;
    }
}
