package com.challengepost.flashdelivery;

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

import com.challengepost.flashdelivery.ItemOptionActivity.Item;

public class OptionFragment extends Fragment {
    private OptionAdapter adapter = null;
    private ArrayList<JSONObject> optionList;
    private int originalSize, totalSize;
	private SparseIntArray posToSel, posToOpt;
	private ArrayList<ArrayList<Integer>> allOptions;
	private Context c = Item.getContext();
	private View rootView;
	private ListView list;
	private ArrayList<Integer> selectedOptions = new ArrayList<Integer>();
	
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	posToSel = new SparseIntArray();
    	posToOpt = new SparseIntArray();
    	optionList = new ArrayList<JSONObject>();
    	allOptions = new ArrayList<ArrayList<Integer>>();
		Item.resetOption();
        rootView = inflater.inflate(R.layout.fragment_option, container, false);
        adapter = new OptionAdapter(c, R.layout.list_option, optionList);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setAdapter(adapter);
        if (container == null) {
            return null;
        }
     
        try {
            JSONArray itemOption = new JSONArray(Item.getOpt());
            originalSize = itemOption.length();
            for (int i = 0; i < itemOption.length(); i++)
                optionList.add(itemOption.getJSONObject(i));
            
            for (int i = 0; i < optionList.size(); i++) {
        		int min = optionList.get(i).getInt("min_selection");
        		int max = optionList.get(i).getInt("max_selection");
            	JSONArray temp = optionList.get(i).getJSONArray("children");
            	posToOpt.put(i, i);
            	if (temp.length() != 0) {
            		if (min >= 1 && max >= min) {
            			String strId = "";
            			for (int j = 0; j < min; j++)
            				strId += "ID:" + temp.getJSONObject(j).getString("id") + "\":" + 1;
            			Item.addOption(strId);
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

     public void addNestedOption(JSONArray tempList) {
    	 try {
    		 for (int j = 0; j < tempList.length(); j++) {
    			 optionList.add(tempList.getJSONObject(j));
    			 posToOpt.put(optionList.size() + j, Item.getOption().size());
    			 int subMin = tempList.getJSONObject(j).getInt("min_selection");
    			 String tempID = "";
    			 ArrayList<Integer> newSelection = new ArrayList<Integer>();
    			 for (int k = 0; k < subMin; k++) {
    				 try {
    					 tempID = "ID:" + tempList.getJSONObject(j).getJSONArray("children")
    							 .getJSONObject(k).getString("id") + "\":" + 1;
    					 newSelection.add(k);
    				 } catch (JSONException e) {};
    			 }
    			 allOptions.add(newSelection);
    			 Item.addOption(tempID);
    		 }
    	 } catch (JSONException e) {}
     }
     
     public void removeOption(JSONArray o,int pos) {
			ArrayList<Integer> temp = allOptions.get(posToSel.get(pos));
			if (temp.size() != 0) {
				totalSize = 0;
				for (int i = 0; i < posToSel.get(pos); i++) {
					for (int j = 0; j < allOptions.get(i).size(); j++) {
						try {
							if(o.getJSONObject(allOptions.get(i).get(j))
									.getJSONArray("children").length() != 0) {
								totalSize += allOptions.get(i).size();
							}
						} catch (JSONException e) {}
					}
				}
				int correctSize = 0;
				for (int i = 0; i < temp.size(); i++) {
					try {
						int size = o.getJSONObject(temp.get(i))
								.getJSONArray("children").length();
						Log.i("Size: " + size, "********");
						if(size != 0) {
							correctSize += size;
						}
					} catch (JSONException e) {}
				}
				
				Log.i("Correct: " + correctSize + " Nest : " + allOptions.size()
						+ "sel: " + temp.size() + "total" + totalSize, "***");
				
				for (int i = correctSize - 1; i >= 0; i--)
					optionList.remove(originalSize + totalSize + i);
			}
     }
     
     @Override
     public void onResume() {
    	 super.onResume();
        if (!optionList.isEmpty()) {
            list.setOnItemClickListener(new OnItemClickListener() {

                int singleSelection = -1;
                int min = 0, max = 0;
                ArrayList<String> option, optionId;
				boolean nestedGroup = false;
                JSONArray oList;
                    
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
					final View v = view;
					final int pos = position;
	            	selectedOptions = new ArrayList<Integer>();
                    option = new ArrayList<String>();
                    optionId = new ArrayList<String>();
                    try {
                        min = optionList.get(position).getInt("min_selection");
                        max = optionList.get(position).getInt("max_selection");
                        oList = optionList.get(position).getJSONArray("children");
                        for (int i = 0; i < oList.length(); i++) {
                        	double price = oList.getJSONObject(i).getDouble("price");
                            option.add(oList.getJSONObject(i).getString("name")
                                    + ((price==0)?"":(" - Price: $" + price )));
                            optionId.add(oList.getJSONObject(i).getString("id"));
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                        alert.setTitle("Select | Min: " + min + " | Max: " + max);

                        String myList[] = option.toArray(new String[option.size()]);                       
                        if (max > 1) {
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
                                selectedOptions.add(singleSelection);
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
            						removeOption(oList ,pos);
            					}
                            	
                                String tempId = "", temp = "";
                                // have multiple options
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
											posToSel.put(pos, allOptions.size());
											allOptions.add(selectedOptions);
										}
										else 
											allOptions.set(posToSel.get(pos), selectedOptions);
										
										for (int i = 0; i < selectedOptions.size(); i++) {
											temp += "- " + option.get(selectedOptions.get(i))
													+ ((i != selectedOptions.size() - 1)?"\n":"");
											tempId += "ID:" + optionId.get(selectedOptions.get(i)) + "\":" + 1;
											try {
												nestedGroup = false;
												if (oList.getJSONObject(selectedOptions.get(i)).getJSONArray("children").length() != 0)
													nestedGroup = true;
												if (nestedGroup) {
													addNestedOption(oList.getJSONObject(selectedOptions.get(i))
															.getJSONArray("children"));
												}
											} catch (JSONException e) {e.printStackTrace();}
										}
									}
                                }
                                // single option
                                else {
                                	if (singleSelection != -1) {
                                    	temp = "- " + option.get(singleSelection);
                                        tempId = "ID:" + optionId.get(singleSelection) + "\":" + 1;
                                        try {                                        
                                        	nestedGroup = false;
                                        	if (oList.getJSONObject(singleSelection).getJSONArray("children").length() != 0)
                                        		nestedGroup = true;
                                        	if (nestedGroup) {
												addNestedOption(oList.getJSONObject(singleSelection)
														.getJSONArray("children"));
                                        	}
                                        } catch (JSONException e) {e.printStackTrace();}                                       
                                    }
                            		if (posToSel.get(pos, -1) == -1) {
                            			posToSel.put(pos, allOptions.size());
                            			allOptions.add(selectedOptions);
                            		}
                            		else 
                            			allOptions.set(posToSel.get(pos), selectedOptions);
                                }

                                TextView textView5 = (TextView) v.findViewById(R.id.choice);
                                textView5.setText(temp.equals("")?"No choice":temp);

                                if (pos < Item.getOption().size())
                                	Item.setOption(pos, tempId);
                                else {
                                	if (posToOpt.get(pos, -1) == -1) {
                                		posToOpt.put(pos, Item.getOption().size());
                                		Item.addOption(tempId);
                                	}
                                	else
                                		Item.setOption(posToOpt.get(pos), tempId);
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
                    for (int i = 0; i < Item.getOption().size(); i++)
                    	Log.i("ID: " + Item.getOption().get(i), "(())))");
                }
            });

        }       
    }
}
