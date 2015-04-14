package comp313.g2.smartgrocery;

import java.util.ArrayList;

import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.ListItem;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends Activity {
	private SharedPreferences prefs;
	private List list;
	private User user;
	
	private ImageView emptyView;
	private ListView lvListItems;
	
	private ArrayList<ListItem> listItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		//initializing components
		InitializeComponents();
	}

	private void InitializeComponents() {
		//loading user
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
		
		//loading list
		Bundle bundle = getIntent().getExtras();
		list = bundle.getParcelable("comp313.g2.smartgrocery.models.List");
		
		//initializing list components
		lvListItems = (ListView) findViewById(R.id.lvItems);
		emptyView = (ImageView) findViewById(R.id.ivEmptyList);
		lvListItems.setEmptyView(emptyView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//updating title of the activity
		setTitle(list.Name);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(R.drawable.ic_launcher);
		
		//loading list data
		fetchListData();
	}

	private void fetchListData() {
		if(GeneralHelpers.IsConnected(getApplicationContext())){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
				}
			}).start();
		}else{
			Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
		}
	}
}
