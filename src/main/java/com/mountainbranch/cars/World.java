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
		Point[] walls = new Point[]{
				new Point(500,     500),
				new Point(20000,   500),
				new Point(30000,  5000),
				new Point(50000, 25000),
				new Point(60000, 45000),
				new Point(65000, 50000),
				new Point(70000, 52500),
				new Point(95000, 52500)
		};
		for (int i = 1; i < walls.length; i++) {
			obstacles.add(new Line(walls[i-1], walls[i]));
		}
		
		Point[] perimeter = new Point[]{
				new Point(0,          0),
				new Point(size.width, 0),
				new Point(size.width, size.height),
				new Point(0,          size.height),
		};
		for (int i = 0; i < perimeter.length; i++) {
			int otherIndex = (i+1) % perimeter.length;
			obstacles.add(new Line(perimeter[i], perimeter[otherIndex]));
		}
	}
	
	public Dimension getSize() {
		return size;
	}
	
	public List<Line> getObstacles() {
		return obstacles;
	}
}
