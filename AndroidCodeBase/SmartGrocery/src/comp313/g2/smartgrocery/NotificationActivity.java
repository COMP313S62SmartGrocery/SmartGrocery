package comp313.g2.smartgrocery;

import comp313.g2.smartgrocery.datasources.NotificationDataSource;
import comp313.g2.smartgrocery.models.Notification;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class NotificationActivity extends Activity {
	private Notification notification;
	
	private NotificationDataSource dsNotifications;
	//private UI fields
	private TextView tvSubject, tvFrom, tvDate, tvText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		//initializing components
		InitializeComponents();
	}

	private void InitializeComponents() {
		//initializing data source
		dsNotifications = new NotificationDataSource(getApplicationContext());
		//getting notification id
		int id = getIntent().getIntExtra(NotificationDataSource.COL_ID, 0);
		//getting notification
		notification = dsNotifications.GetNotification(id);
		//initializing UI components
		tvSubject = (TextView) findViewById(R.id.tvSubject);
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvText = (TextView) findViewById(R.id.tvText);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//setting up action bar
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		bar.setTitle(notification.getSubject());
		bar.setIcon(R.drawable.ic_menu_notifications);
		
		//setting text for UI components
		if(notification!=null){
			tvSubject.setText(notification.getSubject());
			tvDate.setText(notification.getDate());
			tvFrom.setText(notification.getFrom());
			tvText.setText(notification.getText());
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
