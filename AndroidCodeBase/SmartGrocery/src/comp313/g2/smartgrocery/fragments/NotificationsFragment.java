package comp313.g2.smartgrocery.fragments;

import java.util.ArrayList;

import comp313.g2.smartgrocery.NotificationActivity;
import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.adapters.NotificationsAdapter;
import comp313.g2.smartgrocery.datasources.NotificationDataSource;
import comp313.g2.smartgrocery.models.Notification;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class NotificationsFragment extends Fragment implements OnItemClickListener{
	private ArrayList<Notification> list;
	private ListView lvLists;
	private NotificationDataSource dsNotifications;
	private Context context;

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

		// getting list of grocery lists
		dsNotifications = new NotificationDataSource(context);

		// getting UI components
		lvLists = (ListView) getActivity().findViewById(R.id.lvLists);
		//setting empty view for the list
		lvLists.setEmptyView(getActivity().findViewById(R.id.tvEmptyList));

		// setting item click listener
		lvLists.setOnItemClickListener(this);

		// setting item long click listener
		lvLists.setOnCreateContextMenuListener(this);
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
		list = dsNotifications.getNotifications(); // fetching list from db

		// setting list adapter
		lvLists.setAdapter(new NotificationsAdapter(context, list));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//making notification as read
		Notification notification =list.get(arg2);
		if(notification.isRead()==false && dsNotifications.SetNotificationAsRead(notification)){
			notification.setRead(true);
			lvLists.invalidate();//redrawing list
		}
		Intent i = new Intent(context, NotificationActivity.class);
		i.putExtra(NotificationDataSource.COL_ID, list.get(arg2).getId());
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
				if (dsNotifications.RemoveNotification(list.get(selectedItemPosition))) {
					// removing item from the local array
					list.remove(selectedItemPosition);
					// notifying dataset change
					((BaseAdapter) lvLists.getAdapter()).notifyDataSetChanged();
					// resetting selected index
					selectedItemPosition = -1;
				}
				break;

			default:
				break;
			}
		}
		return super.onContextItemSelected(item);
	}
}
