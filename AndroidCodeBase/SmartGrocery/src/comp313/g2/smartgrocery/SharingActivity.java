package comp313.g2.smartgrocery;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.ls.LSInput;

import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SharingActivity extends Activity implements OnClickListener, android.view.View.OnClickListener, OnItemClickListener {
	private User user;
	private SharedPreferences prefs;
	
	private List list;
	private ServiceHelper helper;
	
	private ListView lvLists;
	private ImageView emptyView;
	
	private ArrayList<String> shareList = new ArrayList<String>();
	private int selectedItemPosition = -1;
	
	private AlertDialog dialogUnshare;
	
	private Dialog dialogShare;
	private EditText etEmail;
	private Button btnShare, btnCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharing);
		
		InitializeComponents();
	}

	private void InitializeComponents() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
		
		Bundle bundle = getIntent().getExtras();
		list = (List) bundle.get("comp313.g2.smartgrocery.models.List");
		
		helper = new ServiceHelper();
		
		emptyView = (ImageView) findViewById(R.id.ivEmptyList);
		lvLists = (ListView) findViewById(R.id.lvLists);
		
		lvLists.setEmptyView(emptyView);
		
		dialogUnshare = new AlertDialog.Builder(SharingActivity.this).create();
		dialogUnshare.setTitle("Confirm Unshare?");
		dialogUnshare.setMessage("Are you sure you want to unshare list?");
		dialogUnshare.setIcon(android.R.drawable.ic_menu_share);
		dialogUnshare.setButton(AlertDialog.BUTTON_NEGATIVE, "No", this);
		dialogUnshare.setButton(AlertDialog.BUTTON_POSITIVE, "Unshare", this);
		
		//loading share list
		if(list!=null){
			shareList.clear();
			String[] items = list.Username.split(",");
			for(int i=1;i<items.length;i++){ //skipping owner
				shareList.add(items[i]);
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SharingActivity.this, android.R.layout.simple_list_item_1 ,shareList);
			lvLists.setAdapter(adapter);
			lvLists.setOnItemClickListener(this);
		}
		
		dialogShare = new Dialog(SharingActivity.this);
		dialogShare.setContentView(R.layout.dialog_share);
		dialogShare.setCancelable(false);
		dialogShare.setTitle("Share List");
		
		etEmail = (EditText) dialogShare.findViewById(R.id.etEmailId);
		btnShare = (Button) dialogShare.findViewById(R.id.btnShare);
		btnCancel = (Button) dialogShare.findViewById(R.id.btnCancel);
		btnShare.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	


	@Override
	public void onClick(View view) {
		if(view == btnShare){
			final String emailId = etEmail.getText().toString().trim();
			
			for(String email : shareList){
				if(email.equalsIgnoreCase(emailId)){
					etEmail.setError("Already Shared!");
					return;
				}
			}
			
			if(!GeneralHelpers.IsValidEmailId(emailId)){
				etEmail.setError("Invalid Email Id!");
			}else{
				if(GeneralHelpers.IsConnected(getApplicationContext())){
					btnCancel.setEnabled(false);
					btnShare.setEnabled(false);
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try{
								final boolean success = helper.ShareList(user, list.Id, emailId);
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										if(success){
											shareList.add(emailId);
												((BaseAdapter) lvLists.getAdapter()).notifyDataSetChanged();
										}
										else{
												Toast.makeText(SharingActivity.this, "Unable to share list!", Toast.LENGTH_SHORT).show();
										}
									}
								});
								
							}catch (final Exception e) {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(SharingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});
							}finally{
									runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										etEmail.setText("");
										etEmail.setError(null);
										btnShare.setEnabled(true);
										btnCancel.setEnabled(true);
										dialogShare.dismiss();
									}
								});
							}
							
						}
					}).start();
				}else{
					Toast.makeText(this, "Unable to connect!", Toast.LENGTH_SHORT).show();
				}
			}
			
		}else if(view == btnCancel){
			dialogShare.dismiss();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(dialog == dialogUnshare && selectedItemPosition!=-1){
			switch (which) {
			case AlertDialog.BUTTON_NEGATIVE:
				dialogShare.dismiss();
				break;
			case AlertDialog.BUTTON_POSITIVE:
				final String emailId = shareList.get(selectedItemPosition);

				shareList.remove(selectedItemPosition);
				((BaseAdapter)lvLists.getAdapter()).notifyDataSetChanged();
				
				if(GeneralHelpers.IsConnected(getApplicationContext())){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try{
								final boolean success = helper.UnshareList(user, list.Id, emailId);
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										if(!success){
											shareList.add(emailId);
											((BaseAdapter)lvLists.getAdapter()).notifyDataSetChanged();
											Toast.makeText(SharingActivity.this, "Unable to unshare!", Toast.LENGTH_SHORT).show();
										}
									}
								});
							}catch (final Exception e) {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(SharingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});
							}finally{
								selectedItemPosition = -1;
							}
						}
					}).start();
				}else{
					Toast.makeText(this, "Unable to connect!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(list!=null){
			getActionBar().setTitle(list.Name);
		}
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(android.R.drawable.ic_menu_share);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_sharing, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.itemAdd:
			dialogShare.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		selectedItemPosition = pos;
		dialogUnshare.show();
	}
}
