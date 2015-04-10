package comp313.g2.smartgrocery;

import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.Notification;
import comp313.g2.smartgrocery.models.User;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class NotificationActivity extends Activity {
	private Notification notification;
	private SharedPreferences prefs;

	// private UI fields
	private TextView tvSubject, tvFrom, tvDate, tvText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		// initializing components
		InitializeComponents();
	}

	private void InitializeComponents() {
		// getting notification
		Bundle bundle = getIntent().getExtras();
		notification = bundle
				.getParcelable("comp313.g2.smartgrocery.models.Notification");
		// initializing UI components
		tvSubject = (TextView) findViewById(R.id.tvSubject);
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvText = (TextView) findViewById(R.id.tvText);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		if (!notification.isRead) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						ServiceHelper helper = new ServiceHelper();
						User user = new User();
						user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
						user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
						
						helper.SetNotificationRead(user, notification.Id);
					} catch (Exception e) {
						Log.e("Smart Grocery", e.getMessage());
					}
				}
			}).start();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// setting up action bar
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		bar.setTitle(notification.Subject);
		bar.setIcon(R.drawable.ic_menu_notifications);

		// setting text for UI components
		if (notification != null) {
			tvSubject.setText(notification.Subject);
			tvDate.setText(notification.Date);
			tvFrom.setText(notification.From);
			tvText.setText(notification.Text);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
