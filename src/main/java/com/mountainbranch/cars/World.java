package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mountainbranch.ze.geom.Line;

public class World {
	private Dimension size = new Dimension(1920 * 50, 1200 * 50);
	private List<Line> obstacles = new LinkedList<Line>();
	
	public World() {
		// Perimeter
		createObstaclesLoop(
				new Point(0,            0),
				new Point(size.width-1, 0),
				new Point(size.width-1, size.height-1),
				new Point(0,            size.height-1)
				);
		
		createWorld2();
	}
	
	@SuppressWarnings("unused")
	private void createWorld1() {
		createObstacles(
				1.0,  0.5,
				0.75, 0.5,
				0.5,  0.75,
				0.25, 0.75,
				0.2,  0.65,
				0.2,  0.25,
				0.75, 0.1
				);
		
		createObstacles(
				1.0,  0.85,
				0.95, 0.95,
				0.85, 1.0);

		createObstacles(
				0.0, 0.9,
				0.1, 1.0);
		
		createObstaclesLoop(
				0.075, 0.4,
				0.125, 0.4,
				0.125, 0.2);
	}
	
	private void createWorld2() {
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
	
	private void createObstacles(double... coords) {
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < coords.length-1; i += 2) {
			points.add(createPoint(coords[i], coords[i+1]));
		}
		createObstacles(points.toArray(new Point[points.size()]));
	}
	
	private void createObstaclesLoop(double... coords) {
		createObstacles(coords);
		createObstacles(
				coords[coords.length-2], coords[coords.length-1],
				coords[0], coords[1]);
	}
	
	private void createObstacles(Point... points) {
		for (int i = 1; i < points.length; i++) {
			obstacles.add(new Line(points[i-1], points[i]));
		}
	}
	
	private void createObstaclesLoop(Point... points) {
		createObstacles(points);
		createObstacles(points[points.length-1], points[0]);
	}
	
	private Point createPoint(double worldX, double worldY) {
		return new Point((int) (size.getWidth()  * worldX),
		                 (int) (size.getHeight() * worldY));
	}
	
	public Point getStartLocation() {
		return new Point(
				size.width/4 + Car.SIZE.width,
				size.height/8 + Car.SIZE.height);
	}
	
	public double getStartAngle() {
		return 0.0;
	}
	
	public Dimension getSize() {
		return size;
	}
	
	public List<Line> getObstacles() {
		return obstacles;
	}
}
