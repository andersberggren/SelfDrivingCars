package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class World2 extends WorldBase implements World {
	private static final int PIXEL_SIZE = 80;
	private static final Dimension resolution = new Dimension(1920, 1200);
	
	public World2() {
		super(resolution.width * PIXEL_SIZE,
		      resolution.height * PIXEL_SIZE,
		      new Rectangle(10 * PIXEL_SIZE, 235 * PIXEL_SIZE,
		                    70 * PIXEL_SIZE, 70 * PIXEL_SIZE));
		createWorld();
	}
	
	private void createWorld() {
		final int roadWidth1 = 180;
		final int roadWidth2 = 150;
		final int roadWidth3 = 120;
		final int roadWidth4 = 90;
		final int speedBumpWidth = 30;
		final int speedBumpLength = 100;
		
		// Inner curve
		createObstacles(
				roadWidth1*resolution.width/resolution.height, 0,
				roadWidth1*resolution.width/resolution.height, roadWidth1,
				// Wedge
				resolution.width/2,       roadWidth1,
				resolution.width/2+200,   roadWidth1-50,
				resolution.width/2+2*200, roadWidth1,
				// Top-right corner
				resolution.width-roadWidth2-75, roadWidth1,
				resolution.width-roadWidth2,    roadWidth1+75,
				// Middle-right
				resolution.width-roadWidth2,    resolution.height/2-roadWidth2*99/70,
				resolution.width-2*roadWidth2, resolution.height/2-roadWidth2*(99-70)/70,
				resolution.width-2*roadWidth2, resolution.height/2+roadWidth2*(99-70)/70,
				resolution.width-roadWidth2, resolution.height/2+roadWidth2*99/70,
				// Bottom-right corner
				resolution.width-roadWidth2,    resolution.height-roadWidth3-50,
				resolution.width-roadWidth2-50, resolution.height-roadWidth3,
				// Speed bumps
				resolution.width*5/8+speedBumpLength/2, resolution.height-roadWidth3,
				resolution.width*5/8,                   resolution.height-roadWidth3+speedBumpWidth,
				resolution.width*5/8-speedBumpLength/2, resolution.height-roadWidth3+speedBumpWidth,
				resolution.width*5/8-speedBumpLength/2, resolution.height-roadWidth3,
				resolution.width*4/8+speedBumpLength/2, resolution.height-roadWidth3,
				resolution.width*4/8+speedBumpLength/2, resolution.height-roadWidth3+speedBumpWidth,
				resolution.width*4/8-speedBumpLength/2, resolution.height-roadWidth3+speedBumpWidth,
				resolution.width*4/8-speedBumpLength/2, resolution.height-roadWidth3,
				// Right-angled corners
				resolution.width/4+roadWidth3, resolution.height-roadWidth3,
				resolution.width/4+roadWidth3,    resolution.height*3/4-roadWidth4+60,
				resolution.width/4+roadWidth3-60, resolution.height*3/4-roadWidth4,
				roadWidth4, resolution.height*3/4-roadWidth4,
				roadWidth4, roadWidth1,
				0,          roadWidth1);
		
		{
			// Top-center wedge
			int width = 200;
			int height = 50;
			int xAnchor = resolution.width/2-width;
			createObstacles(
					xAnchor-width, 0,
					xAnchor,       height,
					xAnchor+width, 0);
		}
		
		{
			// Top-right
			int x = 40;
			int y = 40;
			createObstacles(
					resolution.width-6*x, 0,
					resolution.width-3*x, y,
					resolution.width-x,   3*y,
					resolution.width,     6*y);
		}
		
		{
			// Middle-right wedge
			createObstacles(
					resolution.width,            resolution.height/2-roadWidth2,
					resolution.width-roadWidth2, resolution.height/2,
					resolution.width,            resolution.height/2+roadWidth2);
		}
		
		{
			// Bottom-right
			int x = 30;
			int y = 30;
			createObstacles(
					resolution.width,     resolution.height-6*y,
					resolution.width-x,   resolution.height-3*y,
					resolution.width-3*x, resolution.height-y,
					resolution.width-6*x, resolution.height);
		}
		
		{
			// Bottom-center speed bumps
			int x = speedBumpLength;
			int y = speedBumpWidth;
			createObstacles(
			resolution.width*6/8+x/2, resolution.height,
			resolution.width*6/8,     resolution.height-y,
			resolution.width*6/8-x/2, resolution.height-y,
			resolution.width*6/8-x/2, resolution.height,
			resolution.width*4/8+x/2, resolution.height,
			resolution.width*4/8+x/2, resolution.height-y,
			resolution.width*4/8-x/2, resolution.height-y,
			resolution.width*4/8-x/2, resolution.height);
		}
		
		// Bottom-left
		createObstacles(
				resolution.width/4+60, resolution.height,
				resolution.width/4,    resolution.height-60,
				resolution.width/4,    resolution.height*3/4,
				0,   resolution.height*3/4);
	}
	
	@Override
	public Point getStartLocation() {
		return new Point(400 * PIXEL_SIZE, 90 * PIXEL_SIZE);
	}
	
	@Override
	public double getStartAngle() {
		return 0.0;
	}
	
	protected void createObstacles(int... coords) {
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < coords.length-1; i += 2) {
			points.add(createPoint(coords[i], coords[i+1]));
		}
		createObstacles(points.toArray(new Point[points.size()]));
	}

	@Override
	protected Point createPoint(double pixelX, double pixelY) {
		return new Point((int) (pixelX * PIXEL_SIZE),
		                 (int) (pixelY * PIXEL_SIZE));
	}
}
