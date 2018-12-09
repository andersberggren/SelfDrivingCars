package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class World1 implements World {
	private Dimension size = new Dimension(1920 * 50, 1200 * 50);
	private List<Line> obstacles = new LinkedList<Line>();
	
	public World1() {
		// Perimeter
		createObstaclesLoop(
				new Point(0,            0),
				new Point(size.width-1, 0),
				new Point(size.width-1, size.height-1),
				new Point(0,            size.height-1)
				);
		
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
	
	private void createObstacles(double... coords) {
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < coords.length-1; i += 2) {
			points.add(createPoint(coords[i], coords[i+1]));
		}
		createObstacles(points.toArray(new Point[points.size()]));
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
	
	@Override
	public Dimension getSize() {
		return size;
	}
	
	@Override
	public Collection<Line> getObstacles() {
		return obstacles;
	}

	@Override
	public int getFitness(Car car) {
		Point center = new Point(size.width/2, size.height/2);
		Line midwayLine = new Line(center, new Point(size.width, size.height));
		Line carLine = new Line(center, car.getLocation());
		double angleRadians = GeometryUtils.getAngle(midwayLine, carLine);
		return (int) (angleRadians * 1000.0);
	}
}
