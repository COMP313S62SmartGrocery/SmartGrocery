package comp313.g2.smartgrocery.adapters;

import java.util.ArrayList;

import comp313.g2.smartgrocery.R;
import comp313.g2.smartgrocery.models.List;
import comp313.g2.smartgrocery.models.Notification;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotificationsAdapter extends BaseAdapter {
	private ArrayList<Notification> list;
	private Context context;
	public NotificationsAdapter(Context context, ArrayList<Notification> list) {
		super();
		
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Notification getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).Id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView!=null){
			return convertView;
		}else{
			View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_notifications, null);
			
			TextView tvFrom = (TextView) view.findViewById(R.id.text1);
			TextView tvSubject = (TextView) view.findViewById(R.id.text2);
			tvFrom.setText(list.get(position).From);
			tvSubject.setText(list.get(position).Subject);
			if(!list.get(position).isRead){
				tvFrom.setTypeface(tvFrom.getTypeface(), Typeface.BOLD);
				tvSubject.setTypeface(tvSubject.getTypeface(), Typeface.BOLD);
			}
			return view;
		}
	}
}
