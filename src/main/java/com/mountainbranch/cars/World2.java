package com.mountainbranch.cars;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class World2 extends WorldBase implements World {
	private static final int PIXEL_SIZE = 80;
	
	public World2() {
		super(1920 * PIXEL_SIZE,
		      1080 * PIXEL_SIZE,
		      new Rectangle(10 * PIXEL_SIZE, 270 * PIXEL_SIZE,
		                    70 * PIXEL_SIZE, 70 * PIXEL_SIZE));
		createWorld();
	}
	
	private void createWorld() {
		final int roadWidth1 = 180;
		final int roadWidth2 = 150;
		final int roadWidth3 = 120;
		final int roadWidth4 = 90;
		
		// Inner curve
		createObstacles(
				roadWidth1*16/9, 0,
				roadWidth1*16/9, roadWidth1,
				960,       roadWidth1,
				960+200,   roadWidth1-50,
				960+2*200, roadWidth1,
				1920-roadWidth2-50, roadWidth1,
				1920-roadWidth2,    roadWidth1+50,
				1920-roadWidth2,    330,
				1560+50, 540-50,
				1560+50, 540+50,
				1920-roadWidth2, 750,
				1920-roadWidth2, 1080-roadWidth3,
				480+roadWidth3,  1080-roadWidth3,
				480+roadWidth3,  810-roadWidth3,
				roadWidth4, 810-roadWidth3,
				roadWidth4, 0);
		
		{
			// Top-center wedge
			int width = 200;
			int height = 50;
			int xAnchor = 960-width;
			createObstacles(
					xAnchor-width, 0,
					xAnchor,       height,
					xAnchor+width, 0);
		}
		
		{
			// Top-right
			int x = 30;
			createObstacles(
					1920 - 6*x, 0,
					1920 - 3*x, x,
					1920 - x,   3*x,
					1920,       6*x);
		}
		
		{
			// Middle-right wedge
			int width = 150;
			createObstacles(
					1920,        540-width,
					1920-width, 540,
					1920,        540+width);
		}
		
		{
			int x = 75;
			// Bottom-right
			createObstacles(
					1920,   1080-x,
					1920-x, 1080);
		}
		
		// Bottom-left
		createObstacles(
				480, 1080,
				480, 810,
				0,   810);
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
