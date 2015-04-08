package comp313.g2.smartgrocery.datasources;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import comp313.g2.smartgrocery.helpers.GroceryDataHelper;
import comp313.g2.smartgrocery.models.Notification;

public class NotificationDataSource {
	// constant literals
	public static final String TABLE_NAME = "NOTIFICATIONS";
	public static final String COL_ID = "ID";
	public static final String COL_FROM = "FROM";
	public static final String COL_SUBJECT = "SUBJECT";
	public static final String COL_TEXT = "TEXT";
	public static final String COL_DATE = "DATE";
	public static final String COL_READ = "IS_READ";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + "["
			+ COL_FROM + "] TEXT," + COL_SUBJECT + " TEXT," + "[" + COL_TEXT
			+ "]  TEXT," + "[" + COL_DATE + "] TEXT," + COL_READ + " TEXT"
			+ ")";

	// private variables
	private GroceryDataHelper _dbHelper;
	private SQLiteDatabase _db;

	public NotificationDataSource(Context context) {
		_dbHelper = new GroceryDataHelper(context, null);
		_db = _dbHelper.getWritableDatabase();
	}

	public void close() {
		_db.close();
	}

	// method to add data into table
	public boolean AddNotification(Notification notification) {

		// setting values for various columns
		ContentValues values = new ContentValues();
		values.put("[" + COL_FROM + "]", notification.getFrom());
		values.put(COL_SUBJECT, notification.getSubject());
		values.put("[" + COL_TEXT + "]", notification.getText());
		values.put("[" + COL_DATE + "]", notification.getDate());
		values.put(COL_READ, String.valueOf(notification.isRead()));

		boolean result = _db.insert(TABLE_NAME, "", values) > 0;

		return result;
	}

	// method to update list data
	public boolean SetNotificationAsRead(Notification notification) {
		// setting values for various columns
		ContentValues values = new ContentValues();
		values.put(COL_READ, "TRUE");

		return _db.update(TABLE_NAME, values,
				COL_ID + "=" + notification.getId(), null) > 0;
	}

	// method to read single notification from the table
	public Notification GetNotification(int id) {
		Cursor cursor = _db.query(TABLE_NAME, null, COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		Notification notification = null;
		if (cursor.moveToFirst()) {
			// creating notification item instance
			notification = new Notification(cursor.getInt(cursor
					.getColumnIndex(COL_ID)), cursor.getString(cursor
					.getColumnIndex(COL_FROM)), cursor.getString(cursor
					.getColumnIndex(COL_SUBJECT)), cursor.getString(cursor
					.getColumnIndex(COL_TEXT)), cursor.getString(cursor
					.getColumnIndex(COL_DATE)), Boolean.parseBoolean(cursor
					.getString(cursor.getColumnIndex(COL_READ))));
		}

		cursor.close();// closing cursor
		return notification; // returning list
	}

	// method to remove list from the table
	public boolean RemoveNotification(Notification notification) {
		return _db.delete(TABLE_NAME, COL_ID + "=?",
				new String[] { String.valueOf(notification.getId()) }) > 0;
	}

	public ArrayList<Notification> getNotifications() {
		ArrayList<Notification> notificationList = new ArrayList<Notification>();

		// reading data from db
		Cursor cursor = _db.query(TABLE_NAME, null, null, null, null, null,
				COL_ID + " desc");

		while (cursor.moveToNext()) {
			// creating list item instance
			Notification notification = new Notification(cursor.getInt(cursor
					.getColumnIndex(COL_ID)), cursor.getString(cursor
					.getColumnIndex(COL_FROM)), cursor.getString(cursor
					.getColumnIndex(COL_SUBJECT)), cursor.getString(cursor
					.getColumnIndex(COL_TEXT)), cursor.getString(cursor
					.getColumnIndex(COL_DATE)), Boolean.parseBoolean(cursor
					.getString(cursor.getColumnIndex(COL_READ))));

			// adding item to list
			notificationList.add(notification);
		}
		cursor.close();// closing cursor
		return notificationList; // returning list
	}
}
