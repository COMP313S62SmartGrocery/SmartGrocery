package comp313.g2.smartgrocery.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.models.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListItemsAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ListItem> items;
	
	public ListItemsAdapter(Context context, ArrayList<ListItem> listItems){
		this.context = context;
		items = listItems;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
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
			
			view = inflater.inflate(R.layout.list_item_notifications, null);
		}
		
		return view;
	}
}
