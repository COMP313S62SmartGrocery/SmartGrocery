package comp313.g2.smartgrocery.models;

public class ListItem {
	private String _name;
	private String _category;
	private float _quantity;
	
	public ListItem(String name){
		//initializing variables
		_name = name;
	}

	/**
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param _name the _name to set
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * @return the _category
	 */
	public String getCategory() {
		return _category;
	}

	/**
	 * @param category the _category to set
	 */
	public void setCategory(String category) {
		this._category = category;
	}

	/**
	 * @return the _quantity
	 */
	public float getQuantity() {
		return _quantity;
	}

	/**
	 * @param _quantity the _quantity to set
	 */
	public void setQuantity(float _quantity) {
		this._quantity = _quantity;
	}
}
