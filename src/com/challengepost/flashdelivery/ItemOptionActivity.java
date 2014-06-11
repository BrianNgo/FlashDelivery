
package com.challengepost.flashdelivery;

import java.util.ArrayList;

import org.json.JSONArray;

import com.challengepost.flashdelivery.HomeActivity.Home;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
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
                Home.setOrder(true);
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
        mAdapter = new TabsAdapter(getSupportFragmentManager(), this, viewPager, 2);       
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
        Item.setOpt(in.getStringExtra("option"));
        Item.setItemId(in.getStringExtra("id"));
        Item.setDesc(in.getStringExtra("desc"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }    
    
    public static class Item {
        private static ArrayList<String> option = new ArrayList<String>();
        private static double basePrice = 0;
        private static int quantity;
        private static Context con;
        private static String id, opt, itemName, desc, instr;

        public static void addOption(String s) {option.add(s);}
        public static void removeOption(int index) {option.remove(index);}
        public static void setOption(int index, String s) {option.set(index, s);}
        public static void resetOption() {option = new ArrayList<String>();}
        public static ArrayList<String> getOption() {return option;}

        public static void setPrice(double p) {basePrice = p;}
        public static double getPrice() {return basePrice;}
        
        public static void setQuantity(int p) {quantity = p;}
        public static int getQuantity() {return quantity;}
        
        public static void setContext(Context c) {con = c;}
        public static Context getContext() {return con;}
        
        public static void setItemId(String s) {id = s;}
        public static String getItemId() {return id;}

        public static void setOpt(String s) {opt = s;}
        public static String getOpt() {return opt;}
        
        public static void setName(String s) {itemName = Html.fromHtml(s).toString();}
        public static String getName() {return itemName;}
        
        public static void setDesc(String s) {desc = s;}
        public static String getDesc() {return desc;}
        
        public static void setInstr(String s) {instr = s;}
        public static String getInstr() {return instr;}
    }
}