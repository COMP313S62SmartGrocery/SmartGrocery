package comp313.g2.smartgrocery;

import java.util.HashMap;

import comp313.g2.smartgrocery.adapters.NutritionDetailsAdapter;
import comp313.g2.smartgrocery.helpers.NutritionServiceHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NutritionDetailsActivity extends Activity {
	private String title = "Nutrition Details";
	private  String itemName, itemId;

	private LinearLayout llEmptyView;
	private TextView tvLoading;
	private ProgressBar pbLoading;
	private ListView lvSearchResult;
	
	//hash-map to store search result
	private HashMap<String, String> searchResult;
	
	private NutritionServiceHelper serviceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_nutrition_details);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// getting details from intent
		itemName = getIntent().getStringExtra("NAME");
		itemId = getIntent().getStringExtra("ID");

		title = itemName;

		setTitle(title);

		// loading ui components
		InitializeComponents();

		// loading nutrition details
		FetchNutritionInfo();
	}

	private void FetchNutritionInfo() {
		// using thread
		final String id = itemId;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				searchResult = serviceHelper.GetNutritionDetails(id);
				
				//performing post run activities
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {

						if(searchResult.size()>0){
							NutritionDetailsAdapter adapter = new NutritionDetailsAdapter(NutritionDetailsActivity.this, searchResult);
							lvSearchResult.setAdapter(adapter);
						}else{
							pbLoading.setVisibility(View.GONE);
							tvLoading.setText("Unable to fetch nutrition details!");
							Toast.makeText(NutritionDetailsActivity.this, "Unable to fetch details!", Toast.LENGTH_SHORT).show();
						}						
					}
				});
			}
		}).start();
	}

	private void InitializeComponents() {
		llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
		pbLoading = (ProgressBar) findViewById(R.id.progressBar1);
		tvLoading = (TextView) findViewById(R.id.tvLoading);

		lvSearchResult = (ListView) findViewById(R.id.lvSearchResult);
		lvSearchResult.setEmptyView(llEmptyView);
		
		serviceHelper = new NutritionServiceHelper();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTitle(title);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(android.R.drawable.ic_menu_info_details);
	}
}
