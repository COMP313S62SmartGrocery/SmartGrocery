package comp313.g2.smartgrocery;

import java.util.ArrayList;

import comp313.g2.smartgrocery.adapters.TemplateItemsAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.Template;
import comp313.g2.smartgrocery.models.TemplateItem;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TemplateItemsActivity extends Activity {
	private LinearLayout emptyView;
	private ListView lvList;

	private SharedPreferences prefs;
	private User user;
	private Template template;

	private ServiceHelper helper;

	private ArrayList<TemplateItem> templateItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_items);

		InitializeComponents();
	}

	private void InitializeComponents() {
		emptyView = (LinearLayout) findViewById(R.id.llEmptyView);
		lvList = (ListView) findViewById(R.id.lvLists);

		lvList.setEmptyView(emptyView);

		// initializing private objects
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		user = new User();
		user.Username = prefs.getString(PreferenceHelper.KEY_USERNAME, "");
		user.SESS_KEY = prefs.getString(PreferenceHelper.KEY_SESS, "");

		template = new Template();
		template.Id = getIntent().getLongExtra("ID", -1);
		template.Name = getIntent().getStringExtra("NAME");

		helper = new ServiceHelper();
	}

	@Override
	protected void onResume() {
		super.onResume();

		setTitle(template.Name);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		fetchData();
	}

	private void fetchData() {
		if (GeneralHelpers.IsConnected(getApplicationContext())) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						final ArrayList<TemplateItem> items = helper
								.GetTemplateItems(template.Id);

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								templateItems = items;
								if(templateItems!=null){
									TemplateItemsAdapter adapter = new TemplateItemsAdapter(getApplicationContext(), templateItems);
									lvList.setAdapter(adapter);
								}
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										e.getMessage(), Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				}
			}).start();
		} else {
			Toast.makeText(getApplicationContext(), "Unable to connect!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_template, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemAddToList:
			if (GeneralHelpers.IsConnected(getApplicationContext())) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							final String message = helper.AddTemplateToList(
									user, template.Id);

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (message != null) {
										Toast.makeText(getApplicationContext(),
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}
							});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}).start();
			} else {
				Toast.makeText(getApplicationContext(), "Unable to connect!",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
