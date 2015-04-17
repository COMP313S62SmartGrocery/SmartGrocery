package comp313.g2.smartgrocery;

import java.util.ArrayList;
import java.util.Calendar;

import comp313.g2.smartgrocery.adapters.ListItemsAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.ListItem;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Reminders;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity implements OnItemClickListener, OnClickListener, android.content.DialogInterface.OnClickListener {
	private SharedPreferences prefs;
	private List list;
	private User user;
	private boolean isReadOnly = false;
	
	private ImageView emptyView;
	private ListView lvListItems;
	
	private ArrayList<ListItem> listItems;
	
	private ServiceHelper helper;
	
	private int selectedItemPosition = -1;
	
	private Dialog dialogAddItem, dialogUpdateItem;
	private AlertDialog dialogBought;
	
	private EditText etItemName, etUpdateItemName;
	private EditText etItemQuantity, etUpdateItemQuantity;
	private Spinner spUnit, spUpdateUnit;
	private TextView tvReminder, tvUpdateReminder;
	private Button btnAdd, btnUpdate, btnCancel, btnUpdateCancel;
	
	private static final Calendar calender= Calendar.getInstance();
	private static final int DATE_PICKER_DIALOG = 999;	
	
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
		
		if(list!=null){
			if(!list.Username.startsWith(user.Username)){
				isReadOnly = true;
			}
		}
		
		//initializing list components
		lvListItems = (ListView) findViewById(R.id.lvItems);
		emptyView = (ImageView) findViewById(R.id.ivEmptyList);
		lvListItems.setEmptyView(emptyView);
		
		helper = new ServiceHelper();
		
		//initializing dialogs
		dialogAddItem = new Dialog(this);
		dialogAddItem.setTitle("Add Item");
		dialogAddItem.setCancelable(false);
		dialogAddItem.setContentView(R.layout.dialog_add_list_item);
		
		dialogUpdateItem = new Dialog(this);
		dialogUpdateItem.setTitle("Update Item");
		dialogUpdateItem.setCancelable(false);
		dialogUpdateItem.setContentView(R.layout.dialog_add_list_item);
		
		etItemName = (EditText) dialogAddItem.findViewById(R.id.etName);
		etItemQuantity = (EditText) dialogAddItem.findViewById(R.id.etQuantity);
		spUnit = (Spinner) dialogAddItem.findViewById(R.id.spUnit);
		tvReminder = (TextView) dialogAddItem.findViewById(R.id.tvReminder);
		tvReminder.setOnClickListener(this);
		
		btnAdd = (Button) dialogAddItem.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnCancel = (Button) dialogAddItem.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
		etUpdateItemName = (EditText) dialogUpdateItem.findViewById(R.id.etName);
		etUpdateItemName.setEnabled(false);
		etUpdateItemQuantity = (EditText) dialogUpdateItem.findViewById(R.id.etQuantity);
		spUpdateUnit = (Spinner) dialogUpdateItem.findViewById(R.id.spUnit);
		spUpdateUnit.setEnabled(false);
		tvUpdateReminder = (TextView) dialogUpdateItem.findViewById(R.id.tvReminder);
		tvUpdateReminder.setOnClickListener(this);

		btnUpdate = (Button) dialogUpdateItem.findViewById(R.id.btnAdd);
		btnUpdate.setText("Update");
		btnUpdate.setOnClickListener(this);
		btnUpdateCancel = (Button) dialogUpdateItem.findViewById(R.id.btnCancel);
		btnUpdateCancel.setOnClickListener(this);
		

		dialogBought = new Builder(ListActivity.this).create();
		dialogBought.setTitle("Update Item?");
		dialogBought.setMessage("Did you bought this item?");
		dialogBought.setButton(AlertDialog.BUTTON_NEGATIVE, "No", this);
		dialogBought.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", this);
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
									lvListItems.setOnCreateContextMenuListener(ListActivity.this);
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
		if(!isReadOnly){
			selectedItemPosition = pos;
			
			//populating dialog elements
			ListItem item = listItems.get(pos);
			
			etUpdateItemName.setText(item.Name);
			etUpdateItemQuantity.setText(String.valueOf(item.Quantity));
			for(int i=0;i<spUpdateUnit.getCount();i++){
				if(spUpdateUnit.getItemAtPosition(i).equals(item.Unit)){
					spUpdateUnit.setSelection(i);
					break;
				}
			}
			if(item.Reminder!=null && item.Reminder.length()>0){
				tvUpdateReminder.setText(item.Reminder);
			}
			dialogUpdateItem.show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!isReadOnly){
			getMenuInflater().inflate(R.menu.menu_itemlist, menu);
		}
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		selectedItemPosition = ((AdapterContextMenuInfo)menuInfo).position;
		
		super.onCreateContextMenu(menu, v, menuInfo);
		if(!isReadOnly){
			getMenuInflater().inflate(R.menu.context_menu_list_items, menu);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
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
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.itemAdd:
			dialogAddItem.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		if(view == btnCancel){
			dialogAddItem.dismiss();
			
			etItemName.setText("");
			etItemName.setError(null);
			etItemQuantity.setText("");
			etItemQuantity.setError(null);
		}else if(view == btnUpdateCancel){
			dialogUpdateItem.dismiss();

			etUpdateItemName.setText("");
			etUpdateItemName.setError(null);
			etUpdateItemQuantity.setText("");
			etUpdateItemQuantity.setError(null);
		}
		else if(view == btnAdd){
			final String name = etItemName.getText().toString().trim();
			final String quantity = etItemQuantity.getText().toString().trim();
			final String unit = spUnit.getSelectedItem().toString().trim();
			String reminderText = tvReminder.getText().toString().trim();
			final String reminder;
			
			if(reminderText.startsWith("Click") && reminderText.length()>11){
				reminder ="";
			}else{
				if(IsValidFutureDate(reminderText)){
					reminder = reminderText;
				}else{
					return;
				}				
			}
			
			if(name==null || name.length()<=0){
				etItemName.setError("Name is required!");
			}else if(quantity==null || quantity.length()<=0){
				etItemQuantity.setError("Quantity id required!");
			}else{
				if(GeneralHelpers.IsConnected(getApplicationContext())){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try{
								final ListItem item = new ListItem();
								item.Name = name;
								item.Quantity =Float.parseFloat(quantity);
								item.Unit = unit;
								item.Reminder = reminder;
								item.ListId = list.Id;
								
								final long id = helper.AddListItem(item, user);
								
									runOnUiThread(new Runnable() {
									
									@Override
									public void run() {

										if(id !=-1){
											item.Id = id;
											
											listItems.add(item);
											((BaseAdapter) lvListItems.getAdapter()).notifyDataSetChanged();
										}else{
											Toast.makeText(getApplicationContext(),"Unable to add item!", Toast.LENGTH_SHORT).show();
										}
										
										etItemName.setText("");
										etItemName.setError(null);
										etItemQuantity.setText("");
										etItemQuantity.setError(null);
										dialogAddItem.dismiss();
									}
								});
							}catch (final Exception e) {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
										dialogAddItem.dismiss();
									}
								});
							}
						}
					}).start();
				}else{
					Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else if(view == btnUpdate){
			final String quantity = etUpdateItemQuantity.getText().toString().trim();
			String reminder = null;
			
			if(quantity==null || quantity.length()<=0){
				etItemQuantity.setError("Quantity is required!");	
			}
			
			if(tvUpdateReminder.getText().toString().startsWith("Click") && tvUpdateReminder.getText().length()>11){
				reminder="";
			}else{
				if(IsValidFutureDate(tvUpdateReminder.getText().toString())){
					reminder = tvUpdateReminder.getText().toString().trim();
				}
			}
			
			if(null!=reminder){
				dialogBought.show();
			}
		}else if(view == tvReminder || view ==tvUpdateReminder){
			showDialog(DATE_PICKER_DIALOG);
		}
	}
	

	private boolean IsValidFutureDate(String date) {
		String[] reminderData = date.split("-");
		int day = Integer.parseInt(reminderData[0]);
		int month =  Integer.parseInt(reminderData[1]);
		int year =  Integer.parseInt(reminderData[2]);
		
		if(year<calender.get(Calendar.YEAR)){
			Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_SHORT).show();
			return false;
		}else if(year==calender.get(Calendar.YEAR) && month < calender.get(Calendar.MONTH)+1){
			Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_SHORT).show();
			return false;
		}else if(year==calender.get(Calendar.YEAR) && month == calender.get(Calendar.MONTH)+1 &&  day < calender.get(Calendar.DATE)){
			Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_PICKER_DIALOG:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
				   		calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			// set selected date into Text View
			if(dialogAddItem.isShowing()){
			
				tvReminder.setText(new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1)
					   .append("-").append(selectedYear));
			}else{
				tvUpdateReminder.setText(new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1)
						   .append("-").append(selectedYear));
			}
		}
	};

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		if(arg0 == dialogBought){
			dialogBought.dismiss();
			
			final boolean bought;
			if(arg1 == AlertDialog.BUTTON_NEGATIVE){
				bought = false;
			}
			else{
				bought = true;
			}
			
			if(GeneralHelpers.IsConnected(getApplicationContext())){
				
				new Thread(new Runnable() {
					@Override
					public void run() {
					try{
						if(selectedItemPosition!=-1){
							final ListItem listItem = listItems.get(selectedItemPosition);
							listItem.Quantity =Float.parseFloat(etUpdateItemQuantity.getText().toString());
							String reminder;
							
							if(tvUpdateReminder.getText().toString().startsWith("Click") || tvUpdateReminder.getText().length()<7 || tvUpdateReminder.getText().length()>11){
								reminder="";
							}else{
								reminder = tvUpdateReminder.getText().toString();
							}
							listItem.Reminder = reminder;
							
							final boolean success = helper.UpdateListItem(user, listItem, bought);
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if(success){
										((BaseAdapter)lvListItems.getAdapter()).notifyDataSetChanged();
										
									}else{
										Toast.makeText(getApplicationContext(), "Unable to update!", Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}catch (final Exception e) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}finally{
							runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								btnUpdate.setEnabled(true);
								btnUpdateCancel.setEnabled(true);
								dialogUpdateItem.dismiss();
								selectedItemPosition = -1;
							}
						});
					}
					
					}
				}).start();
			}else{
				Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
