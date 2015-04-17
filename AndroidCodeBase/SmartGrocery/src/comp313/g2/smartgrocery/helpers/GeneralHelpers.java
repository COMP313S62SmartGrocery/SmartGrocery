package comp313.g2.smartgrocery.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GeneralHelpers {
	private static final Calendar calender= Calendar.getInstance();

	private static final String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    private static final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(ePattern);
	
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



	public static String GetCurrentLocalDate() {
		String val = String.valueOf(calender.get(Calendar.DAY_OF_MONTH))+"-"+String.valueOf(calender.get(Calendar.MONTH)+1)+"-"+String.valueOf(calender.get(Calendar.YEAR));
		return val;
	}
	
	public static boolean IsValidEmailId(String emailId){
		Matcher matcher = pattern.matcher(emailId);
		return matcher.matches();
	}
}
