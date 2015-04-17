package comp313.g2.smartgrocery.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import comp313.g2.smartgrocery.LoginActivity;
import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.fragments.SettingsFragment;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {
	private Notification notification;
	private LocationManager lManager;
	private static final int NOTIFICATION_ID = 101;
	public static final String KEY_LAST_SYNC = "LAST_SYNC_TIME";
	private static final String NOTIFICATION_TITLE = "Smart Grocery";
	
	private SharedPreferences prefs;
	private ServiceHelper helper;
	private String username;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		prefs = PreferenceManager
				.getDefaultSharedPreferences(ReminderService.this
						.getApplicationContext());
		username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		
		// creating instance of client
		helper = new ServiceHelper();

		if (GeneralHelpers.IsConnected(ReminderService.this.getApplicationContext())) {

			fetchData();
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	private void fetchData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(username.length()>0){
					try{
						final int count = helper.getReminderCount(username,GeneralHelpers.GetCurrentDate());
						showNotification("You have "+count+" items to buy today.");
						
					}catch (Exception e) {
						
					}
				}
			}
		}).start();
	}

	private void showNotification(String message) {
		if (prefs.getBoolean(SettingsFragment.KEY_NOTIFICATION, true)) {
			Intent i = new Intent(this, LoginActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

			notification = new Notification.Builder(ReminderService.this)
					.setContentTitle(NOTIFICATION_TITLE)
					.setContentText(message)
					.setSmallIcon(R.drawable.notification)
					.setContentIntent(pIntent).build();

			if (prefs.getBoolean(SettingsFragment.KEY_NOTIFICATION_SOUND, true)) {
				notification.defaults |= Notification.DEFAULT_SOUND;
			}

			notification.ledARGB = 0xff00ff00;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;

			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// hide the notification after its selected
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;

			notificationManager.notify(NOTIFICATION_ID, notification);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Intent i = new Intent(getApplicationContext(), ReminderService.class);
		PendingIntent pi = PendingIntent.getService(getApplicationContext(),
				1111, i, PendingIntent.FLAG_ONE_SHOT);

		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.MINUTE, 30);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(),
				pi);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
