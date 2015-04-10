package comp313.g2.smartgrocery.models;

import java.sql.Date;
import java.util.ArrayList;

import android.graphics.Color;

public class List {
	private int _id;
	private String _name;
	private String _lastModified;
	private int _color;
	
	//array to hold items
	private ArrayList<ListItem> _listItems;
	
	public List(int id, String name, int color, String lastModified){
		//initializing variables
		_id= id;
		_name = name;
		_color = color;
		_lastModified = lastModified;
		_listItems = new ArrayList<ListItem>();
	}
	
	//method to add item to list
	public void AddItem(ListItem listItem){
		_listItems.add(listItem);
	}
	
	//method to remove single item from the list
	public void RemoveItem(int index){
		_listItems.remove(index);
	}
	
	//method to remove all items from the list
	public void RemoveAll(){
		_listItems.clear();
	}
	
	//getters and setters
	/**
	 * @return the id
	 */
	public int getId() {
		return _id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		_id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * @return the color
	 */
	public int getColor() {
		return _color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		_color = color;
	}
	
	/**
	 * @return the lastModified
	 */
	public String getLastModified() {
		return _lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		_lastModified = lastModified;
	}
	
	
}
