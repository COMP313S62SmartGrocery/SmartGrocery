package comp313.g2.smartgrocery;

import java.util.ArrayList;

import comp313.g2.smartgrocery.adapters.TemplateAdapter;
import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.Template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TemplateActivity extends Activity implements OnItemClickListener {
	private LinearLayout emptyView;
	private ListView lvList;
	
	private ArrayList<Template> templates;
	
	private ServiceHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templates);
		
		InitializeComponents();
	}

	private void InitializeComponents() {
		emptyView = (LinearLayout) findViewById(R.id.llEmptyView);
		lvList = (ListView) findViewById(R.id.lvLists);
		
		lvList.setEmptyView(emptyView);
		
		helper = new ServiceHelper();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setTitle("Templates");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		fetchData();
	}

	private void fetchData() {
		if(GeneralHelpers.IsConnected(getApplicationContext())){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						final ArrayList<Template> templateList = helper.GetTemplates();
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {

								if(templateList!=null){
									templates = templateList;
									
									TemplateAdapter adapter = new TemplateAdapter(getApplicationContext(), templates);
									lvList.setAdapter(adapter);
									lvList.setOnItemClickListener(TemplateActivity.this);
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
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		Template template = templates.get(pos);
		
		Intent i = new Intent(TemplateActivity.this, TemplateItemsActivity.class);
		i.putExtra("NAME", template.Name);
		i.putExtra("ID", template.Id);
		startActivity(i);
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
}
