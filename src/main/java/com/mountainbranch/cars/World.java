package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;

import com.mountainbranch.ze.geom.Line;

public interface World {
	public Point getStartLocation();
	public double getStartAngle();
	public Dimension getSize();
	public Collection<Line> getObstacles();
}
