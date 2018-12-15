package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mountainbranch.ze.geom.Line;

public abstract class WorldBase implements World {
	protected final Dimension size;
	protected final List<Line> obstacles = new LinkedList<Line>();
	
	public WorldBase(int width, int height) {
		this.size = new Dimension(width, height);
		
		// Perimeter
		createObstaclesLoop(
				new Point(0,            0),
				new Point(size.width-1, 0),
				new Point(size.width-1, size.height-1),
				new Point(0,            size.height-1)
				);
	}
	
	@Override
	public abstract Point getStartLocation();

	@Override
	public abstract double getStartAngle();

	@Override
	public Dimension getSize() {
		return size;
	}

	@Override
	public Collection<Line> getObstacles() {
		return obstacles;
	}

	protected void createObstacles(double... coords) {
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < coords.length-1; i += 2) {
			points.add(createPoint(coords[i], coords[i+1]));
		}
		createObstacles(points.toArray(new Point[points.size()]));
	}
	
	protected void createObstacles(Point... points) {
		for (int i = 1; i < points.length; i++) {
			obstacles.add(new Line(points[i-1], points[i]));
		}
	}
	
	protected void createObstaclesLoop(Point... points) {
		createObstacles(points);
		createObstacles(points[points.length-1], points[0]);
	}
	
	protected Point createPoint(double worldX, double worldY) {
		return new Point((int) (size.getWidth()  * worldX),
		                 (int) (size.getHeight() * worldY));
	}
}
