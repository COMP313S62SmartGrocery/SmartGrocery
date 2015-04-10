package comp313.g2.smartgrocery.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class ColorView {
	private boolean isSelected = false;
	private Paint paint;
	float x,y, radius;
	private int color;
	
	public ColorView(int color){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		this.color = color;
	}
	
	public ColorView(int color, boolean selected) {
		isSelected = selected;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		this.color = color;
	}

	public void draw(Canvas canvas, float x, float y, float radius){
		if(isSelected){
			paint.setStyle(Style.FILL);
		}else{
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(5f);
		}
		
		this.x = x;
		this.y= y;
		this.radius = radius;
		
		canvas.drawCircle(x, y, radius, paint);
	}
	
	public boolean setClicked(float posX, float posY){
		if(x-radius<=posX && posX<=x+radius){
			if(y-radius<=posY && posY <= y+radius){
				isSelected = true;
				return true;
			}
		}
		isSelected = false;
		return false;
	}
	
	public int getColor(){
		return color;
	}
}
