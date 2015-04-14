package comp313.g2.smartgrocery.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import comp313.g2.smartgrocery.models.ListItem;
import comp313.g2.smartgrocery.models.Notification;
import comp313.g2.smartgrocery.models.Template;
import comp313.g2.smartgrocery.models.TemplateItem;
import comp313.g2.smartgrocery.models.User;

import android.util.Log;

public class ServiceHelper {

	// private static final String baseURL =
	// "http://10.24.68.148/WeMeetService/WeMeetService.svc/json/";
	private static final String baseURL = "http://192.168.0.105/SmartGroceryApi/SmartGroceryApi.svc/json/";
	
	public String RegisterUser(User user) throws Exception {
		return PostData(
				baseURL + "user/register",
				"{\"Username\":\"" + user.Username + "\", \"Password\":\""
						+ user.Password + "\"}").replace("\"", "");
	}

	public String GetAuthKey(User user) throws Exception {
		return PostData(
				baseURL + "user/GetAuthKey",
				"{\"Username\":\"" + user.Username + "\", \"Password\":\""
						+ user.Password + "\"}").replace("\"", "");
	}

	public String AuthenticateUser(String authKey) throws Exception {
		return PostData(baseURL + "user/Authenticate", authKey)
				.replace("\"", "");
	}

	public int GetNotificationCount(User user) throws Exception {
		String count = PostData(
				baseURL + "notifications/getCount",
				"{\"Username\":\"" + user.Username + "\", \"SESS_KEY\":\""
						+ user.SESS_KEY + "\"}").replace("\"", "");
		if (!count.equals("")) {
			return Integer.parseInt(count);
		}
		return 0;
	}

	public ArrayList<Notification> GetNotifications(User user) throws Exception {
		ArrayList<Notification> notifications = new ArrayList<Notification>();

		String response = PostData(baseURL + "notifications/get",
				"{\"Username\":\"" + user.Username + "\", \"SESS_KEY\":\""
						+ user.SESS_KEY + "\"}");
		if (response.length() > 2) {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
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
		}
		return notifications;
	}

	public void SetNotificationRead(User user, int notificationId)
			throws Exception {
		PostData(baseURL + "notifications/setRead", "{\"user\":{\"Username\":\""
				+ user.Username + "\", \"SESS_KEY\":\"" + user.SESS_KEY
				+ "\"},\"notificationId\":\"" + String.valueOf(notificationId)
				+ "\"}");
	}

	public boolean DeleteNotification(User user, int notificationId)
			throws Exception {
		return Boolean.parseBoolean(PostData(
				baseURL + "notifications/delete",
				"{\"user\":{\"Username\":\"" + user.Username
						+ "\", \"SESS_KEY\":\"" + user.SESS_KEY
						+ "\"},\"notificationId\":\""
						+ String.valueOf(notificationId) + "\"}").replace("\"",
				""));
	}

	public ArrayList<comp313.g2.smartgrocery.models.List> GetLists(User user) throws Exception {
		ArrayList<comp313.g2.smartgrocery.models.List> lists = new ArrayList<comp313.g2.smartgrocery.models.List>();
		String response = PostData(baseURL + "list/", "{\"Username\":\""
				+ user.Username + "\", \"SESS_KEY\":\"" + user.SESS_KEY + "\"}");
		if (response.length() > 2) {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject data = array.getJSONObject(i);

				comp313.g2.smartgrocery.models.List list = new comp313.g2.smartgrocery.models.List();
				list.Id = data.getInt("Id");
				list.Name = data.getString("Name");
				list.Color = data.getString("Color");
				list.LastModified = data.getString("LastModified");
				list.Username = data.getString("Username");

				lists.add(list);
			}
		}
		return lists;
	}

	public String GetListLastModified(User user, long listId) throws Exception {
		String lastModified = PostData(
				baseURL + "list/getLastModified",
				"{\"user\":{\"Username\":\"" + user.Username + "\", \"SESS_KEY\":\""
						+ user.SESS_KEY + "\"}, \"listId\":\""+listId+"\"}").replace("\"", "");
		if (lastModified!=null && lastModified.length()<20) {
			return lastModified;
		}
		return "";
	}

	public long AddList(comp313.g2.smartgrocery.models.List list, User user) throws Exception{
		String response = PostData(baseURL+"list/add",
				"{"
				+"\"list\":{"
						+ "\"Name\":\""+list.Name+"\","
						+ "\"Color\":\""+list.Color+"\","
						+ "\"LastModified\":\""+ GeneralHelpers.GetCurrentDate()+"\","
						+ "\"Username\":\""+user.Username+"\""
						+"},"
				+"\"user\":{"
						+ "\"Username\":\""+user.Username+"\","
						+"\"SESS_KEY\":\""+user.SESS_KEY+"\""
						+"}"
				+"}").replace("\"", "");
		
		if(response.length()>0){
			return Long.parseLong(response);
		}
		return -1;
	}
	
	public boolean RenameList(User user, long listId, String newName) throws Exception {
		String response = PostData(
				baseURL + "list/rename",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"listId\":\""+listId+"\","+
				"\"newName\":\""+newName+"\"}").replace("\"", "");
		
		if (!response.equals("")) {
			return Boolean.parseBoolean(response);
		}
		return false;
	}
	
	/* Share Unshare remaining */
	
	public comp313.g2.smartgrocery.models.List DuplicateList(User user, long listId, String newListName) throws Exception {
		
		String response = PostData(
				baseURL + "list/duplicate",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"listId\":\""+listId+"\","+
				"\"listName\":\""+newListName+"\"}");
		
		if (!response.equals("")) {
			comp313.g2.smartgrocery.models.List list = new comp313.g2.smartgrocery.models.List();
			
			JSONObject listData = new JSONObject(response);
			list.Id = listData.getInt("Id");
			list.Name = listData.getString("Name");
			list.Color = listData.getString("Color");
			list.LastModified = listData.getString("LastModified");
			list.Username = listData.getString("Username");
			
			return list;
		}
		return null;
	}
	
	public boolean DeleteList(User user, long listId) throws Exception {
		String response = PostData(
				baseURL + "list/delete",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"listId\":\""+listId+"\"}").replace("\"", "");
		
		if (!response.equals("")) {
			return Boolean.parseBoolean(response);
		}
		return false;
	}



	public ArrayList<comp313.g2.smartgrocery.models.ListItem> GetListItems(User user, long listId) throws Exception {
		ArrayList<comp313.g2.smartgrocery.models.ListItem> listItems = new ArrayList<comp313.g2.smartgrocery.models.ListItem>();
		String response = PostData(baseURL + "listitems/", 
				"{" +
						"\"user\":{"+
							"\"Username\":\""+ user.Username + "\", " +
							"\"SESS_KEY\":\"" + user.SESS_KEY + "\"" +
							"}," +
						"\"listId\":\""+String.valueOf(listId)+"\""+
				"}");
		
		if (response.length() > 2) {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject data = array.getJSONObject(i);

				comp313.g2.smartgrocery.models.ListItem listItem = new comp313.g2.smartgrocery.models.ListItem();
				listItem.Id = data.getInt("Id");
				listItem.Name = data.getString("Name");
				listItem.LastModified = data.getString("LastModified");
				listItem.Reminder = data.getString("Reminder");
				listItem.ListId = data.getLong("ListId");
				listItem.Quantity =(float) data.getDouble("Quantity");
				listItem.Unit =data.getString("Unit");

				listItems.add(listItem);
			}
		}
		return listItems;
	}

	public int GetItemCount(User user, long listId) throws Exception {
		String count = PostData(
				baseURL + "listitems/count",
				"{\"user\":{\"Username\":\"" + user.Username + "\", \"SESS_KEY\":\""
						+ user.SESS_KEY + "\"}, \"listId\":\""+listId+"\"}").replace("\"", "");
		if (!count.equals("")) {
			return Integer.parseInt(count);
		}
		return 0;
	}

	public long AddListItem(comp313.g2.smartgrocery.models.ListItem listItem, User user) throws Exception{
		String response = PostData(baseURL+"listitems/add",
				"{"
				+"\"list\":{"
						+ "\"Name\":\""+listItem.Name+"\","
						+ "\"Quantity\":\""+listItem.Quantity+"\","
						+ "\"Reminder\":\""+listItem.Reminder+"\","
						+ "\"Unit\":\""+listItem.Unit+"\","
						+ "\"ListId\":\""+listItem.ListId+"\","
						+ "\"LastModified\":\""+ GeneralHelpers.GetCurrentDate()+"\""
						+"},"
				+"\"user\":{"
						+ "\"Username\":\""+user.Username+"\","
						+"\"SESS_KEY\":\""+user.SESS_KEY+"\""
						+"}"
				+"}").replace("\"", "");
		
		if(response.length()>0){
			return Long.parseLong(response);
		}
		return -1;
	}
	

	
	public boolean UpdateListItem(User user, ListItem listItem, boolean addToHistory) throws Exception {
		/*String response = PostData(
				baseURL + "list/delete",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"listId\":\""+listId+"\"}").replace("\"", "");
		
		if (!response.equals("")) {
			return Boolean.parseBoolean(response);
		}*/
		return false;
	}
	
	public boolean DeleteListItem(User user, long listItemId) throws Exception {
		String response = PostData(
				baseURL + "listitems/delete",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"itemId\":\""+listItemId+"\","+
				"\"time\":\""+GeneralHelpers.GetCurrentDate()+"\"}").replace("\"", "");
		
		if (!response.equals("")) {
			return Boolean.parseBoolean(response);
		}
		return false;
	}
	
	private String PostData(String serviceUrl, String data) throws Exception {
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
			String resposeData = "", line = "";
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
	

	
	public ArrayList<Template> GetTemplates() throws Exception{
		ArrayList<Template> templates = new ArrayList<Template>(); 
		
		String data = GetData(baseURL+"templates");
		
		if(data!=null && data.length()>2){
			JSONArray array = new JSONArray(data);
			for(int i=0;i<array.length();i++){
				JSONObject tempateData = array.getJSONObject(i);
				
				Template template = new Template();
				template.Id = tempateData.getLong("Id");
				template.Name = tempateData.getString("Name");
				template.Color = tempateData.getString("Color");
				
				templates.add(template);
			}
		}
		
		return templates;
	}
	
	public ArrayList<TemplateItem> GetTemplateItems(long templateId) throws Exception{
		ArrayList<TemplateItem> templateItems = new ArrayList<TemplateItem>(); 
		
		String data = GetData(baseURL+"templates/"+String.valueOf(templateId));
		
		if(data!=null && data.length()>2){
			JSONArray array = new JSONArray(data);
			for(int i=0;i<array.length();i++){
				JSONObject tempateData = array.getJSONObject(i);
				
				TemplateItem item = new TemplateItem();
				item.Id = tempateData.getLong("Id");
				item.Name = tempateData.getString("Name");
				item.Quantity =(float) tempateData.getDouble("Quantity");
				item.Unit = tempateData.getString("Unit");
				item.TemplateId = templateId;
				
				templateItems.add(item);
			}
		}
		
		return templateItems;
	}
	
	public String AddTemplateToList(User user,long templateId) throws Exception{
		return PostData(
				baseURL + "templatetolist/",
				"{" +
				"\"user\":{" +
					"\"Username\":\"" + user.Username + "\"," +
					 "\"SESS_KEY\":\""+ user.SESS_KEY + "\"" +
					 "}, " +
				"\"templateId\":\""+String.valueOf(templateId)+"\" }").replace("\"", "");
	}
	
	private String GetData(String serviceUrl) throws Exception {
		try {
			// make web service connection
			HttpGet request = new HttpGet(serviceUrl);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			
			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			// Get the status of web service
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			// print status in log
			String resposeData = "", line = "";
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
