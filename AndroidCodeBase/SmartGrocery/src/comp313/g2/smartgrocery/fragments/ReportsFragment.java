package comp313.g2.smartgrocery.fragments;

import java.util.ArrayList;

import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.ReportActivity;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.User;
import devsd.android.controls.chartcontrols.*;
import devsd.android.controls.chartcontrols.models.ChartPoint;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ReportsFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	private Spinner spItems, spYears, spMonths;
	private Button btnGenerate;
	
	private SharedPreferences prefs;
	private User user;
	
	private ArrayList<String> items,months;
	
	private ServiceHelper helper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_reports, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//initializing components
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS,"");
		
		helper = new ServiceHelper();
		
		months = new ArrayList<String>();
		months.add("January");months.add("February");months.add("March");months.add("April");
		months.add("May");months.add("June");months.add("July");months.add("August");months.add("September");months.add("October");
		months.add("November");months.add("December");
		
		btnGenerate = (Button) getActivity().findViewById(R.id.btnGenerate);
		btnGenerate.setOnClickListener(this);
		
		spItems = (Spinner) getActivity().findViewById(R.id.spItems);
		spYears= (Spinner) getActivity().findViewById(R.id.spYears);
		spMonths = (Spinner) getActivity().findViewById(R.id.spMonths);
		spMonths.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, months));
		
		//fetching items
		fetchItems();
	}

	private void fetchItems() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					items = helper.getHistoryItems(user);
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(items!=null){
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
								spItems.setAdapter(adapter);
								spItems.setOnItemSelectedListener(ReportsFragment.this);
							}
						}
					});
				}catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();	
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onResume() {
		super.onResume();

		// enabling options menu
		setHasOptionsMenu(true);

		// getting action bar
		ActionBar bar = getActivity().getActionBar();

		// setting title
		bar.setTitle(getActivity().getString(R.string.title_reports));
		// setting icon
		bar.setIcon(android.R.drawable.ic_menu_recent_history);
	}

	@Override
	public void onClick(View arg0) {
		if(arg0==btnGenerate){
			try{
			final String item =spItems.getSelectedItem().toString();
			final String year =spYears.getSelectedItem().toString();
			final String month =spMonths.getSelectedItem().toString();
			
			if(item==null || item.length()==0){
				Toast.makeText(getActivity(), "Invalid Item!", Toast.LENGTH_SHORT).show();
			}
			else if(year==null || year.length()==0){
				Toast.makeText(getActivity(), "Invalid Year!", Toast.LENGTH_SHORT).show();
			}else if(month==null || month.length()==0){
				Toast.makeText(getActivity(), "Invalid Month!", Toast.LENGTH_SHORT).show();
			}else{
				Intent i = new Intent(getActivity().getApplicationContext(), ReportActivity.class);
				i.putExtra("ITEM", item);
				i.putExtra("YEAR", year);
				i.putExtra("MONTH", spMonths.getSelectedItemPosition()+1);
				startActivity(i);
			}
			}catch (Exception e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		fetchYears();
	}

	private void fetchYears() {
		final String item = spItems.getSelectedItem().toString();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					final ArrayList<String> years = helper.getHistoryYears(user, item);
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(items!=null){
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, years);
								spYears.setAdapter(adapter);
							}
						}
					});
				}catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();	
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
