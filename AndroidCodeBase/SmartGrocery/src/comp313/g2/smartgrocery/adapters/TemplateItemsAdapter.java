package comp313.g2.smartgrocery.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.models.ListItem;
import comp313.g2.smartgrocery.models.TemplateItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TemplateItemsAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<TemplateItem> items;
	
	public TemplateItemsAdapter(Context context, ArrayList<TemplateItem> listItems){
		this.context = context;
		items = listItems;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public TemplateItem getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		if(view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = inflater.inflate(R.layout.list_item, null);
		}
		
		TextView tvName= (TextView)view.findViewById(R.id.tvName);
		TextView tvQuantity = (TextView)view.findViewById(R.id.tvQuantity);
		
		TemplateItem item = items.get(pos);
		tvName.setText(item.Name);
		tvQuantity.setText(String.valueOf(item.Quantity)+item.Unit);
		
		return view;
	}
}
