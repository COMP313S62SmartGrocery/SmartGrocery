package comp313.g2.smartgrocery.helpers;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

public class NutritionServiceHelper {
	private static final String DEV_KEY = "0yUbnq4pq2Yio1sRghSGM9ZpbMBT8FrAHpnczMvy";
	private static final String NUTRITION_INFO_API = "http://api.nal.usda.gov/usda/ndb/reports/?type=f&format=json";
	private static final String SEARCH_API = "http://api.nal.usda.gov/usda/ndb/search/?format=json&sort=r&max=25&offset=0";
	
	public HashMap<String, String> GetNutritionDetails(String ndbno){
		HashMap<String, String> result = new HashMap<String, String>();
		
		String url = NUTRITION_INFO_API+"&ndbno="+ndbno+"&api_key="+DEV_KEY;
		try {
			JSONObject data = new JSONObject(GetData(url).toString());
			//getting array of items
			data = data.getJSONObject("report");
			data = data.getJSONObject("food");
			
			//getting array of results
			JSONArray array = data.getJSONArray("nutrients");
			
			for(int i=0;i<array.length();i++){
				data = array.getJSONObject(i);
				result.put(data.getString("name"), data.getString("value")+" "+data.getString("unit"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public HashMap<String, String> Search(String query){
		HashMap<String, String> result = new HashMap<String, String>();
		
		String url = SEARCH_API+"&q="+query+"&api_key="+DEV_KEY;
		try {
			JSONObject data = new JSONObject(GetData(url).toString());
			//getting array of items
			data = data.getJSONObject("list");
			//getting array of results
			JSONArray array = data.getJSONArray("item");
			
			for(int i=0;i<array.length();i++){
				data = array.getJSONObject(i);
				result.put(data.getString("name"), data.getString("ndbno"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	private Object GetData(String url) throws Exception {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);

			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				return EntityUtils.toString(entity);
			} else {
				throw new Exception("Connection error");
			}

		} catch (ClientProtocolException e) {
			Log.e("WeMeet_Exception", e.getMessage());
			throw e;
		} catch (IOException e) {
			Log.e("WeMeet_Exception", e.getMessage());
			throw e;
		} catch (ParseException e) {
			Log.e("WeMeet_Exception", e.getMessage());
			throw e;
		} catch (Exception e) {
			Log.e("WeMeet_Exception", e.getMessage());
			throw e;
		}
	}
}
