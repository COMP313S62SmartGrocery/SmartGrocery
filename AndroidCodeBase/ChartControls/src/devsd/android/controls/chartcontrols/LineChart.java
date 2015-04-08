package devsd.android.controls.chartcontrols;

import java.util.ArrayList;

import devsd.android.controls.chartcontrols.models.ChartPoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class LineChart extends View {
	//private fields
	private int xColor= Color.rgb(29, 29, 29); 
	private int yColor = Color.rgb(29, 29, 29);
	private int pointColor = Color.rgb(159, 0, 167);
	private int axisLabelColor = Color.rgb(29, 29, 29);
	private int pointLabelBgColor = Color.rgb(29, 29, 29);
	private int lineColor = Color.argb(100,126, 56, 120);
	private int pointLabelTextColor = Color.WHITE;
	
	private float POINT_RADIUS = 30;
	private float XMAX = 0, YMAX = 0;
	
	//list to hold points
	private ArrayList<ChartPoint> chartPoints = new ArrayList<ChartPoint>();
	
	//labels
	private String xAxisLabel="";
	private String yAxisLabel="";
	
	//path for y axis label
	private Path path = new Path();
	
	//fields to record last touch point
	private float touchX=-1, touchY=-1;
	private Paint paintPoint, paintAxis, paintAxisLabel, paintSelectedLabel, paintLine;
	
	//variable to store previous point
	private ChartPoint previousPoint = null;
	
	//constructors
	public LineChart(Context context) {
		super(context);
		
		InitializePaints();
	}
	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		InitializePaints();
	}
	public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		InitializePaints();
	}
	
	private void InitializePaints() {
		paintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintAxis.setStyle(Style.FILL_AND_STROKE);
		
		paintAxisLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintAxisLabel.setTypeface(Typeface.create("serif", Typeface.NORMAL));
		paintAxisLabel.setTextSize(16);
		paintAxisLabel.setColor(axisLabelColor);
		
		paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintPoint.setTypeface(Typeface.create("Serif", Typeface.NORMAL));
		paintPoint.setStrokeWidth(3f);
		paintPoint.setColor(pointColor);
		
		paintSelectedLabel= new Paint(Paint.ANTI_ALIAS_FLAG);
		paintSelectedLabel.setStrokeCap(Paint.Cap.ROUND);
		paintSelectedLabel.setTextSize(16);
		paintSelectedLabel.setStyle(Style.FILL_AND_STROKE);
		
		paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLine.setStyle(Style.FILL_AND_STROKE);
		paintLine.setColor(lineColor);
		paintLine.setStrokeWidth(5f);
		paintLine.setStrokeJoin(Join.MITER);
	}
	//overriding default paint method
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//getting height and width of the canvas
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		//setting point radius
		POINT_RADIUS = 0.02f*width;
		
		//drawing x and y axis
		paintAxis.setColor(xColor);
		canvas.drawLine(40, height-40, width-10, height-40, paintAxis);//x axis
		paintAxis.setColor(yColor);
		canvas.drawLine(40, 10, 40, height-40, paintAxis);//y axis
		
		//drawing axis label
		canvas.drawText(xAxisLabel,(width - (xAxisLabel.length()*9))/2, height - 7, paintAxisLabel);//x axis label
		
		path.addRect(new RectF(2, (height-(yAxisLabel.length()*9))/2, 10, height), Direction.CW);
		canvas.drawTextOnPath(yAxisLabel, path, 2, 2, paintAxisLabel);//y axis label

		//drawing points
		for(ChartPoint point : chartPoints){
			float xPercent = point.getXValue()/XMAX;
			float yPercent = 1-(point.getYValue()/YMAX);
			float x =40+((width-50)*xPercent);
			float y = 10+((height-50)*yPercent);

			//setting point position
			point.setPosX(x);
			point.setPosY(y);
			
			//drawing line
			if(previousPoint!=null){
				canvas.drawLine(previousPoint.getPosX(), previousPoint.getPosY(), point.getPosX(), point.getPosY(), paintLine);
			}
			previousPoint = point;//setting current point as previous point
		}
		
		//setting previous point to null
		previousPoint = null;
		
		for(ChartPoint point : chartPoints){
			//setting paint style to stroke
			paintPoint.setStyle(Style.STROKE);
			
			if(point.getPosX()-POINT_RADIUS<=touchX && touchX <= point.getPosX()+POINT_RADIUS && point.getPosY()-POINT_RADIUS <= touchY && touchY <= point.getPosY()+POINT_RADIUS){
			
				paintPoint.setStyle(Style.FILL_AND_STROKE);
				//drawing point
				canvas.drawCircle(point.getPosX(), point.getPosY(), POINT_RADIUS+2, paintPoint);
				
				int labelWidth = point.getLable().length()*9;
				float labelXPos = point.getPosX()+POINT_RADIUS+10;
				//boundary checking
				if(labelXPos+labelWidth>width){
					labelXPos = point.getPosX()-(labelWidth+10);
				}
				
				float labelYPos = point.getPosY();

				paintSelectedLabel.setColor(pointLabelBgColor);
				paintSelectedLabel.setStrokeWidth(25f);
				//drawing background
				canvas.drawLine(labelXPos, labelYPos, labelXPos+labelWidth, labelYPos, paintSelectedLabel);
				
				paintSelectedLabel.setStrokeWidth(1f);
				paintSelectedLabel.setColor(pointLabelTextColor);
				
				canvas.drawText(point.getLable(), labelXPos+10, labelYPos+7, paintSelectedLabel);
			}else{
				canvas.drawCircle(point.getPosX(), point.getPosY(), POINT_RADIUS, paintPoint);
			}			
		}
		
	}
	
	//overriding default touch event
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();
		
		invalidate();
		
		return super.onTouchEvent(event);
	}
	
	//getters and setters
	public int getXColor() {
		return xColor;
	}
	public void setXColor(int xColor) {
		this.xColor = xColor;
	}
	public int getYColor() {
		return yColor;
	}
	public void setYColor(int yColor) {
		this.yColor = yColor;
	}
	public int getPointColor() {
		return pointColor;
	}
	public void setPointColor(int pointColor) {
		this.pointColor = pointColor;
	}
	public int getAxisLabelColor() {
		return axisLabelColor;
	}
	public void setAxisLabelColor(int axisLabelColor) {
		this.axisLabelColor = axisLabelColor;
	}
	public int getPointLabelBgColor() {
		return pointLabelBgColor;
	}
	public void setPointLabelBgColor(int pointLabelBgColor) {
		this.pointLabelBgColor = pointLabelBgColor;
	}
	public int getLineColor() {
		return lineColor;
	}
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}
	public int getPointLabelTextColor() {
		return pointLabelTextColor;
	}
	public void setPointLabelTextColor(int pointLabelTextColor) {
		this.pointLabelTextColor = pointLabelTextColor;
	}
	
	public void setXAxisLabel(String label){
		this.xAxisLabel = label;
	}
	public String getXAxisLabel(){
		return xAxisLabel;
	}public void setYAxisLabel(String label){
		this.yAxisLabel = label;
	}
	public String getYAxisLabel(){
		return yAxisLabel;
	}
	
	public void addPoint(ChartPoint point){
		chartPoints.add(point);
		if(point.getXValue()>XMAX)
			XMAX = point.getXValue();
		if(point.getYValue()>YMAX)
			YMAX = point.getYValue();
	}
	public void clearPoints(){
		chartPoints.clear();
		XMAX = 0;
		YMAX = 0;
	}
}
