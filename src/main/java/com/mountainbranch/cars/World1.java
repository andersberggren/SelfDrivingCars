package com.mountainbranch.cars;

import java.awt.Point;
import java.awt.Rectangle;

public class World1 extends WorldBase implements World {
	public World1() {
		super(1920 * 50, 1200 * 50, new Rectangle(0, 0, 10000, 600*50));
		createWorld();
	}
	
	private void createWorld() {
		// Inner curve
		createObstacles(
				0.25, 0.00,
				0.25, 0.25,
				0.50, 0.25,
				0.75, 0.15,
				0.80, 0.20,
				0.85, 0.25,
				0.85, 0.40,
				0.80, 0.50,
				0.60, 0.50,
				0.55, 0.55,
				0.55, 0.80,
				0.50, 0.85,
				0.15, 0.85,
				0.15, 0.75,
				0.10, 0.75,
				0.05, 0.10,
				0.00, 0.10);
		
		// Top-right
		createObstacles(
				0.80, 0.00,
				0.95, 0.10,
				1.00, 0.30);
		
		// Bottom-right
		createObstacles(
				1.00, 0.45,
				0.95, 0.55,
				0.85, 0.65,
				0.70, 0.65,
				0.65, 0.70,
				0.65, 0.95,
				0.60, 0.95,
				0.60, 1.00);
		
		// Bottom-left
		createObstacles(
				0.10, 1.00,
				0.10, 0.90,
				0.05, 0.90,
				0.05, 0.80,
				0.00, 0.80);
	}
	
	@Override
	public Point getStartLocation() {
		return new Point(
				size.width/4 + Car.SIZE.width,
				size.height/8 + Car.SIZE.height);
	}
	
	@Override
	public double getStartAngle() {
		return 0.0;
	}
}
