package com.androidexample.delivery;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidexample.delivery.DisplayMerchantsActivity.MenuData;
import com.androidexample.delivery.HomeActivity.Home;
import com.androidexample.delivery.ItemOptionActivity.Item;
import com.androidexample.delivery.SearchFragment.MerchantData;

import android.net.Uri;
import android.util.Log;

/**
 * This class receives input address, creates http call to
 * the delivery server and returns the merchants' data
 * @author brian & lam & quang
 *
 */

public class ServerInteract {

    // For server authentication
    final static String host = "https://api.delivery.com/";
    final static String GUEST_TOKEN = "Guest-Token";
    final static String AUTH_TOKEN = "Authorization";
    final static String GUEST_TOKEN_URL = "customer/auth/guest";
    final static String CUSTOMER_CART_URL = "customer/cart";
    final static String CHECKOUT_URL = "customer/cart";
    final static String CC_URL = "customer/cc";
    final static String AUTH_URL = "customer/auth";
    final static String LOCATION_URL = "customer/location";
    final static String ORDER_URL = "customer/orders/recent";
    final static String SEARCH_URL = "merchant/search/";
    final static String SEARCH_ADDRESS = "1330 1st Ave, 10021";
    final static String ADDRESS_APT = "Apt 123";
    final static String CLIENT_ID = "NzBlNjU4MWNkMzhhYTU4Y2IzOGM5NzU5ZjczN2IzN2I3";
    final static String ORDER_TYPE = "delivery";
    final static String CLIENT_SECRET = "Yp7uhzncQzyBjaR5bj8f5iYc492hIspLXo3JwyiH";
    final static String URI = "http://localhost";
    final static int SEARCH_MERCHANT = 0, SEARCH_MENU = 1;
    private static String urlCode = "";
    private static double lattitude = 0, longitude = 0;

    public static void setLat(double d) {lattitude = d;}
    public static void setLong(double d) {longitude = d;}
    
    /**
     * The search method create a http call to delivery server
     * and returns the string output
     * @param address The input address
     * @return buffer The string output from server
     */  
//    public static String search(String input, int type) {
//        String url = host;
//        if (type == SEARCH_MERCHANT) {
//			try {
//				url +=  SEARCH_URL + "?client_id=" + CLIENT_ID + "&address=" + URLEncoder.encode(input, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
//		else if (type == SEARCH_MENU)
//        	url += "merchant/" + input + "/menu";
//        else
//        	url +=  SEARCH_URL + "?client_id=" + CLIENT_ID + "&latitude=" + lattitude + "&longitude=" + longitude;
//        // The input stream to hold data from server
//        InputStream is = null;
//
//        // To create http call
//        HttpURLConnection con = null;
//        try {
//            // Create a httpp call by using the url
//            con = (HttpURLConnection) (new URL(url)).openConnection();
//            // Send request and connect to sever
//            con.setRequestMethod("GET");
//            con.setDoInput(true);
//            con.connect();
//
//            // Buffer to read from the input stream
//            StringBuffer buffer = new StringBuffer();
//            is = con.getInputStream();
//
//            String line = null;
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            // Continue to read from the input stream
//            while (  (line = br.readLine()) != null )
//                buffer.append(line + "\r\n");
//
//            // Close the input stream and disconnect from server
//            is.close();
//            con.disconnect();
//
//            // Return the buffer
//            return buffer.toString();
//        }
//        catch (Throwable t) {
//            t.printStackTrace();
//        }
//        finally {
//            try { is.close(); } catch (Throwable t) {}
//            try { is.close(); } catch (Throwable t) {}
//        }
//        return null;
//    }

    public static String search(String input, int type) {
    	String url = host;
    	String orderType = MerchantData.getOrderType();
    	Log.i("Order type: " + orderType, "sdfsdf");
    	if (type == SEARCH_MERCHANT) {
    		try {
    			url +=  SEARCH_URL + orderType + "?client_id=" + CLIENT_ID + "&address=" + URLEncoder.encode(input, "UTF-8");
    		} catch (UnsupportedEncodingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	else if (type == SEARCH_MENU)
    		url += "merchant/" + input + "/menu";
    	else
    		url +=  SEARCH_URL + orderType + "?client_id=" + CLIENT_ID + "&latitude=" + lattitude + "&longitude=" + longitude;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {		
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

    public static String accountCreate(String fName, String lName, String username, String password) {
    	String url = "https://api.delivery.com/third_party/account/create";
    	String temp = "?client_id=" + Uri.encode(CLIENT_ID) + 
        		"&redirect_uri=" + URI + "&response_type=code&scope=global&state=good";
        String result = "Not Connected\n";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url + temp);

        try {
            ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            pairs.add(new BasicNameValuePair("email", username));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("first_name", fName));
            pairs.add(new BasicNameValuePair("last_name", lName));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            JSONObject responseBody = new JSONObject(httpClient.execute(httpPost, responseHandler));
            
            
            result = responseBody.getString("redirect_uri");
            if (!result.contains("?error=")) {
            	urlCode = result.substring(22, result.length() - 7);
            	result = "Signed in as: " + username;
            }
            
            return result;
        } catch (ClientProtocolException e) {e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {e1.printStackTrace();
        } catch (IOException e) {e.printStackTrace();
        } catch (JSONException e) {e.printStackTrace();}
        return result;
    }
    
    public static String userLogin(String username, String password) {
        String url = "https://api.delivery.com/third_party/authorize";
        String temp = "?client_id=" + Uri.encode(CLIENT_ID) + 
        		"&redirect_uri=" + URI + "&response_type=code&scope=global&state=good";
        String result = "Not Connected\n";
        username = "buu1989@yahoo.com";
        password = "13121989";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url + temp);

        try {
            ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            JSONObject responseBody = new JSONObject(httpClient.execute(httpPost, responseHandler));
            
            
            result = responseBody.getString("redirect_uri");
            if (!result.contains("?error=")) {
            	urlCode = result.substring(22, result.length() - 11);
            	result = username;
            	Home.setUserName(username);
            	Home.setPass(password);
            }
            
            return result;
        } catch (ClientProtocolException e) {e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {e1.printStackTrace();
        } catch (IOException e) {e.printStackTrace();
        } catch (JSONException e) {e.printStackTrace();}
        return result;
    }
    
    public static String addToCart(String authToken) {
    	String url = host + CUSTOMER_CART_URL + "/" + MenuData.getMerchantId();
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

    	// require for guest token
    	String temp = "?client_id="	+ CLIENT_ID + "&cart_index=" + key;
    	url += temp;
    	
		DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete remove = new HttpDelete(url);
		remove.setHeader(GUEST_TOKEN, authToken);
		try {		
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
		} catch (IOException e) {e.printStackTrace();}
		return "";
    }
    
    public static String getAccessToken() {
    	String url = host + "third_party/access_token";

    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpPost httpPost = new HttpPost(url);
    	try {

    		JSONObject pairs = new JSONObject();
    		pairs.put("client_id", CLIENT_ID);
    		pairs.put("redirect_uri", URI);
    		pairs.put("grant_type", "authorization_code");
    		pairs.put("client_secret", CLIENT_SECRET);
    		pairs.put("code", urlCode);

	    	
			StringEntity se = new StringEntity(pairs.toString(), "UTF-8");
			se.setContentType("application/json; charset=UTF-8");
			httpPost.setEntity(se);
			HttpResponse response = client.execute(httpPost);

			BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			JSONObject accessToken = new JSONObject(result.toString());
			return accessToken.getString("access_token");
        } catch (ClientProtocolException e) { e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {e1.printStackTrace();
        } catch (IOException e) {e.printStackTrace(); 
        } catch (JSONException e) {e.printStackTrace();}
		return "ERROR";
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