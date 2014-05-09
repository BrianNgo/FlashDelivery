package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ItemOptionActivity extends BaseActivity {

    Button btnBack, btnAddCart, btnPlus, btnMinus;
    private ItemOptionAdapter adapter = null;
    private ArrayList<JSONObject> optionList = new ArrayList<JSONObject>();
    JSONArray itemOption = null;
    private Context c = this;
    private int maxQty, minQty, quantity, originalSize, totalSize;
	private ArrayList<Integer> selectedOptions;
	private SparseIntArray map = new SparseIntArray();
	private ArrayList<ArrayList<Integer>> nestedOptions = new ArrayList<ArrayList<Integer>>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_option);

        // top bar
        ActionBar actionBarTop = getActionBar();
        actionBarTop.setCustomView(R.layout.actionbar_top_item_option_activity);
        actionBarTop.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        final TextView textView1 = (TextView) findViewById(R.id.quantity);
        TextView textView2 = (TextView) findViewById(R.id.desc);
        TextView textView3 = (TextView) findViewById(R.id.price);
        TextView textView4 = (TextView) findViewById(R.id.name);

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
                // call fragment order
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

        Intent in = getIntent();
        String option = in.getStringExtra("option");
        String itemName = in.getStringExtra("name");
        String desc = in.getStringExtra("desc");
        String price = in.getStringExtra("price");
        maxQty = Integer.parseInt(in.getStringExtra("maxQty"));
        minQty = Integer.parseInt(in.getStringExtra("minQty"));
        quantity = minQty;

        textView1.setText(minQty + "");
        textView2.setText(desc.equals("")?"No Description":"Description: " + desc);
        textView3.setText("Base price: $" + price);
        textView4.setText(itemName);
        Cart.setPrice(Double.parseDouble(price));

        try    {
            itemOption = new JSONArray(option);
            for (int i = 0; i < itemOption.length(); i++)
                optionList.add(itemOption.getJSONObject(i));
            originalSize = itemOption.length();
        }
        catch (JSONException e) {e.printStackTrace();}


        if (!optionList.isEmpty()) {
            adapter = new ItemOptionAdapter(this, R.layout.list_option,
                    optionList);
            ListView list = (ListView) findViewById(R.id.listView);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                int singleSelection;
                int min, max;
                ArrayList<String> option;
				boolean nestedGroup = false;
                JSONArray oList;
                    
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
					final View v = view;
					final int pos = position;
					if (map.get(pos, -1) != -1) {
						selectedOptions = nestedOptions.get(map.get(pos));
						if (selectedOptions.size() != 0) {
							for (int i = 0; i < map.get(pos); i++)
								totalSize += nestedOptions.get(i).size();
							int correctSize = selectedOptions.size();
							for (int i = 0; i < selectedOptions.size(); i++) {
								try {
									if(oList.getJSONObject(selectedOptions.get(i))
											.getJSONArray("children").length() == 0)
										correctSize--;
								} catch (JSONException e) {e.printStackTrace();}
							}
							
							for (int i = correctSize - 1; i >= 0; i--)
								optionList.remove(originalSize + totalSize + i);
						}
					}
					
                    try {
                        min = optionList.get(position).getInt("min_selection");
                        max = optionList.get(position).getInt("max_selection");
                        oList = optionList.get(position).getJSONArray("children");
                        option = new ArrayList<String>();
                        for (int i = 0; i < oList.length(); i++) {
                            option.add(oList.getJSONObject(i).getString("name")
                                    + "\t Price: $" + oList.getJSONObject(i).getInt("price"));
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                        alert.setTitle("Select | Min: " + min + " | Max: " + max);
                        
                        if (max > 1) {
                            selectedOptions = new ArrayList<Integer>();
                            String myList[] = option.toArray(new String[option.size()]);
                            alert.setMultiChoiceItems(myList, null,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        selectedOptions.add(which);
                                    } else if (selectedOptions.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        selectedOptions.remove(Integer.valueOf(which));
                                    }
                                }
                            });
                        }
                        else {
                            String myList[] = option.toArray(new String[option.size()]);
                            singleSelection = -1;
                            if (min > 0) {
                                alert.setSingleChoiceItems(myList, 0,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        singleSelection = which;
                                    }
                                });
                            }
                            else {
                                alert.setSingleChoiceItems(myList, -1,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        singleSelection = which;
                                    }
                                });
                            }
                        }

                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String temp = "";
                                if (max > 1) {
									if (selectedOptions.size() < min) {
										AlertDialog.Builder diag = new AlertDialog.Builder(c);
										diag.setTitle("Error");
										diag.setMessage("Not select enough options!");
										diag.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
											}      
										});
										diag.show();
									}
									else {
										for (int i = 0; i < selectedOptions.size(); i++) {
											temp += " - " + option.get(selectedOptions.get(i))
													+ ((i != selectedOptions.size() - 1)?"\n":"");
											try {
												nestedGroup = false;
												if (oList.getJSONObject(selectedOptions.get(i)).getJSONArray("children").length() != 0)
													nestedGroup = true;
												if (nestedGroup) {
													if (map.get(pos, -1) == -1) {
														map.put(pos, nestedOptions.size());
														nestedOptions.add(selectedOptions);
													}
													else 
														nestedOptions.set(map.get(pos), selectedOptions);
													JSONArray tempList = oList.getJSONObject(selectedOptions.get(i))
															.getJSONArray("children");
													for (int j = 0; j < tempList.length(); j++)
														optionList.add(tempList.getJSONObject(j));				
												}
											} catch (JSONException e) {e.printStackTrace();}
										}
									}
                                }
                                else {
                                    if (singleSelection != -1) {
                                        temp = "- " + option.get(singleSelection);
                                        try {                                        
                                        	nestedGroup = false;
                                        	if (oList.getJSONObject(singleSelection).getJSONArray("children").length() != 0)
                                        		nestedGroup = true;
                                        	if (nestedGroup) {
                                        		if (map.get(pos, -1) == -1) {
                                        			selectedOptions = new ArrayList<Integer>();
                                        			selectedOptions.add(singleSelection);
                                        			map.put(pos, nestedOptions.size());
                                        			nestedOptions.add(selectedOptions);
                                        		}
                                        		else 
                                        			nestedOptions.set(map.get(pos), selectedOptions);

                                        		JSONArray tempList = oList.getJSONObject(singleSelection)
                                        				.getJSONArray("children");
                                        		for (int j = 1; j <= tempList.length(); j++)
                                        			optionList.add(tempList.getJSONObject(j));

                                        	}
                                        } catch (JSONException e) {e.printStackTrace();}                                       
                                    }
                                }

                                TextView textView5 = (TextView) v.findViewById(R.id.choice);
                                textView5.setText(temp);
								adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    }    catch (Exception e) {e.printStackTrace();}
                }
            });

        }
    }

    public static class Cart {
        private static String opt = "";
        private static double totalPrice = 0;

        public static void setOption(String s) {opt = s;}
        public static String getOption(String s) {return opt;}

        public static void setPrice(double p) {totalPrice = p;}
        public static double getPrice() {return totalPrice;}
    }
}