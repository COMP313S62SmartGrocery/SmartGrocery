package comp313.g2.smartgrocery.recievers;

import comp313.g2.smartgrocery.services.ReminderService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		if(arg1.getAction()==Intent.ACTION_BOOT_COMPLETED)
		{
			Intent iWeMeetService = new Intent(context, ReminderService.class);
			context.startService(iWeMeetService);
		}
	}

}
