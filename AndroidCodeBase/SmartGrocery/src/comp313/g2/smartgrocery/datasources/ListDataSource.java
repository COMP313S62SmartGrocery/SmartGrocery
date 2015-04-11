/*
package comp313.g2.smartgrocery.datasources;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import comp313.g2.smartgrocery.helpers.GroceryDataHelper;
import comp313.g2.smartgrocery.models.List;

public class ListDataSource {
	//constant literals
	public static final String TABLE_NAME = "LISTS";
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COLOR = "COLOR";
	public static final String COL_LAST_MODIFIED = "LAST_MODIFIED";
	
	public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
			+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+COL_NAME + " TEXT,"
			+COL_COLOR + "  TEXT,"
			+COL_LAST_MODIFIED + " TEXT"
			+")";
	
	//private variables
	private GroceryDataHelper _dbHelper;
	private SQLiteDatabase _db;
	
	public ListDataSource(Context context){
		_dbHelper = new GroceryDataHelper(context, null);
		_db = _dbHelper.getWritableDatabase();
	}
	
	public void close(){
		_db.close();
	}
	
	//method to add data into table
	public boolean AddList(List list){
		
		//setting values for various columns
		ContentValues values = new ContentValues();
		values.put(COL_NAME, list.getName());
		values.put(COL_COLOR, list.getColor());
		values.put(COL_LAST_MODIFIED, list.getLastModified());
		
		boolean result = _db.insert(TABLE_NAME, "", values) > 0;
		
		return result;
	}
	
	//method to update list data
	public boolean UpdateList(List list){
		//setting values for various columns
		ContentValues values = new ContentValues();
		values.put(COL_NAME, list.getName());
		values.put(COL_COLOR, list.getColor());
		values.put(COL_LAST_MODIFIED, list.getLastModified());
		
		return _db.update(TABLE_NAME, values, COL_ID+"="+list.getId(), null) > 0;
	}
	
	//method to remove list from the table
	public boolean RemoveList(List list){
		return _db.delete(TABLE_NAME, COL_NAME+"=?",new String[]{list.getName()} ) > 0;
	}

	public ArrayList<List> getLists() {
		ArrayList<List> list = new ArrayList<List>();
		
		//reading data from db
		Cursor cursor= _db.query(TABLE_NAME, null, null, null, null, null, COL_NAME);
		
		while(cursor.moveToNext()){
			//creating list item instance
			List listItem = new List(cursor.getInt(cursor.getColumnIndex(COL_ID)), 
					cursor.getString(cursor.getColumnIndex(COL_NAME)), 
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_COLOR))), 
					cursor.getString(cursor.getColumnIndex(COL_LAST_MODIFIED)));
			
			//adding item to list
			list.add(listItem);
		}
		cursor.close();//closing cursor
		return list; //returning list
	}
}
*/