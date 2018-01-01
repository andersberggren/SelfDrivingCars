package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.mountainbranch.ze.geom.Line;

public class World {
	private Dimension size = new Dimension(1920 * 50, 1200 * 50);
	private List<Line> obstacles = new LinkedList<Line>();
	
	public World() {
		// Perimeter
		createObstaclesLoop(
				new Point(0,          0),
				new Point(size.width, 0),
				new Point(size.width, size.height),
				new Point(0,          size.height)
				);
		
		createObstacles(
				new Point((int) (size.getWidth()*1.0),  (int) (size.getHeight()*0.5)),
				new Point((int) (size.getWidth()*0.75), (int) (size.getHeight()*0.5)),
				new Point((int) (size.getWidth()*0.5),  (int) (size.getHeight()*0.75)),
				new Point((int) (size.getWidth()*0.25), (int) (size.getHeight()*0.75)),
				new Point((int) (size.getWidth()*0.2),  (int) (size.getHeight()*0.65)),
				new Point((int) (size.getWidth()*0.2),  (int) (size.getHeight()*0.25)),
				new Point((int) (size.getWidth()*0.75), (int) (size.getHeight()*0.1))
				);
		
		createObstacles(
				new Point((int) (size.getWidth()*1.0),  (int) (size.getHeight()*0.85)),
				new Point((int) (size.getWidth()*0.95), (int) (size.getHeight()*0.95)),
				new Point((int) (size.getWidth()*0.85),  (int) (size.getHeight()*1.0))
				);

		createObstacles(
				new Point((int) (size.getWidth()*0.0),  (int) (size.getHeight()*0.9)),
				new Point((int) (size.getWidth()*0.1),  (int) (size.getHeight()*1.0))
				);
		
		createObstaclesLoop(
				new Point((int) (size.getWidth()*0.075), (int) (size.getHeight()*0.4)),
				new Point((int) (size.getWidth()*0.125), (int) (size.getHeight()*0.4)),
				new Point((int) (size.getWidth()*0.125), (int) (size.getHeight()*0.2))
				);
	}
	
	private void createObstacles(Point... points) {
		for (int i = 1; i < points.length; i++) {
			obstacles.add(new Line(points[i-1], points[i]));
		}
	}
	
	private void createObstaclesLoop(Point... points) {
		createObstacles(points);
		obstacles.add(new Line(points[points.length-1], points[0]));
	}
	
	public Dimension getSize() {
		return size;
	}
	
	public List<Line> getObstacles() {
		return obstacles;
	}
}
