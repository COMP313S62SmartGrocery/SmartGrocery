package comp313.g2.smartgrocery.helpers;

import comp313.g2.smartgrocery.datasources.ListDataSource;
import comp313.g2.smartgrocery.datasources.NotificationDataSource;

import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GroceryDataHelper extends SQLiteOpenHelper {
	/* Constant literals */
	private static final String DB_NAME = "SmartGroceryDB";
	private static final int VERSION = 1;
	
	public GroceryDataHelper(Context context,CursorFactory factory) {
		super(context, DB_NAME, factory, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ListDataSource.CREATE_TABLE);
		db.execSQL(NotificationDataSource.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
