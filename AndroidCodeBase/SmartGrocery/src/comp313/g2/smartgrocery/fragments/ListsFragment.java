package comp313.g2.smartgrocery.fragments;

import java.util.ArrayList;
import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.TemplateActivity;
import comp313.g2.smartgrocery.adapters.ListsAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.User;
import comp313.g2.smartgrocery.widgets.ColorPicker;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
	private Context context;
	private Dialog dialogAddList, dialogRenameList, dialogDuplicateList;

	// field to set position of selected item
	private int selectedItemPosition = -1;

	// dialog ui controls
	private EditText etListName, etRenameListName, etDuplicateListName;
	private ColorPicker colorPicker;
	private Button btnAdd, btnCancel;
	private Button btnRename, btnRenameCancel;
	private Button btnDuplicate, btnDuplicateCancel;

	private SharedPreferences prefs;
	private User user;
	private ServiceHelper helper = new ServiceHelper();

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

		// getting UI components
		lvLists = (ListView) getActivity().findViewById(R.id.lvLists);
		// setting empty view for the list
		lvLists.setEmptyView(getActivity().findViewById(R.id.ivEmptyList));

		// setting item click listener
		lvLists.setOnItemClickListener(this);

		// setting item long click listener
		lvLists.setOnCreateContextMenuListener(this);

		// initializing add list dialog
		dialogAddList = new Dialog(getActivity());
		dialogAddList.setTitle(getActivity()
				.getString(R.string.dialog_add_list));
		dialogAddList.setCancelable(false);
		dialogAddList.setContentView(R.layout.dialog_add_list);

		btnAdd = (Button) dialogAddList.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		btnCancel = (Button) dialogAddList.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		// initializing rename list dialog
		dialogRenameList = new Dialog(getActivity());
		dialogRenameList.setTitle(getActivity().getString(
				R.string.dialog_rename_list));
		dialogRenameList.setCancelable(false);
		dialogRenameList.setContentView(R.layout.dialog_rename_list);

		etRenameListName = (EditText) dialogRenameList
				.findViewById(R.id.etListName);
		btnRename = (Button) dialogRenameList.findViewById(R.id.btnRename);
		btnRename.setOnClickListener(this);

		btnRenameCancel = (Button) dialogRenameList
				.findViewById(R.id.btnCancel);
		btnRenameCancel.setOnClickListener(this);

		// initializing duplicate list dialog
		dialogDuplicateList = new Dialog(getActivity());
		dialogDuplicateList.setTitle(getActivity().getString(
				R.string.dialog_duplicate_list));
		dialogDuplicateList.setCancelable(false);
		dialogDuplicateList.setContentView(R.layout.dialog_duplicate_list);

		etDuplicateListName = (EditText) dialogDuplicateList
				.findViewById(R.id.etListName);
		btnDuplicate = (Button) dialogDuplicateList
				.findViewById(R.id.btnRename);
		btnDuplicate.setOnClickListener(this);

		btnDuplicateCancel = (Button) dialogDuplicateList
				.findViewById(R.id.btnCancel);
		btnDuplicateCancel.setOnClickListener(this);

		// initializing dialog ui controls
		etListName = (EditText) dialogAddList.findViewById(R.id.etListName);
		colorPicker = (ColorPicker) dialogAddList
				.findViewById(R.id.cpListColor);

		// initializing shared preference
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getApplicationContext());
		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");
	}

	// listens to dialog click events
	@Override
	public void onClick(View view) {
		if (view == btnAdd) {
			// validating input
			String listName = etListName.getText().toString().trim();// sanitizing
																		// input
			if (listName.equals("")) {
				etListName.setError("Name is required!");
			} else if (isListExists(listName)) {
				etListName.setError("List already exists!");
			} else {
				if (GeneralHelpers.IsConnected(context)) {
					// code to add List
					final List listItem = new List();
					listItem.Name = listName;
					listItem.Color = colorPicker.getSelectedColorString();
					listItem.LastModified = GeneralHelpers.GetCurrentDateTime();

					// performing pre add operations
					btnAdd.setEnabled(false);
					btnCancel.setEnabled(false);

					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								final long id = helper.AddList(listItem, user);

								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (id != -1) {
											listItem.Id = id;
											// adding item to local list
											list.add(listItem);

											// redrawing list on UI
											((BaseAdapter) lvLists.getAdapter())
													.notifyDataSetChanged();
										} else {
											Toast.makeText(context,
													"Unable to add list!",
													Toast.LENGTH_LONG).show();
										}

										// resetting text
										etListName.setText("");
										dialogAddList.dismiss();
										btnAdd.setEnabled(true);
										btnCancel.setEnabled(true);
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
		} else if (view == btnCancel) {
			dialogAddList.dismiss();
		} else if (view == btnRenameCancel) {
			dialogRenameList.dismiss();
		} else if (view == btnRename) {
			// validating
			final String newName = etRenameListName.getText().toString().trim();
			if (newName.length() == 0) {
				etRenameListName.setError("Enter name!");
			} else if (isListExists(newName)) {
				etRenameListName.setError("Name already exists!");
			} else {
				if (GeneralHelpers.IsConnected(context)) {
					btnRename.setEnabled(false);
					btnRenameCancel.setEnabled(false);

					final List item = list.get(selectedItemPosition);
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								final boolean success = helper.RenameList(user,
										item.Id, newName);

								// performing post rename operations
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (success) {
											// removing item from the local
											// array
											item.Name = newName;

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

										// resetting text
										etRenameListName.setText("");
										dialogRenameList.dismiss();
										btnRename.setEnabled(true);
										btnRenameCancel.setEnabled(true);
									}
								});
							} catch (final Exception e) {
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(context, e.getMessage(),
											Toast.LENGTH_SHORT).show();	
									}
								});
							}

						}
					}).start();
				} else {
					Toast.makeText(context, "Unable to connect!",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else if (view == btnDuplicateCancel) {
			dialogDuplicateList.dismiss();
		} else if (view == btnDuplicate) {
			// validating
			final String listName = etDuplicateListName.getText().toString().trim();
			if (listName.length() == 0) {
				etDuplicateListName.setError("Enter name!");
			} else if (isListExists(listName)) {
				etDuplicateListName.setError("Name already exists!");
			} else {
				if (GeneralHelpers.IsConnected(context)) {
					btnDuplicate.setEnabled(false);
					btnDuplicateCancel.setEnabled(false);
					
					final List item = list.get(selectedItemPosition);
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								final List success = helper.DuplicateList(user, item.Id, listName);
								
								// performing post rename operations
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if(success!=null){
										// removing item from the local array
										list.add(success);
										
										// notifying dataset change
										((BaseAdapter) lvLists.getAdapter())
												.notifyDataSetChanged();
										// resetting selected index
										selectedItemPosition = -1;
										}else{
											Toast.makeText(context, "Unable to connect!",
													Toast.LENGTH_SHORT).show();
										}
										
										// resetting text
										etDuplicateListName.setText("");
										dialogDuplicateList.dismiss();
										btnDuplicate.setEnabled(true);
										btnDuplicateCancel.setEnabled(true);
									}
								});
							} catch (final Exception e) {
								getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(context, e.getMessage(),
												Toast.LENGTH_SHORT).show();										
									}
								});
							}

						}
					}).start();
				} else {
					Toast.makeText(context, "Unable to connect!",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private boolean isListExists(String name) {
		for (List item : list) {
			if (item.Name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
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
		if (GeneralHelpers.IsConnected(context)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						final ArrayList<List> lists = helper.GetLists(user);

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (lists != null) {
									list = lists;

									// setting list adapter
									lvLists.setAdapter(new ListsAdapter(
											context, list));
								} else {
									Toast.makeText(getActivity(),
											"Unable to connect!",
											Toast.LENGTH_SHORT).show();
								}

							}
						});
					} catch (final Exception e) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getActivity(), e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						});
					}

				}
			}).start();
		} else {
			Toast.makeText(getActivity(), "Unable to connect!",
					Toast.LENGTH_SHORT).show();
		}
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
		case R.id.itemTemplates:
			Intent i =new Intent(context, TemplateActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		// making notification as read
		List listItem = list.get(pos);
		
		Intent i = new Intent(context, comp313.g2.smartgrocery.ListActivity.class);
		i.putExtra("comp313.g2.smartgrocery.models.List", listItem);
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
		inflater.inflate(R.menu.context_menu_lists, menu);
		

		List currentlist = list.get(selectedItemPosition);
		if(currentlist!=null){
			if(!currentlist.Username.startsWith(user.Username)){
				menu.removeItem(R.id.itemDuplicate);
				menu.removeItem(R.id.itemRename);
				menu.removeItem(R.id.itemShare);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (selectedItemPosition != -1) {
			switch (item.getItemId()) {
			case R.id.itemDelete:
				final List listItem = list.get(selectedItemPosition);

				new Thread(new Runnable() {

					public void run() {
						try {
							final boolean isDeleted;
							if(!listItem.Username.startsWith(user.Username)){
									isDeleted = true;//invoke un-share method 
							}else{
							
							isDeleted = helper.DeleteList(user,
									listItem.Id);
							}
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (isDeleted) {
										// removing item from the local array
										list.remove(selectedItemPosition);
										// notifying dataset change
										((BaseAdapter) lvLists.getAdapter())
												.notifyDataSetChanged();
										// resetting selected index
										selectedItemPosition = -1;
									} else {
										Toast.makeText(context,
												"Unable to delete list!",
												Toast.LENGTH_SHORT).show();
									}
								}
							});

						} catch (final Exception e) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(context, e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				}).start();
				break;
			case R.id.itemRename:
				dialogRenameList.show();
				break;
			case R.id.itemDuplicate:
				dialogDuplicateList.show();
				break;
			default:
				break;
			}
			Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
		}
		return super.onContextItemSelected(item);
	}
}
