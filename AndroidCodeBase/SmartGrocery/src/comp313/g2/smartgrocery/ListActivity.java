package comp313.g2.smartgrocery;

import java.util.ArrayList;

import comp313.g2.smartgrocery.adapters.ListItemsAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.ListItem;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends Activity implements OnItemClickListener {
	private SharedPreferences prefs;
	private List list;
	private User user;
	
	private ImageView emptyView;
	private ListView lvListItems;
	
	private ArrayList<ListItem> listItems;
	
	private ServiceHelper helper;
	
	private int selectedItemPosition = -1;
	
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
		
		helper = new ServiceHelper();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//updating title of the activity
		setTitle(list.Name);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(R.drawable.launcher);
		
		//loading list data
		fetchListData();
	}

	private void fetchListData() {
		if(GeneralHelpers.IsConnected(getApplicationContext())){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						final ArrayList<ListItem> items = helper.GetListItems(user, list.Id);
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								listItems = items;
								
								if(listItems!=null){
									ListItemsAdapter adapter =new ListItemsAdapter(getApplicationContext(), listItems);
									lvListItems.setAdapter(adapter);
									lvListItems.setOnItemClickListener(ListActivity.this);
								}								
							}
						});
					}catch (final Exception e) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();								
							}
						});
					}
					
				}
			}).start();
		}else{
			Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		selectedItemPosition = ((AdapterContextMenuInfo)menuInfo).position;
		
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu_list_items, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemDelete:
			if(selectedItemPosition!=-1){
				if(GeneralHelpers.IsConnected(getApplicationContext())){
					final ListItem listItem = listItems.get(selectedItemPosition);
					listItems.remove(selectedItemPosition);
					((BaseAdapter)lvListItems.getAdapter()).notifyDataSetChanged();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							try{
								final boolean success = helper.DeleteListItem(user, listItem.Id);
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										if(!success){
											listItems.add(listItem);
											((BaseAdapter)lvListItems.getAdapter()).notifyDataSetChanged();
										}								
									}
								});
							}catch (final Exception e) {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										listItems.add(listItem);
										((BaseAdapter)lvListItems.getAdapter()).notifyDataSetChanged();
										Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();								
									}
								});
							}
							
						}
					}).start();
				}else{
					Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
