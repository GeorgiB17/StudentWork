package SelectionWheel;


import org.apache.log4j.Logger;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Wheel extends JPanel {
	
	
	static private final Logger logger = Logger.getLogger(Wheel.class.getName());
		
	        
	public static enum Shape {
		CIRCLE,
		UMBRELLA
	}
	
	Image image = null;
	private boolean hasBorders = false;
	private double delta;
	private Point2D imagePosition;
	private Point2D rotationCenter;
	private double rotationAngle = 0;
	private double zoomFactor = 1;
	
	private ArrayList<Color> colors;
	int colorCounter = 0;
	
	private Shape shape = Shape.CIRCLE;
	private final int BORDER = 10;
	private int radius;
	private Point2D center = new Point2D.Double();
	
	ArrayList<String> stringList;
	private int withoutElement;
	private final int LIMIT = 100;
	private final int MAXFONTSIZE = 80, MINFONTSIZE = 10;
	private final Font DEFAULTFONT = new Font("TimesRoman", Font.PLAIN, 12);
	private Font font = DEFAULTFONT;

	private boolean isTurning = false;
	private double spinSpeed = 0;
	private double _maxSpinSpeed = 360;
	private double spinDeceleration = -20;
	private Timer speedTimer;
	private long timeStart, timeEnd;
	private double rotationAngleStart, rotationAngleEnd;
	private int _refreshRate = 100;
	private Point2D mouseDragPosition;

	@Override
	public void setBounds(int x, int y, int width, int height) {
		image = null;
		super.setBounds(x, y, width, height);
	}
	/*
	 * Borders on/off.
	 * If switched on, borders of sections and circle + circle center will be visible.
	 */
	public void hasBorders(boolean borders) {

		hasBorders = borders;
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	/*
	 * Set the shape of the wheel.
	 * Options in Shape enum.
	 */
	public void setShape(Shape shape) {

		shape = shape;
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	/*
	 * Get current rotation of the wheel.
	 */
	public double getRotationAngle() {

		return rotationAngle;
	}
	/*
	 * Set the current rotation of the wheel.
	 */
	public void setRotationAngle(double rotationAngle) {
		
		this.rotationAngle = rotationAngle % 360;
		this.repaint();
	}
	/*
	 * Get ArrayList of colors used for sections of the wheel.
	 */
	public ArrayList<Color> getColorScheme() {

		return colors;
	}
	/*
	 * Set ArrayList of colors used for sections of the wheel.
	 */
	public void setColorScheme(ArrayList<Color> colors) {

		this.colors = colors;
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	/*
	 * Add a new color to the existing color scheme for the sections of the wheel.
	 */
	public void addColor(Color color) {

		if(colors == null)
			colors = new ArrayList<Color>();
		colors.add(color);
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	/*
	 * Get radius of the wheel.
	 * The radius is set in DrawImage method based on the dimensions of this.
	 */
	public int getRadius() {

		return radius;
	}
	/*
	 * Get list of strings displayed inside the sections of the wheel.
	 */
	public ArrayList<String> getListOfStrings() {

		return stringList;
	}
	/**
	 * Set list of strings displayed inside the sections of the wheel.
	 * The initial list is set in constructor method and can be changed during runtime.
	 */
	public void setListOfStrings(ArrayList<String> list){

		withoutElement = list.size();
		if(withoutElement > LIMIT)
			throw new IllegalArgumentException("String list is larger then limit (" + LIMIT + ")");
		delta = (double)360 / (double) withoutElement;
		
		stringList = list;
		logger.info("A new List of Strings got imported");
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	@Override
	public Font getFont() {
		/*
		 * Get current font of the displayed strings in the wheel.
		 */
		return font;
	}
	
	@Override
	public void setFont(Font font) {
		/*
		 * Set current font of the displayed strings in the wheel.
		 */
		super.setFont(font);
		image = null;
		isTurning = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public double getSpinSpeed() {
		/*
		 * Get current spinning speed.
		 * If the spinning is off, it returns 0.
		 */
		return isTurning ? spinSpeed : 0;
	}
	
	public double getMaxSpinSpeed() {
		/*
		 * Get current speed limit.
		 */
		return _maxSpinSpeed;
	}
	/*
	 * Set current speed limit.
	 */
	public void setMaxSpinSpeed(double speed) {

		isTurning = false;
		_maxSpinSpeed = speed;
	}
	
	public double getSpinDeceleration() {
		return spinDeceleration;
	}

	/**
	 * This Operations is setting the deceleration of the wheel
	 * @param deceleration a double which the deceleration should be set to
	 */
	
	public void setSpinDeceleration(double deceleration){
		logger.info(String.format("The spin deceleration has been set to: %.2f", deceleration));
		assert (deceleration < 0 ): "Parameter value for acceleration must be > 0";
		spinDeceleration = deceleration;
	}
	/*
	 * Check if the wheel is spinning.
	 */
	public boolean isSpinning() {
		return isTurning;
	}
	
	public String getSelectedString() {
		/*
		 * Get current selection.
		 * Returns the string which is displayed in the section of the wheel that is currently positioned between 0 and delta degrees.
		 * The idea is to get the number of deltas in current rotationAngle.
		 * This number is added to the size of the string arraylist, and then MODed by the size of the string arraylist,
		 * in order to avoid negative indices.
		 */
		return stringList.get((int)Math.floor(withoutElement + (rotationAngle % 360) / delta) % withoutElement);
	}
	/*
	 * Constructor of the class.
	 * Sets the string arraylist, adds mouse listeners and stast TimerTask to measure the rotation speed.
	 */
	public Wheel(ArrayList<String> listOfStrings){
		setListOfStrings(listOfStrings);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseDragPosition = new Point2D.Double(e.getX(), e.getY());
				// to stop the spinning if the circle is clicked on
				double distance = Math.sqrt(Math.pow(mouseDragPosition.getX() - center.getX(),2) + Math.pow(mouseDragPosition.getY() - center.getY(),2));
				if(distance <= radius)
				{
					stopSpin();
				}
				// to measure initial speed
				timeStart = System.currentTimeMillis();
				rotationAngleStart = rotationAngle;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				// to measure initial speed
				timeEnd = System.currentTimeMillis();
				rotationAngleEnd = rotationAngle;
				double initialSpeed = 1000 * (rotationAngleEnd - rotationAngleStart) / (timeEnd - timeStart);
				initialSpeed = (int)Math.signum(initialSpeed) * Math.min(Math.abs(initialSpeed), _maxSpinSpeed);
				try {
					spinStartAsync(Math.abs(initialSpeed), (int)Math.signum(initialSpeed), spinDeceleration);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				stopSpin();
				/*
				 * Use the equation for angle between two vectors:
				 * vector 1 between last position of mouse and center of circle
				 * vector 2 between current position of mouse and center of circle
				 * ("k" is direction coefficient)
				 */
				Point2D mousePos = new Point2D.Double(e.getX(), e.getY());
				double k1 = (mouseDragPosition.getY() - rotationCenter.getY()) / (mouseDragPosition.getX() - rotationCenter.getX());
				double k2 = (mousePos.getY() - rotationCenter.getY()) / (mousePos.getX() - rotationCenter.getX());
				double _delta = Math.toDegrees(Math.atan((k2-k1)/(1 + k2 * k1)));
				if(!Double.isNaN(_delta))
					setRotationAngle(getRotationAngle() + _delta);
				mouseDragPosition = mousePos;
			}
		});
		
		// start TimerTask to measure current speed
		TimerTask timerTask = new speedTimerTask();
		speedTimer = new Timer(true);
		speedTimer.schedule(timerTask, 0);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		/*
		 * Paintcomponent - if the image is null, create it and then draw it whilst keeping the current rotation.
		 * The image can be larger than the displaying area, so after it is drawn it needs to be placed properly.
		 */
		super.paintComponent(g);
		
		if(image == null) {
			image = drawImage();
			rotationCenter = new Point2D.Double(
					this.getWidth() - image.getWidth(null) + center.getX(),
					this.getHeight() / 2
				);
			imagePosition = new Point2D.Double(
						(int)(this.getWidth() - image.getWidth(null)),
						(int)(this.getHeight() / 2 - center.getY())
					);
		}
		
		Graphics2D gPanel = (Graphics2D) g;
		gPanel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gPanel.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		gPanel.rotate(Math.toRadians(rotationAngle), rotationCenter.getX(), rotationCenter.getY());
		gPanel.drawImage(image, (int) imagePosition.getX(), (int) imagePosition.getY(), null);
	}
	
	private BufferedImage drawImage()
	{
		/*
		 * Calculate all the necessary parameters for the wheel and draw it section by section.
		 */
		int width = this.getWidth(), height = this.getHeight();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();

		// Calculate radius
		radius = Math.min(img.getWidth(), img.getHeight()) / 2 - BORDER;
		
		double stringDistanceFromEdge = 0.05 * radius;
		int fontSize, stringWidth, maxStringWidth;
		
		maxStringWidth = (int)(radius - 2 * stringDistanceFromEdge);
		fontSize = calcFontSize(g2d, stringDistanceFromEdge, maxStringWidth);
		g2d.setFont(new Font(font.getFamily(), font.getStyle(), fontSize));
		
		// Adjust the parameters (for "zoom in") - if the font size is too small
		if(fontSize < MINFONTSIZE) {
			zoomFactor = (double)MINFONTSIZE / fontSize;
			width += (int) 2 * ((zoomFactor * radius) - radius);
			height += (int) 2 * ((zoomFactor * radius) - radius);
			radius = (int)(zoomFactor * radius);
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D) img.getGraphics();
			maxStringWidth = (int)(radius - 2 * stringDistanceFromEdge);
			fontSize = calcFontSize(g2d, stringDistanceFromEdge, maxStringWidth);
		}
		
		// Calculate center point
		center = new Point2D.Double((double)img.getWidth() / 2, (double)img.getHeight() / 2);
		
		// Set rendering hints
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.rotate(Math.toRadians(rotationAngle), center.getX(), center.getY());
		
		// Draw center point
		if(hasBorders) {
			g2d.setColor(Color.BLACK);
			g2d.fillArc((int) center.getX() - (int)Math.floor(Math.max(0.01 * radius, 1)), (int) center.getY() - (int)Math.floor(Math.max(0.01 * radius, 1)), (int)Math.floor(Math.max(0.01 * 2 * radius, 2)), (int)Math.floor(Math.max(0.01 * 2 * radius, 2)), 0, 360);
		}
		
		// Divide circle and draw strings
		FontMetrics fontMetrics;
		if(colors == null)
			colors = getDefaultColorList();
		colorCounter = 0;
		for(int i = withoutElement - 1; i >= 0; i--)
		{
			// Draw section border
			if(hasBorders) {
				g2d.setColor(Color.BLACK);
				g2d.drawLine((int) center.getX(), (int) center.getY(), (int) center.getX() + radius, (int) center.getY());
			}
			// Fill section depending on the chosen shape
			g2d.setColor(colors.get(colorCounter++ % colors.size()));
			if(shape == Shape.UMBRELLA)
				fillTriangle(g2d);
			else //if(_shape == Shape.CIRCLE)
				fillArc(g2d);
			// Draw string - rotate half delta, then draw then rotate the other half (to have the string in the middle)
			g2d.rotate(Math.toRadians(delta / 2), center.getX(), center.getY());
			g2d.setColor(Color.BLACK);
			fontMetrics = g2d.getFontMetrics();
			stringWidth = fontMetrics.stringWidth(stringList.get(i));
			g2d.drawString(stringList.get(i), (int)(center.getX() + maxStringWidth - stringWidth + stringDistanceFromEdge), (int)(center.getY() + (double)fontMetrics.getHeight() / 2 - fontMetrics.getMaxDescent()));
			g2d.rotate(Math.toRadians(delta / 2), center.getX(), center.getY());
		}
		
		return img;
	}
	/*
	 * Calculates the optimal font size for the strings inside the sections.
	 * The strings need to be positioned next to the broader end of the section.
	 * The optimal size will depend on the longest string length and maximum height of the section
	 * in the left border of the rectangle surrounding the string.
	 */
	private int calcFontSize(Graphics g, double stringDistanceFromEdge, int maxStringWidth) {
		// Find the longest string
		String tmpString = "";
		for(int i = withoutElement - 1; i >= 0; i--) {
			if(stringList.get(i).length() > tmpString.length())
				tmpString = stringList.get(i);
		}
		
		// Set it to max font size and calculate rectangle
		int fontSize = MAXFONTSIZE;
		g.setFont(new Font(font.getFamily(), font.getStyle(), fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(tmpString, g);
		
		// Adjust string height / font size
		int maxHeight = (int)Math.floor(2 * stringDistanceFromEdge * Math.sin(Math.toRadians(delta / 2)));
		if(stringBounds.getHeight() > maxHeight) {
			fontSize = (int)Math.floor(fontSize * maxHeight / stringBounds.getHeight());
			g.setFont(new Font(font.getFamily(), font.getStyle(), fontSize));
			fontMetrics = g.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		
		// Adjust string width
		// If the string is too narrow, increase font until it fits
		double K = stringBounds.getWidth() / stringBounds.getHeight();
		maxHeight = (int)Math.floor(2 * (radius - stringDistanceFromEdge) * Math.tan(Math.toRadians(delta / 2)) / (1 + 2 * K * Math.tan(Math.toRadians(delta / 2))));
		while(stringBounds.getWidth() < maxStringWidth) {
				g.setFont(new Font(font.getFamily(), font.getStyle(), ++fontSize));
				fontMetrics = g.getFontMetrics();
				stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		// If the string is too wide, decrease font until it fits
		while(stringBounds.getWidth() > maxStringWidth) {
			g.setFont(new Font(font.getFamily(), font.getStyle(), --fontSize));
			fontMetrics = g.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		
		return Math.min(fontSize, MAXFONTSIZE);
	}
	
	private void fillArc(Graphics g2d) {
		g2d.fillArc((int) center.getX() - radius, (int) center.getY() - radius, 2 * radius, 2 * radius, 0, (int)- Math.ceil(delta)); // use ceil because of decimal part (would be left empty)
		if(hasBorders) {
			g2d.setColor(Color.black);
			g2d.drawArc((int) center.getX() - radius, (int) center.getY() - radius, 2 * radius, 2 * radius, 0, (int)- Math.ceil(delta));
		}
	}
	/*
	 * Method that draws section as a triangle (in case Shape=UMBRELLA was chosen)
	 */
	private void fillTriangle(Graphics2D g2d) {
		int[] xpoints = new int[3];
		xpoints[0] = (int) center.getX();
		xpoints[1] = (int) center.getX() + radius;
		int dx = (int) (2 * radius * Math.pow(Math.sin(Math.toRadians(delta / 2)), 2));
		xpoints[2] = xpoints[1] - dx;
		int[] ypoints = new int[3];
		ypoints[0] = (int) center.getY();
		ypoints[1] = (int) center.getY();
		int dy = (int) (2 * radius * Math.sin(Math.toRadians(delta / 2)) * Math.cos(Math.toRadians(delta / 2)));
		ypoints[2] = ypoints[1] + dy;
		g2d.fillPolygon(xpoints, ypoints, 3);
		if(hasBorders) {
			g2d.setColor(Color.black);
			g2d.drawLine(xpoints[1], ypoints[1], xpoints[2], ypoints[2]);
		}
	}
	/*
	 * Runnable class that handles the spinning of the wheel.
	 * It sets the rotation angle by calculating the speed through time based on deceleration.
	 * Each setRotationAngle call will cause the wheel to be redrawn.
	 */
	private class SpinRunnable implements Runnable {

		private double spinSpeed;
		private int spinDirection;
		private double spinDeceleration;

	    public SpinRunnable(double speed, int direction, double deceleration) {
	        this.spinSpeed = speed;
	        this.spinDirection = direction;
	        this.spinDeceleration = deceleration;
	    }

	    public void run()
		{
	    	isTurning = true;
			int sleepTime = 1000 / _refreshRate;
			double delta;
			while(isTurning && spinSpeed > 0)
			{
				delta = spinDirection * (spinSpeed / _refreshRate);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
					 logger.error(e);
				}
				setRotationAngle(getRotationAngle() + delta);
				spinSpeed += spinDeceleration / _refreshRate;
			}
			isTurning = false;
		}
	}
	/**
	 * Method that starts the spinning thread.
	 * Parameters:
	 * speed => degrees per second
	 * direction => "< 0" = clockwise , "> 0" = counter-clockwise, "=0" = stand still
	 * deceleration => "< 0" = degrees per second per second reducing speed, "= 0" = perpetual spin
	 */
	public void spinStartAsync(double speed, int direction, double deceleration) {
		assert deceleration <= 0;
		SpinRunnable spinRunnable = new SpinRunnable(speed, direction, deceleration);
		Thread t = new Thread(spinRunnable);
		t.start();
	}
	/*
	 * Sets the flag to stop the spinnning.
	 */
	public void stopSpin() {

		isTurning = false;
		logger.info("wheel gets stopped and can't spin");
	}
	/*
	 * TimerTask class that monitors and refreshes the _spinSpeed
	 * The speed is calculated as a difference of two rotation angles over a period of time.
	 * We add the 360 to the "now" angle and then MOD it by 360 to avoid miscalculation when passing the full circle.
	 */
	private class speedTimerTask extends TimerTask {

		@Override
		public void run() {
			double prevAngle, nowAngle;
			long sleepTime = 100;
			while(true) {
				prevAngle = getRotationAngle();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				nowAngle = getRotationAngle();
				nowAngle = (nowAngle + Math.signum(nowAngle) * 360) % 360;
				spinSpeed = Math.abs(nowAngle - prevAngle) * (1000 / sleepTime);
			}
		}
	}
	/*
	 * Returns default color list.
	 * To be used in case when no explicit color list is set.
	 */
	private ArrayList<Color> getDefaultColorList() {
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		colors.add(Color.CYAN);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.GRAY);
		colors.add(Color.GREEN);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.MAGENTA);
		colors.add(Color.ORANGE);
		colors.add(Color.PINK);
		colors.add(Color.RED);
		colors.add(Color.WHITE);
		colors.add(Color.YELLOW);
		return colors;
	}
}