package comp313.g2.smartgrocery;

import java.util.ArrayList;

import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.ItemHistory;
import comp313.g2.smartgrocery.models.User;

import devsd.android.controls.chartcontrols.LineChart;
import devsd.android.controls.chartcontrols.models.ChartPoint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ReportActivity extends Activity {
	private LineChart chart;
	
	private SharedPreferences prefs;
	private User user;
	
	private String item="", month="", year="";
	ArrayList<ItemHistory> history;
	
	private ServiceHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
		
		InitializeComponents();
	}

	private void InitializeComponents() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
		
		item = getIntent().getStringExtra("ITEM");
		year = getIntent().getStringExtra("YEAR");
		month =String.valueOf(getIntent().getIntExtra("MONTH",0));
		
		helper = new ServiceHelper();
		
		//initializing ui components
		chart = (LineChart) findViewById(R.id.lcChart);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setTitle(item);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(android.R.drawable.ic_menu_recent_history);
		
		//fetch data
		fetchData();
	}

	private void fetchData() {
		if(GeneralHelpers.IsConnected(getApplicationContext())){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						history = helper.getItemHistory(user, item, month, year);
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(history!=null){
									for(ItemHistory item:history){
										float xValue =Float.parseFloat(item.Date.split("-")[0]);
										chart.addPoint(new ChartPoint(xValue, item.Quantity));

										chart.setYAxisLabel("   Items in "+history.get(0).Unit);
									}
									chart.setXAxisLabel("Days of Month");
									chart.invalidate();
								}
							}
						});
					}catch (final Exception e) {
						Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}).start();
		}else{
			Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
}
