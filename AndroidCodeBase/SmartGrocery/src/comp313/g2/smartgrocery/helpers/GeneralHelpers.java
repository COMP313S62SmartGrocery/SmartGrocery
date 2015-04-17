package comp313.g2.smartgrocery.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GeneralHelpers {
	private static final Calendar calender= Calendar.getInstance();
	
	public static boolean IsConnected(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
	}
	

	
	public static String GetCurrentDateTime() {
		String val = String.valueOf(calender.get(Calendar.DAY_OF_MONTH))+"-"+String.valueOf(calender.get(Calendar.MONTH)+1)+"-"+String.valueOf(calender.get(Calendar.YEAR));
		val+=" "+String.valueOf(calender.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calender.get(Calendar.MINUTE))+":"+String.valueOf(calender.get(Calendar.SECOND));
		return val;
	}
	
	public static String GetCurrentDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");//dd/MM/yyyy
	    Date now = new Date();
	    return sdfDate.format(now);
	}
}
