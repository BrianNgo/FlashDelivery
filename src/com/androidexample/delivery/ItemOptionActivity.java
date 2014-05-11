package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemOptionActivity extends FragmentActivity {

    Button btnBack, btnAddCart, btnPlus, btnMinus;
    private ViewPager viewPager;
    private TabsAdapter mAdapter;
    JSONArray itemOption = null;
    private int maxQty, minQty, quantity;
    private Context c = this;
    private String[] tabs = {"Item", "Options"};
    private ActionBar actionBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_option);

        // top bar
        actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_top_item_option_activity);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
        
	    // fix flipping tabs and action bar
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);

        final TextView textView1 = (TextView) findViewById(R.id.quantity);

        // buttons in this activity
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAddCart = (Button) findViewById(R.id.btnAddCart);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);

        // handle events for buttons
        btnBack.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddCart.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
            	// add special instruction and quantity
            	TextView text1 = (TextView) findViewById(R.id.quantity);
            	Item.setQuantity(Integer.parseInt(text1.getText().toString()));
                EditText instr = (EditText) findViewById(R.id.instr);
                Item.setInstr(instr.getText().toString());
                // call fragment order
                Intent callIntent = new Intent(Intent.ACTION_CALL); 
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setClass(c, HomeActivity.class);
                callIntent.putExtra("order", "1");
                startActivity(callIntent);
            }
        });

        btnPlus.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (quantity < maxQty)
                    quantity++;
                textView1.setText(quantity + "");
            }
        });

        btnMinus.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (quantity > minQty)
                    quantity--;
                textView1.setText(quantity + "");
            }
        });
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        mAdapter = new TabsAdapter(getSupportFragmentManager(), this, viewPager, 1);       
        for (String tab_name : tabs) {
        	mAdapter.addTab(tab_name);
        }
		actionBar.setSelectedNavigationItem(0);
        
        Intent in = getIntent();
        maxQty = Integer.parseInt(in.getStringExtra("maxQty"));
        minQty = Integer.parseInt(in.getStringExtra("minQty"));
        quantity = minQty;

        textView1.setText(minQty + "");
        
        Item.setContext(c); 
        Item.setPrice(Double.parseDouble(in.getStringExtra("price")));
        Item.setName(in.getStringExtra("name"));
        Item.setOption(in.getStringExtra("option"));
        Item.setDesc(in.getStringExtra("desc"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }    
    
    public static class Item {
        private static ArrayList<String> item = new ArrayList<String>();
        private static double basePrice = 0;
        private static int quantity;
        private static Context con;
        private static String option, itemName, desc, instr;

        public static void addOption(String s) {item.add(s);}
        public static void removeOption(int index) {item.remove(index);}
        public static void setOption(int index, String s) {item.set(index, s);}
        public static void resetOption() {item = new ArrayList<String>();}
        public static ArrayList<String> getItem() {return item;}

        public static void setPrice(double p) {basePrice = p;}
        public static double getPrice() {return basePrice;}
        
        public static void setQuantity(int p) {quantity = p;}
        public static int getQuantity() {return quantity;}
        
        public static void setContext(Context c) {con = c;}
        public static Context getContext() {return con;}

        public static void setOption(String s) {option = s;}
        public static String getOption() {return option;}
        
        public static void setName(String s) {itemName = s;}
        public static String getName() {return itemName;}
        
        public static void setDesc(String s) {desc = s;}
        public static String getDesc() {return desc;}
        
        public static void setInstr(String s) {instr = s;}
        public static String getInstr() {return instr;}
    }
}