package comp313.g2.smartgrocery.fragments;

import java.util.ArrayList;
import java.util.Date;

import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.adapters.ListsAdapter;
import comp313.g2.smartgrocery.datasources.ListDataSource;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.widgets.ColorPicker;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListsFragment extends Fragment implements OnItemClickListener,
		android.view.View.OnClickListener {
	private ArrayList<List> list;
	private ListView lvLists;
	private ListDataSource listData;
	private Context context;
	private Dialog dialogAddList;

	// field to set position of selected item
	private int selectedItemPosition = -1;

	// dialog ui controls
	private EditText etListName;
	private ColorPicker colorPicker;
	private Button btnAdd, btnCancel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_lists, null);
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
		listData = new ListDataSource(context);

		// getting UI components
		lvLists = (ListView) getActivity().findViewById(R.id.lvLists);
		//setting empty view for the list
		lvLists.setEmptyView(getActivity().findViewById(R.id.ivEmptyList));

		// setting item click listener
		lvLists.setOnItemClickListener(this);

		// setting item long click listener
		lvLists.setOnCreateContextMenuListener(this);

		// initializing add list dialog
		dialogAddList = new Dialog(getActivity());
		dialogAddList.setTitle(getActivity().getString(R.string.dialog_add_list));
		dialogAddList.setCancelable(false);
		dialogAddList.setContentView(R.layout.dialog_add_list);
		
		btnAdd = (Button)dialogAddList.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		
		btnCancel = (Button)dialogAddList.findViewById(R.id.btnCancel);;
		btnCancel.setOnClickListener(this);

		// initializing dialog ui controls
		etListName = (EditText) dialogAddList.findViewById(R.id.etListName);
		colorPicker = (ColorPicker) dialogAddList.findViewById(R.id.cpListColor);
	}

	// listens to dialog click events
	@Override
	public void onClick(View view) {
		if(view == btnAdd){
			// validating input
			String listName = etListName.getText().toString().trim();// sanitizing input
			if (listName.equals("")) {
				etListName.setError("Name is required!");
			} else {
				// code to add List
				String date = new Date().toString();
				List listItem = new List(-1, listName,
						colorPicker.getSelectedColor(), date);

				ListDataSource dsLists = new ListDataSource(context);
				if (dsLists.AddList(listItem)) {
					// adding item to local list
					list.add(listItem);

					// redrawing list on UI
					((BaseAdapter) lvLists.getAdapter())
							.notifyDataSetChanged();
				} else {
					Toast.makeText(context, "Unable to add list!",
							Toast.LENGTH_LONG).show();
				}
				
				//resetting text
				etListName.setText("");
				dialogAddList.dismiss();
			}
		}else if(view == btnCancel){
			dialogAddList.dismiss();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// enabling options menu
		setHasOptionsMenu(true);

		// getting action bar
		ActionBar bar = getActivity().getActionBar();

		// setting title
		bar.setTitle(getActivity().getString(R.string.title_lists));
		// setting icon
		bar.setIcon(android.R.drawable.ic_menu_save);

		// setting data
		fetchData();
	}

	private void fetchData() {
		list = listData.getLists(); // fetching list from db

		// setting list adapter
		lvLists.setAdapter(new ListsAdapter(context, list));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_lists, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemAdd:
			// code to show add item dialog
			dialogAddList.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(context, list.get(arg2).getName(), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// setting selected item position
		selectedItemPosition = ((AdapterContextMenuInfo) menuInfo).position;

		// showing menu
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_menu_lists, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (selectedItemPosition != -1) {
			switch (item.getItemId()) {
			case R.id.itemDelete:
				// removing item from the database
				if (listData.RemoveList(list.get(selectedItemPosition))) {
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
			Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
		}
		return super.onContextItemSelected(item);
	}
}
