package com.mountainbranch.ze.geom;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Geometry-related methods for {@link Point} and {@link Line}, e.g. length, distance,
 * angle, shortest vector from line to point, intersection point of two lines.
 */
public class GeometryUtils {
	/**
	 * Returns the distance between {@code a} and {@code b}.
	 */
	public static double getDistance(Point a, Point b) {
		return a.distance(b.x, b.y);
	}

	/**
	 * Returns the length of vector {@code p}.
	 */
	public static double getLength(Point p) {
		return p.distance(0.0, 0.0);
	}

	/**
	 * Returns the length of vector {@code p}.
	 */
	public static double getLength(Point2D.Double p) {
		return p.distance(0.0, 0.0);
	}

	/**
	 * Returns the angle (in radians) of vector {@code p}, relative to the x-axis. Range: [-pi,pi]
	 */
	public static double getAngle(Point p) {
		return Math.atan2((double) p.y, (double) p.x);
	}

	/**
	 * Returns the angle (in radians) of vector {@code p}, relative to the x-axis. Range: [-pi,pi]
	 */
	public static double getAngle(Point2D.Double p) {
		return Math.atan2(p.y, p.x);
	}

	/**
	 * Returns the angle (in radians) of the vector from {@code from} to {@code to},
	 * relative to the x-axis. Range: [-pi,pi]
	 */
	public static double getAngle(Point from, Point to) {
		return getAngle(getVectorBetweenPoints(from, to));
	}

	/**
	 * Returns the angle (in radians) of line {@code line2}, relative to line {@code line1}.
	 * Range: [-pi,pi]
	 */
	public static double getAngle(Line line1, Line line2) {
		double angle = getAngle(line2.getParallelVector()) - getAngle(line1.getParallelVector());
		return getNormalizedAngle(angle);
	}

	/**
	 * Returns the value of {@code angle}, normalized to the interval [-pi,pi].
	 */
	public static double getNormalizedAngle(double angle) {
		while (angle > Math.PI)
			angle -= 2.0 * Math.PI;
		while (angle < -Math.PI)
			angle += 2.0 * Math.PI;
		return angle;
	}

	/**
	 * Returns a vector from {@code from} to {@code to}.
	 */
	public static Point getVectorBetweenPoints(Point from, Point to) {
		return new Point(to.x-from.x, to.y-from.y);
	}

	/**
	 * Returns the shortest vector from {@code line} to {@code p}, which is either perpendicular
	 * to a point on the line, or the vector from the closest endpoint.
	 */
	public static Point getShortestVectorFromLineToPoint(Line line, Point p) {
		// 'a' and 'b' are the line's endpoints.
		// 'ab', 'ba', 'ap' and 'bp' are vectors from/to the respective points.
		Point ab = line.getParallelVector();
		Point ba = new Point(-ab.x, -ab.y);
		Point ap = getVectorBetweenPoints(line.endPoint1, p);
		Point bp = getVectorBetweenPoints(line.endPoint2, p);

		if (dotProduct(ab, ap) < 0 || dotProduct(ba, bp) < 0) {
			// One of the dot products are negative, which means that the perpendicular projection
			// of p on the line is outside the line (i.e. somewhere on the line's extension),
			// and the shortest vector to the line is from the closest endpoint.
			Point v1 = getVectorBetweenPoints(line.endPoint1, p);
			Point v2 = getVectorBetweenPoints(line.endPoint2, p);
			return getLength(v1) < getLength(v2) ? v1 : v2;
		} else {
			// The shortest vector is perpendicular to the line
			Point normal = line.getNormalVector();
			double normalLength = getLength(normal);
			double distance = dotProduct(normal, ap) / normalLength;
			return new Point((int) (normal.x * distance / normalLength),
					(int) (normal.y * distance / normalLength));
		}
	}

	/**
	 * Returns {@code true} iff {@code line1} and {@code line2} are parallel.
	 */
	public static boolean isParallel(Line line1, Line line2) {
		Point vector1 = line1.getParallelVector();
		Point vector2 = line2.getParallelVector();
		return crossProductLength(vector1, vector2) == 0;
	}

	/**
	 * Returns the intersection point of {@code line1} and {@code line2}. If the lines are
	 * parallel, or don't intersect, {@code null} is returned.
	 */
	public static Point getIntersectionStrict(Line line1, Line line2) {
		return getIntersection(line1, line2, true);
	}

	/**
	 * Returns the intersection point of {@code line1} and {@code line2} (lines extended
	 * infinitely in both directions). If the lines are parallel, {@code null} is returned.
	 */
	public static Point getIntersectionExtended(Line line1, Line line2) {
		return getIntersection(line1, line2, false);
	}

	// http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
	private static Point getIntersection(Line line1, Line line2, boolean strict) {
		// line1 goes from p to p+r
		// line2 goes from q to q+s
		// Intersection point is: p+t*r = q+u*s
		Point p = line1.endPoint1;
		Point q = line2.endPoint1;
		Point r = line1.getParallelVector();
		Point s = line2.getParallelVector();

		// Iff the cross product (r x s) is 0, lines are parallel
		long r_x_s = crossProductLength(r, s);
		if (r_x_s != 0) {
			Point pq = getVectorBetweenPoints(p, q);
			double t = (double) crossProductLength(pq, s) / (double) r_x_s;
			double u = (double) crossProductLength(pq, r) / (double) r_x_s;
			if (!strict || (t >= 0.0 && t <= 1.0 && u >= 0.0 && u <= 1.0)) {
				Point intersection = new Point(p);
				intersection.x += t * (double) r.x;
				intersection.y += t * (double) r.y;
				return intersection;
			}
		}

		return null;
	}

	/**
	 * Returns the dot product of vectors {@code a} and {@code b}
	 */
	public static long dotProduct(Point a, Point b) {
		return a.x*b.x + a.y*b.y;
	}

	/**
	 * Returns the length (i.e. z-component) of the cross product of {@code a} and
	 * {@code b}. {@code a} and {@code b} are points in the (x,y)-plane.
	 */
	private static long crossProductLength(Point a, Point b) {
		return (long) a.x*b.y - (long) a.y*b.x;
	}
}
