package comp313.g2.smartgrocery.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ColorPicker extends View implements View.OnTouchListener {
	private ArrayList<ColorView> colors;
	private int selectedColor=Color.rgb(153,180,51);
	
	public ColorPicker(Context context) {
		super(context);
		this.setOnTouchListener(this);
		//initializing colors list
		InitializeColors();
	}
	public ColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(this);
		//initializing colors list
		InitializeColors();
	}

	public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setOnTouchListener(this);
		//initializing colors list
		InitializeColors();
	}
	
	private void InitializeColors() {
		colors = new ArrayList<ColorView>();
		
		colors.add(new ColorView(Color.rgb(153,180,51), true));
		colors.add(new ColorView(Color.rgb(0,163,0)));
		colors.add(new ColorView(Color.rgb(30,113,69)));
		colors.add(new ColorView(Color.rgb(255,0,151)));
		colors.add(new ColorView(Color.rgb(159,0,167)));
		colors.add(new ColorView(Color.rgb(126,56,120)));
		colors.add(new ColorView(Color.rgb(96,60,186)));
		colors.add(new ColorView(Color.rgb(29,29,29)));
		colors.add(new ColorView(Color.rgb(0,171,169)));
		colors.add(new ColorView(Color.rgb(45,137,239)));
		colors.add(new ColorView(Color.rgb(43,87,151)));
		colors.add(new ColorView(Color.rgb(255,196,13)));
		colors.add(new ColorView(Color.rgb(227,162,26)));
		colors.add(new ColorView(Color.rgb(218,83,44)));
		colors.add(new ColorView(Color.rgb(238,17,17)));
		colors.add(new ColorView(Color.rgb(185,29,71)));
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int radius = canvas.getWidth()/13;
		int x = radius;
		int y = radius;

		
		for(int i=0;i<2;i++){
			for(int j=0;j<6;j++){
				colors.get((i*6)+j).draw(canvas, x, y, radius-5);
				x+=2*radius;
			}
			x = radius;
			y+=2*radius;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		for(ColorView view : colors){
			if(view.setClicked(arg1.getX(), arg1.getY())){
				selectedColor = view.getColor();
			}
		}
		invalidate();
		return false;
	}
	
	public int getSelectedColor(){
		return selectedColor;
	}
	
	public String getSelectedColorString(){
		return String.format("#%02x%02x%02x",Color.red(selectedColor) , Color.green(selectedColor), Color.blue(selectedColor));
	}
}
