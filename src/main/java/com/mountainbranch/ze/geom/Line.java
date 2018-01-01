package com.mountainbranch.ze.geom;

import java.awt.Point;

/**
 * A {@code Line} represents a straight line segment between two endpoints in the (x,y)-plane.
 */
public class Line {
	public final Point endPoint1;
	public final Point endPoint2;

	/**
	 * Constructs a new {@code Line}, between {@code endPoint1} and {@code endPoint2}.
	 * The endpoints must be non-null, and not equal.
	 * 
	 * @throws IllegalArgumentException if the endpoints are equal, or either of them is null.
	 */
	public Line(Point endPoint1, Point endPoint2) {
		if (endPoint1 == null)
			throw new IllegalArgumentException("endPoint1 is null");
		if (endPoint2 == null)
			throw new IllegalArgumentException("endPoint2 is null");
		if (endPoint1.equals(endPoint2))
			throw new IllegalArgumentException("endPoint1 and endPoint2 are equal");
		this.endPoint1 = endPoint1;
		this.endPoint2 = endPoint2;
	}

	/**
	 * Returns a vector from {@code endPoint1} to {@code endPoint2}.
	 */
	public Point getParallelVector() {
		return new Point(endPoint2.x-endPoint1.x, endPoint2.y-endPoint1.y);
	}

	/**
	 * Returns a vector representing the normal to the line (i.e. perpendicular to the line).
	 */
	public Point getNormalVector() {
		Point parallel = getParallelVector();
		return new Point(-parallel.y, parallel.x);
	}

	/**
	 * Returns the endpoint of this line, that is the opposite endpoint compared to {@code p}.
	 * 
	 * @throws IllegalArgumentException if {@code p} is not equal to any of the endpoints.
	 */
	public Point getOppositeEndPoint(Point p) {
		if (endPoint1.equals(p))
			return endPoint2;
		else if (endPoint2.equals(p))
			return endPoint1;
		else
			throw new IllegalArgumentException(p + " is not an endpoint for " + this);
	}

	/**
	 * Returns {@code true} iff {@code p} is equal to either of the endpoints of this line.
	 */
	public boolean isEndPoint(Point p) {
		return endPoint1.equals(p) || endPoint2.equals(p);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Line) {
			Line line = (Line) o;
			return (line.endPoint1.equals(endPoint1) && line.endPoint2.equals(endPoint2))
					|| (line.endPoint1.equals(endPoint2) && line.endPoint2.equals(endPoint1));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return endPoint1.hashCode() + endPoint2.hashCode();
	}

	@Override
	public String toString() {
		return "Line[(" + endPoint1.x + "," + endPoint1.y + "),("
				+ endPoint2.x + "," + endPoint2.y + ")]";
	}
}
