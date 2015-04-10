package comp313.g2.smartgrocery.fragments;

import comp313.g2.smartgrocery.AboutActivity;
import comp313.g2.smartgrocery.HelpActivity;
import comp313.g2.smartgrocery.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SettingsFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener {
	private Switch switchNotificationSound, switchNotification;
	private Button btnAbout, btnHelp;

	private SharedPreferences prefs;

	public static final String KEY_NOTIFICATION = "NOTIFICATION_ENABLED";
	public static final String KEY_NOTIFICATION_SOUND = "NOTIFICATION_SOUND_ENABLED";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// initializing components
		InitializeComponenets();
	}

	@Override
	public void onResume() {
		super.onResume();
		// loading default values
		boolean enableNotification = prefs.getBoolean(KEY_NOTIFICATION, true);
		switchNotification.setChecked(enableNotification);

		boolean enableNotificationSound = prefs.getBoolean(
				KEY_NOTIFICATION_SOUND, true);

		switchNotificationSound.setChecked(enableNotificationSound);

		// enabling options menu
		setHasOptionsMenu(false);

		// getting action bar
		ActionBar bar = getActivity().getActionBar();

		// setting title
		bar.setTitle(getActivity().getString(R.string.title_settings));
		// setting icon
		bar.setIcon(android.R.drawable.ic_menu_manage);
	}

	private void InitializeComponenets() {
		try {
			prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
					.getApplicationContext());

			switchNotification = (Switch) getActivity().findViewById(
					R.id.switchNotification);

			switchNotificationSound = (Switch) getActivity().findViewById(
					R.id.switchNotificationSound);

			switchNotification.setOnCheckedChangeListener(this);
			switchNotificationSound.setOnCheckedChangeListener(this);

			btnHelp = (Button) getActivity().findViewById(R.id.btnHelp);
			btnHelp.setOnClickListener(this);

			btnAbout = (Button) getActivity().findViewById(R.id.btnAbout);
			btnAbout.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor = prefs.edit();

		if (switchNotificationSound.isChecked()) {
			editor.putBoolean(KEY_NOTIFICATION_SOUND, true);
		} else {
			editor.putBoolean(KEY_NOTIFICATION_SOUND, false);
		}

		if (switchNotification.isChecked()) {
			editor.putBoolean(KEY_NOTIFICATION, true);
		} else {
			editor.putBoolean(KEY_NOTIFICATION, false);
		}

		editor.commit();
	}

	@Override
	public void onClick(View v) {
		if (v == btnAbout) {
			startActivity(new Intent(this.getActivity(), AboutActivity.class));
		} else if (v == btnHelp) {
			startActivity(new Intent(this.getActivity(), HelpActivity.class));
		}

	}
}