package comp313.g2.smartgrocery.adapters;

import java.util.HashMap;
import comp313.g2.smartgrocery.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NutritionDetailsAdapter extends BaseAdapter {
	private Object[] nutritionName, nutritionValue;
	private Context context;

	public NutritionDetailsAdapter(Context context,
			HashMap<String, String> details) {
		nutritionName = details.keySet().toArray();
		nutritionValue = details.values().toArray();
		this.context = context;
	}

	@Override
	public int getCount() {
		return nutritionName.length;
	}

	@Override
	public String getItem(int arg0) {
		return nutritionName[arg0].toString();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		if (view == null) {
			view = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.list_item_nutrition_detail, null);
		}
		TextView nutrition = (TextView) view.findViewById(R.id.textView1);
		nutrition.setText(nutritionName[pos].toString());

		TextView value = (TextView) view.findViewById(R.id.textView2);
		value.setText(nutritionValue[pos].toString());

		return view;
	}

}
