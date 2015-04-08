package comp313.g2.smartgrocery.adapters;

import java.util.ArrayList;

import comp313.g2.smartgrocery.models.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListsAdapter extends BaseAdapter {
	private ArrayList<List> list;
	private Context context;
	public ListsAdapter(Context context, ArrayList<List> list) {
		super();
		
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public List getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView!=null){
			return convertView;
		}else{
			View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null);
			
			view.setBackgroundColor(list.get(position).getColor());
			TextView tvITem = (TextView) view.findViewById(android.R.id.text1);
			tvITem.setText(list.get(position).getName());
			
			return view;
		}
	}
}
