package comp313.g2.smartgrocery.fragments;

import java.util.HashMap;

import comp313.g2.smartgrocery.NutritionDetailsActivity;
import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.NutritionServiceHelper;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NutritionInfoFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private NutritionServiceHelper serviceHelper;

	// local ui elements
	private LinearLayout llEmptyView;
	private TextView tvSearchHint;
	private Button btnSearch;
	private ListView lvSearchResult;
	private EditText etSearch;

	// private hashmap to store previous search result
	private HashMap<String, String> searchResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_nutrition_info, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// initializing components
		InitializeComponents();
	}

	private void InitializeComponents() {
		etSearch = (EditText) getActivity().findViewById(R.id.etSearch);

		btnSearch = (Button) getActivity().findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		llEmptyView = (LinearLayout) getActivity().findViewById(
				R.id.llEmptyView);
		tvSearchHint = (TextView) getActivity().findViewById(R.id.tvSearchHint);

		lvSearchResult = (ListView) getActivity().findViewById(
				R.id.lvSearchResult);
		lvSearchResult.setEmptyView(llEmptyView);

		// instantiating service helper
		serviceHelper = new NutritionServiceHelper();
	}

	@Override
	public void onResume() {
		super.onResume();

		// enabling options menu
		setHasOptionsMenu(true);

		// getting action bar
		ActionBar bar = getActivity().getActionBar();

		// setting title
		bar.setTitle(getActivity().getString(R.string.title_nutrition_info));
		// setting icon
		bar.setIcon(android.R.drawable.ic_menu_info_details);
	}

	@Override
	public void onClick(View view) {
		if (view == btnSearch) {
			final String keyword = etSearch.getText().toString().trim();

			if (keyword.length() > 0) {
				// performing pre execution on view
				tvSearchHint.setText("Loading...");
				btnSearch.setEnabled(false);
				if (searchResult != null) {
					searchResult.clear();
					BaseAdapter adapter = ((BaseAdapter) lvSearchResult.getAdapter());
					if(adapter!=null){
						adapter.notifyDataSetChanged();
					}
				}

				if (GeneralHelpers.IsConnected(getActivity())) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							searchResult = serviceHelper.Search(keyword);

							// performing post execution on view
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									btnSearch.setEnabled(true);
									tvSearchHint
											.setText(R.string.str_search_hint);

									if (searchResult.size() > 0) {
										lvSearchResult
												.setAdapter(new ArrayAdapter<>(
														getActivity(),
														android.R.layout.simple_list_item_1,
														android.R.id.text1,
														searchResult.keySet()
																.toArray()));
										lvSearchResult
												.setOnItemClickListener(NutritionInfoFragment.this);
									} else {
										Toast.makeText(getActivity(),
												"No results found!",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}).start();
				} else {
					etSearch.setError("Enter keyword!");
				}
			} else {

				Toast.makeText(getActivity(),
						"Unable to connect!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {

		String key = ((TextView) view).getText().toString();
		String val = searchResult.get(key);

		Intent intent = new Intent(getActivity(),
				NutritionDetailsActivity.class);
		intent.putExtra("NAME", key);
		intent.putExtra("ID", val);
		startActivity(intent);

	}
}
