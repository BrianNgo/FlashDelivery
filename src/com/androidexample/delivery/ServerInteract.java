package com.androidexample.delivery;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;
import com.androidexample.delivery.ItemOptionActivity.Item;
import com.androidexample.delivery.SearchFragment.MerchantData;

import android.net.Uri;

/**
 * This class receives input address, creates http call to
 * the delivery server and returns the merchants' data
 * @author brian & lam & quang
 *
 */

public class ServerInteract {

    // For server authentication
    final static String host = "http://sandbox.delivery.com/";
    final static String GUEST_TOKEN = "Guest-Token";
    final static String AUTH_TOKEN = "Authorization";
    final static String GUEST_TOKEN_URL = "customer/auth/guest";
    final static String CUSTOMER_CART_URL = "customer/cart";
    final static String CHECKOUT_URL = "customer/cart";
    final static String CC_URL = "customer/cc";
    final static String AUTH_URL = "customer/auth";
    final static String LOCATION_URL = "customer/location";
    final static String ORDER_URL = "customer/orders/recent";
    final static String SEARCH_URL = "merchant/search/delivery";
    final static String SEARCH_ADDRESS = "1330 1st Ave, 10021";
    final static String ADDRESS_APT = "Apt 123";
    final static String CLIENT_ID = "MzMxMjA4N2FjOWM0YjQ1YmIyYzgwMTI1MmIzMjA1MDYz";
    final static String ORDER_TYPE = "delivery";

    /**
     * The search method create a http call to delivery server
     * and returns the string output
     * @param address The input address
     * @return buffer The string output from server
     */
    public static String search(String input, int type) {
        String url = "";
        if (type == 0)
            // The url to connect
            url = "https://api.delivery.com/merchant/search/delivery?client_id=ZjkxODFiNWRkMTYzOWNhMzEzZTk4ZTZjNTU4MDM2ZjJj&address=1330%201st%20Ave,%2010021";
        else
 //           url = "https://api.delivery.com/merchant/" + input + "/menu";
            url = host + "merchant/" + 3215 + "/menu";
//        url = host + SEARCH_URL + "?address=" + SEARCH_ADDRESS + "&client_id=" + CLIENT_ID;
        // The input stream to hold data from server
        InputStream is = null;

        // To create http call
        HttpURLConnection con = null;
        try {
            // Create a httpp call by using the url
            con = (HttpURLConnection) (new URL(url)).openConnection();
            // Send request and connect to sever
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

            // Buffer to read from the input stream
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();

            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Continue to read from the input stream
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            // Close the input stream and disconnect from server
            is.close();
            con.disconnect();

            // Return the buffer
            return buffer.toString();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch (Throwable t) {}
            try { is.close(); } catch (Throwable t) {}
        }

        return null;
    }
    
    public static ArrayList<Merchant> createMerList(JSONObject result) {

        ArrayList<Merchant> m = new ArrayList<Merchant>();
        try {
            JSONObject searchResult = result;
            JSONArray mArray = searchResult.getJSONArray("merchants");

            // variables that hold infor. for a merchant
            String name, cuisine;
            boolean status;
            int id, rating;
            double distance;
            // create array of merchants
            for (int i = 0; i < mArray.length(); i++)
            {
                if (mArray.getJSONObject(i).getJSONObject("summary").get("type").equals("R")) {
                    cuisine = "";
                    name = mArray.getJSONObject(i).getJSONObject("summary").getString("name");
                    id = mArray.getJSONObject(i).getInt("id");
                    rating = mArray.getJSONObject(i).getJSONObject("summary").getInt("overall_rating");
                    status = mArray.getJSONObject(i).getJSONObject("ordering").getBoolean("is_open");
                    try {
                        JSONArray cui = mArray.getJSONObject(i).getJSONObject("summary")
                                .getJSONArray("cuisines");
                        int size = cui.length();
                        for (int j = 0; j < size; j++)
                            if (j < size - 1)
                                cuisine = cuisine + cui.getString(j) + ", ";
                            else
                                cuisine = cuisine + cui.getString(j);
                    } catch (JSONException e) {
                        cuisine = "No tags";
                    }
                    distance = mArray.getJSONObject(i).getJSONObject("location").getDouble("distance");
                    m.add(new Merchant(name, id, rating, status, cuisine, distance));
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
        return m;
    }

    public static String userLogin(String username, String password) {
        String uri = "http://localhost";
        String url = "https://api.delivery.com/third_party/authorize?client_id=ZjkxODFiNWRkMTYzOWNhMzEzZTk4ZTZjNTU4MDM2ZjJj&scope=global&redirect_uri=" + uri + "&state=good&response_type=code";
        String result = "Not Connected\n";
        password = "13121989";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>(2);
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();

            if (status.getStatusCode() == HttpStatus.SC_OK) {
                result = "Successfully Connected\n";
            }

            return result;
        } catch (ClientProtocolException e) {e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {e1.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}
        return result;
    }
    
    public static String addToCart(String authToken) {
    	String url = host + CUSTOMER_CART_URL + "/" + MenuData.getMerchantId();
        url = host + CUSTOMER_CART_URL + "/" + 3215;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader(GUEST_TOKEN, authToken);
	    	JSONObject formData = new JSONObject();
	    	// creating item body
			String optionQuantity = "", option = "";
			ArrayList<String> temp = Item.getOption();
			for (int i = 0; i < temp.size(); i++)
				option += temp.get(i);
			String[] opt = option.split("ID:");
			for (int i = 0; i < opt.length; i++) {
				if (!opt[i].equals("")) {
					if (i != opt.length - 1)
						optionQuantity += "\"" + opt[i] + ",";
					else
						optionQuantity += "\"" + opt[i];
				}
			}
			
			String itemString = "{" + "\"item_id\": \"" + Item.getItemId() + "\","
					+ "\"item_qty\": " + Item.getQuantity() + ","
					+ "\"option_qty\": {" + optionQuantity + "}" + "}";
			JSONObject item = new JSONObject(itemString);
			
	    	formData.put("item", item);
	    	formData.put("order_type", ORDER_TYPE);
	    	formData.put("instructions", Item.getInstr());
	    	formData.put("client_id", CLIENT_ID);
	    	
			StringEntity se = new StringEntity(formData.toString(), "UTF-8");
			se.setContentType("application/json; charset=UTF-8");
			post.setEntity(se);
			HttpResponse response = client.execute(post);

			BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {e1.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		} catch (JSONException e) {e.printStackTrace();}
		return "";
    }
 
    public static String viewCart(String authToken, String orderTime) {
    	String url = host + CUSTOMER_CART_URL + "/" + MenuData.getMerchantId();
    	JSONObject address = MerchantData.getAddress();
    	try {
    		url = host + CUSTOMER_CART_URL + "/" + 3215;
    		String temp = "?client_id="	+ CLIENT_ID
    				+ "&zip=" + Uri.encode(address.getString("zip_code"))
    				+ "&city=" + Uri.encode(address.getString("city"))
    				+ "&state=" + Uri.encode(address.getString("state"))
    				+ "&latitude=" + Uri.encode(address.getString("latitude"))
    				+ "&longitude=" + Uri.encode(address.getString("longitude"))
    				+ "&order_type=" + ORDER_TYPE;
    		url += temp;;
		} catch (JSONException e) {e.printStackTrace();}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {
			request.setHeader(GUEST_TOKEN, authToken);			
			HttpResponse response = client.execute(request);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {e1.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
		return "";
    }
    
    public static String removeOrder(int key, String authToken) {
    	String url = host + CUSTOMER_CART_URL + "/" + MenuData.getMerchantId();
    	url = host + CUSTOMER_CART_URL + "/" + 3215;
    	// require for guest token
    	String temp = "?client_id="	+ CLIENT_ID;
    	url += temp;
    	
		DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete remove = new HttpDelete(url);
		try {
			remove.setHeader(GUEST_TOKEN, authToken);			
			if (key != -1) {
				JSONObject formData = new JSONObject();
				formData.put("cart_index", key);
				StringEntity se = new StringEntity(formData.toString(), "UTF-8");
				se.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			}
						
			HttpResponse response = client.execute(remove);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {e1.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		} catch (JSONException e) {e.printStackTrace();}
		return "";
    }
    
    public static String getGuestToken(String clientId) {
    	String url = host + GUEST_TOKEN_URL + "?client_id=" + CLIENT_ID;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = br.readLine()) != null) {
					result.append(line);
				}
				JSONObject temp = new JSONObject(result.toString());
				return temp.getString("Guest-Token");
			}
        } catch (ClientProtocolException e) { e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {e1.printStackTrace();
        } catch (IOException e) {e.printStackTrace();
        } catch (JSONException e) {e.printStackTrace();}
		return "";
    }
}