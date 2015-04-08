package devsd.android.controls.chartcontrols.models;

public class ChartPoint {
	//creating private fields
	private float xValue, yValue;
	private String lable;
	private float posX, posY;
	
	//constructor
	public ChartPoint(float xValue, float yValue){
		this.setXValue(xValue);
		this.setYValue(yValue);
		this.setLable(String.valueOf(xValue)+","+String.valueOf(yValue));
	}

	//getters and setters
	public float getXValue() {
		return xValue;
	}

	public void setXValue(float xValue) {
		this.xValue = xValue;
	}

	public float getYValue() {
		return yValue;
	}

	public void setYValue(float yValue) {
		this.yValue = yValue;
	}

	public String getLable() 
	{
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}
}