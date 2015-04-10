package comp313.g2.smartgrocery.fragments;

import comp313.g2.smartgrocery.R;
import devsd.android.controls.chartcontrols.*;
import devsd.android.controls.chartcontrols.models.ChartPoint;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReportsFragment extends Fragment {
	private LineChart chart;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_reports, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//initializing components
		chart = (LineChart) getActivity().findViewById(R.id.lcChart);
		
		for(int i=1;i<=7;i++){
			chart.addPoint(new ChartPoint(i, (float) (Math.random()*50f)));
		}
		chart.setXAxisLabel("Week Days");
		chart.setYAxisLabel(" Volume in Kgs");
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
}
