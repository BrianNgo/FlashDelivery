package com.androidexample.delivery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.androidexample.delivery.ItemOptionActivity.Item;

public class OptionFragment extends Fragment {
    private ItemOptionAdapter adapter = null;
    private ArrayList<JSONObject> optionList;
    private JSONArray itemOption = null;
    private int originalSize, totalSize;
	private ArrayList<Integer> selectedOptions;
	private SparseIntArray posToSel, posToOpt;
	private ArrayList<ArrayList<Integer>> nestedOptions;
	private Context c = Item.getContext();
	private View rootView;
	private ListView list;
	
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	posToSel = new SparseIntArray();
    	posToOpt = new SparseIntArray();
    	optionList = new ArrayList<JSONObject>();
    	nestedOptions = new ArrayList<ArrayList<Integer>>();
        rootView = inflater.inflate(R.layout.fragment_option, container, false);
        adapter = new ItemOptionAdapter(c, R.layout.list_option, optionList);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setAdapter(adapter);
        if (container == null) {
            return null;
        }
     
        try {
            itemOption = new JSONArray(Item.getOption());
            originalSize = itemOption.length();
            for (int i = 0; i < itemOption.length(); i++)
                optionList.add(itemOption.getJSONObject(i));
            
            for (int i = 0; i < optionList.size(); i++) {
            	JSONArray temp = optionList.get(i).getJSONArray("children");
            	posToOpt.put(i, i);
            	if (temp.length() != 0) {
            		if (optionList.get(i).getInt("min_selection") == 1 &&
            				optionList.get(i).getInt("max_selection") == 1) {
            			double price = temp.getJSONObject(0).getDouble("price");
            			Item.addOption("- " + temp.getJSONObject(0).getString("name")
            					+ ((price == 0)?"":" Price: $" + price));
            		}
            		else
            			Item.addOption("");
            	}
            	else
            		Item.addOption("");
            }
        }
        catch (JSONException e) {e.printStackTrace();}
        
        return rootView;
     }

     @Override
     public void onResume() {
    	 super.onResume();
    	 
        if (!optionList.isEmpty()) {
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
					
                    try {
                        min = optionList.get(position).getInt("min_selection");
                        max = optionList.get(position).getInt("max_selection");
                        oList = optionList.get(position).getJSONArray("children");
                        option = new ArrayList<String>();
                        for (int i = 0; i < oList.length(); i++) {
                        	double price = oList.getJSONObject(i).getDouble("price");
                            option.add(oList.getJSONObject(i).getString("name")
                                    + ((price==0)?"":(" Price: $" + price )));
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                        alert.setTitle("Select | Min: " + min + " | Max: " + max);

                        String myList[] = option.toArray(new String[option.size()]);                       
                        if (max > 1) {
                            selectedOptions = new ArrayList<Integer>();
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
                            	
            					if (posToSel.get(pos, -1) != -1) {
            						ArrayList<Integer> temp = nestedOptions.get(posToSel.get(pos));
            						if (temp.size() != 0) {
            							totalSize = 0;
            							for (int i = 0; i < posToSel.get(pos); i++) {
            								for (int j = 0; j < nestedOptions.get(i).size(); j++) {
            									try {
            										if(oList.getJSONObject(nestedOptions.get(i).get(j))
            												.getJSONArray("children").length() != 0) {
            											totalSize += nestedOptions.get(i).size();
            										}
            									} catch (JSONException e) {}
            								}
            							}
            							int correctSize = temp.size();
            							for (int i = 0; i < temp.size(); i++) {
            								try {
            									if(oList.getJSONObject(temp.get(i))
            											.getJSONArray("children").length() == 0) {
            										correctSize--;
            									}
            								} catch (JSONException e) {correctSize--;}
            							}
            							
            							Log.i("Correct: " + correctSize + " Nest : " + nestedOptions.size()
            									+ "sel: " + temp.size() + "total" + totalSize, "***");
            							
            							for (int i = correctSize - 1; i >= 0; i--) {
            								optionList.remove(originalSize + totalSize + i);
                                    		Item.removeOption(pos);
            							}
            						}
            					}
                            	
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
										if (posToSel.get(pos, -1) == -1) {
											posToSel.put(pos, nestedOptions.size());
											nestedOptions.add(selectedOptions);
										}
										else 
											nestedOptions.set(posToSel.get(pos), selectedOptions);
										
										for (int i = 0; i < selectedOptions.size(); i++) {
											temp += "- " + option.get(selectedOptions.get(i))
													+ ((i != selectedOptions.size() - 1)?"\n":"");

											try {
												nestedGroup = false;
												if (oList.getJSONObject(selectedOptions.get(i)).getJSONArray("children").length() != 0)
													nestedGroup = true;
												if (nestedGroup) {
													JSONArray tempList = oList.getJSONObject(selectedOptions.get(i))
															.getJSONArray("children");
													for (int j = 0; j < tempList.length(); j++) {
														optionList.add(tempList.getJSONObject(j));
														posToOpt.put(originalSize + i + j, Item.getItem().size());
														String name = tempList.getJSONObject(j).getJSONArray("children")
																.getJSONObject(0).getString("name");
														double price = tempList.getJSONObject(j).getJSONArray("children")
																.getJSONObject(0).getDouble("price");
														String o = "- " + name + ((price == 0)?"":" Price: $" + price);
														Item.addOption(o);
													}
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
                                        		JSONArray tempList = oList.getJSONObject(singleSelection)
                                        				.getJSONArray("children");
                                        		for (int j = 0; j < tempList.length(); j++) {
                                        			optionList.add(tempList.getJSONObject(j));
                                        			posToOpt.put(originalSize + j, Item.getItem().size());
                                        			String name = tempList.getJSONObject(j).getJSONArray("children")
                                                			.getJSONObject(0).getString("name");
                                        			double price = tempList.getJSONObject(j).getJSONArray("children")
                                                			.getJSONObject(0).getDouble("price");
                                        			String o = "- " + name + ((price == 0)?"":" Price: $" + price);
                                            		Item.addOption(o);
                                        		}
                                        	}
                                        } catch (JSONException e) {e.printStackTrace();}                                       
                                    }
                                    else if (min > 0) {
                                    	singleSelection = 0;
                                    	temp = "- " + option.get(singleSelection);
                                    }
                            		if (posToSel.get(pos, -1) == -1) {
                            			selectedOptions = new ArrayList<Integer>();
                            			selectedOptions.add(singleSelection);
                            			posToSel.put(pos, nestedOptions.size());
                            			nestedOptions.add(selectedOptions);
                            		}
                            		else 
                            			nestedOptions.set(posToSel.get(pos), selectedOptions);
                                }

                                TextView textView5 = (TextView) v.findViewById(R.id.choice);
                                textView5.setText(temp.equals("")?"No choice":temp);

                                if (pos < Item.getItem().size())
                                	Item.setOption(pos, temp);
                                else {
                                	if (posToOpt.get(pos, -1) == -1) {
                                		posToOpt.put(pos, Item.getItem().size());
                                		Item.addOption(temp);
                                	}
                                	else
                                		Item.setOption(posToOpt.get(pos), temp);
                                }
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
}