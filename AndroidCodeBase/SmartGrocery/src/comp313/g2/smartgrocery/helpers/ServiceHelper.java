package comp313.g2.smartgrocery.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import comp313.g2.smartgrocery.models.Notification;
import comp313.g2.smartgrocery.models.User;

import android.util.Log;


public class ServiceHelper {

	// private static final String baseURL =
	// "http://10.24.68.148/WeMeetService/WeMeetService.svc/json/";
	private static final String baseURL = "http://192.168.0.105/SmartGroceryApi/SmartGroceryApi.svc/json/";

	public String RegisterUser(User user) throws Exception {
		return GetData(baseURL+"user/register", "{\"Username\":\""+user.Username+"\", \"Password\":\""+user.Password+"\"}").replace("\"", "");
	}
	
	public String GetAuthKey(User user) throws Exception {
		return GetData(baseURL+"user/GetAuthKey", "{\"Username\":\""+user.Username+"\", \"Password\":\""+user.Password+"\"}").replace("\"", "");
	}
	
	public String AuthenticateUser(String authKey) throws Exception {
		return GetData(baseURL+"user/Authenticate", authKey).replace("\"", "");
	}
	
	public int GetNotificationCount(User user) throws Exception {
		String count = GetData(baseURL+"notifications/getCount", "{\"Username\":\""+user.Username+"\", \"SESS_KEY\":\""+user.SESS_KEY+"\"}").replace("\"", "");
		if(!count.equals("")){
			return Integer.parseInt(count);
		}
		return 0;
	}
	
	public ArrayList<Notification> GetNotifications(User user) throws Exception {
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		
		JSONArray array = new JSONArray(GetData(baseURL+"notifications/get", "{\"Username\":\""+user.Username+"\", \"SESS_KEY\":\""+user.SESS_KEY+"\"}"));
		for(int i=0;i<array.length();i++){
			JSONObject data = array.getJSONObject(i);
			
			Notification notification = new Notification();
			notification.Id = data.getInt("Id");
			notification.From = data.getString("From");
			notification.Subject = data.getString("Subject");
			notification.Text = data.getString("Text");
			notification.Date = data.getString("Date");
			notification.isRead = data.getBoolean("IsRead");
			notification.Username = data.getString("Username");
			
			notifications.add(notification);
		}
		
		return notifications;
	}

	public void SetNotificationRead(User user, int notificationId) throws Exception {
		GetData(baseURL+"notifications/setRead", 
				"{\"user\":{\"Username\":\""+user.Username+"\", \"SESS_KEY\":\""+user.SESS_KEY+"\"},\"notificationId\":\""+String.valueOf(notificationId)+"\"}");
	}
	
	public boolean DeleteNotification(User user, int notificationId) throws Exception {
		return Boolean.parseBoolean(GetData(baseURL+"notifications/delete", 
				"{\"user\":{\"Username\":\""+user.Username+"\", \"SESS_KEY\":\""+user.SESS_KEY+"\"},\"notificationId\":\""+String.valueOf(notificationId)+"\"}").replace("\"", ""));
	}
	
	private String GetData(String serviceUrl, String data) throws Exception {
		try {
			// make web service connection
            HttpPost request = new HttpPost(serviceUrl);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            StringEntity entity = new StringEntity(data);
            request.setEntity(entity);
            // Send request to WCF service
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            // Get the status of web service
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                         response.getEntity().getContent()));
            // print status in log
            String resposeData ="", line = "";
            while ((line = rd.readLine()) != null) {
                  resposeData += line;
            }
            
            return resposeData;
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
