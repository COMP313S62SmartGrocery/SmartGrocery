package comp313.g2.smartgrocery.fragments;

import java.util.ArrayList;
import java.util.List;

import comp313.g2.smartgrocery.NotificationActivity;
import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.adapters.NotificationsAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.Notification;
import comp313.g2.smartgrocery.models.User;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationsFragment extends Fragment implements
		OnItemClickListener {
	private ArrayList<Notification> list;
	private ListView lvLists;
	private Context context;
	private User user = null;

	private SharedPreferences prefs;

	// field to set position of selected item
	private int selectedItemPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_notifications, null);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// initializing components
		InitializeComponenets();
	}

	private void InitializeComponenets() {
		// getting context
		context = getActivity().getApplicationContext();

		// getting UI components
		lvLists = (ListView) getActivity().findViewById(R.id.lvLists);
		// setting empty view for the list
		lvLists.setEmptyView(getActivity().findViewById(R.id.tvEmptyList));

		// setting item click listener
		lvLists.setOnItemClickListener(this);

		// setting item long click listener
		lvLists.setOnCreateContextMenuListener(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getApplicationContext());
		

		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
	}

	@Override
	public void onResume() {
		super.onResume();

		// enabling options menu
		setHasOptionsMenu(false);

		// getting action bar
		ActionBar bar = getActivity().getActionBar();

		// setting title
		bar.setTitle(getActivity().getString(R.string.title_notifications));
		// setting icon
		bar.setIcon(R.drawable.ic_menu_notifications);

		// setting data
		fetchData();
	}

	private void fetchData() {
		if (GeneralHelpers.IsConnected(getActivity())) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						ServiceHelper helper = new ServiceHelper();

						User user = new User();
						user.Username = prefs.getString(
								PreferenceHelper.KEY_USERNAME, "");
						user.SESS_KEY = prefs.getString(
								PreferenceHelper.KEY_SESS, "");

						final ArrayList<Notification> notifications = helper
								.GetNotifications(user);

						// performing post run operations
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								list = notifications; // fetching list from db

								// setting list adapter
								lvLists.setAdapter(new NotificationsAdapter(
										context, list));

							}
						});
					} catch (Exception e) {
						Log.e("Smart Grocery", e.getMessage());
					}
				}
			}).start();
		} else {
			Toast.makeText(getActivity(), "Unable to connect!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Notification notification = list.get(pos);
		
		Intent i = new Intent(context, NotificationActivity.class);
		i.putExtra("comp313.g2.smartgrocery.models.Notification", notification);
		startActivity(i);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// setting selected item position
		selectedItemPosition = ((AdapterContextMenuInfo) menuInfo).position;

		// showing menu
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_menu_notifications, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (selectedItemPosition != -1) {
			switch (item.getItemId()) {
			case R.id.itemDelete:
				// removing item from the database
				final int id = list.get(selectedItemPosition).Id;

				new Thread(new Runnable() {

					@Override
					public void run() {
						ServiceHelper helper = new ServiceHelper();
						
						try {
							final boolean deleted = helper.DeleteNotification(user, id);
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (deleted) {
										// removing item from the local array
										list.remove(selectedItemPosition);
										// notifying dataset change
										((BaseAdapter) lvLists.getAdapter())
												.notifyDataSetChanged();
										// resetting selected index
										selectedItemPosition = -1;
									} else {
										Toast.makeText(context,
												"Unable to connect!",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
						} catch (Exception e) {
							Log.e("Smart Grocery", e.getMessage());
						}
					}
				}).start();

				break;

			default:
				break;
			}
		}
		return super.onContextItemSelected(item);
	}
}
