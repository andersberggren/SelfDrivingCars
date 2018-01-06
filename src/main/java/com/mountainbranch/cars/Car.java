package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mountainbranch.neuralnetwork.NeuralNetwork;
import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class Car {
	// Distance is mm.
	// Speed is mm/s.
	// Rotation is radians/s.
	public static final Dimension SIZE = new Dimension(4000, 2000);
	public static final int NUMBER_OF_INPUTS = 8;
	public static final int NUMBER_OF_OUTPUTS = 2;
	private static final double MAX_SPEED = 30000.0;
	private static final double MAX_STEERING = Math.PI;
	
	private final NeuralNetwork neuralNetwork;
	private final Point2D.Double location;
	private double direction;
	private List<Sensor> sensors;
	
	public Car(NeuralNetwork neuralNetwork, Point startLocation, double direction) {
		this.neuralNetwork = neuralNetwork;
		this.location = new Point2D.Double(startLocation.x, startLocation.y);
		this.direction = direction;
		
		// Create sensors
		sensors = new ArrayList<Sensor>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (!(x == 0 && y == 0)) {
					double xOffset = SIZE.getWidth()/2.0  * x;
					double yOffset = SIZE.getHeight()/2.0 * y;
					double angle = Math.atan2(yOffset, xOffset);
					double offset = Math.sqrt(xOffset*xOffset + yOffset*yOffset);
					sensors.add(new Sensor(angle, offset));
				}
			}
		}
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}
	
	public void update(double deltaTime, World world) {
		// Read sensors
		double[] input = readSensors(world);
		
		// Feed sensor values to neural network
		neuralNetwork.setInputs(input);
		
		// Use neural network output to determine velocity and steering
		double[] output = neuralNetwork.getOutputs();
		double velocity = (output[0]*2.0 - 1.0) * MAX_SPEED;
		double steering = (output[1]*2.0 - 1.0) * MAX_STEERING;
		
		// Move car
		double turnRadius = SIZE.getWidth() * 0.5;
		double maxSteering = Math.abs(velocity) / turnRadius;
		if (Math.abs(steering) > maxSteering) {
			steering = maxSteering * Math.signum(steering);
		}
		direction += steering * deltaTime;
		location.x += velocity * deltaTime * Math.cos(direction);
		location.y += velocity * deltaTime * Math.sin(direction);
	}
	
	public Point getLocation() {
		return new Point((int) location.x, (int) location.y);
	}
	
	public double getDirection() {
		return direction;
	}
	
	public Collection<Line> asLines() {
		double[] angles = new double[4];
		angles[0] = Math.atan(SIZE.getHeight()/SIZE.getWidth());
		angles[1] = Math.PI - angles[0];
		angles[2] = -angles[1];
		angles[3] = -angles[0];
		Point[] vertices = new Point[4];
		double magnitude = Math.sqrt(Math.pow(SIZE.getWidth()/2.0, 2.0)
				+ Math.pow(SIZE.getHeight()/2.0, 2.0));
		
		for (int i = 0; i < vertices.length; i++) {
			double angle = angles[i] + direction;
			vertices[i] = new Point(
					(int) (location.x + magnitude*Math.cos(angle)),
					(int) (location.y + magnitude*Math.sin(angle)));
		}
		
		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < vertices.length; i++) {
			lines.add(new Line(vertices[i], vertices[(i+1)%vertices.length]));
		}
		
		return lines;
	}
	
	public Collection<Line> getSensorLines() {
		List<Line> sensorLines = new LinkedList<Line>();
		for (Sensor sensor : sensors) {
			sensorLines.add(getSensorLine(sensor, false));
		}
		return sensorLines;
	}
	
	/**
	 * If {@code showMaxDistance} is true, the returned line will show the max
	 * distance of the sensor, rather than the distance to the closest obstacle.
	 */
	private Line getSensorLine(Sensor sensor, boolean showMaxDistance) {
		double angle = direction + sensor.angle;
		double distance = showMaxDistance ? Sensor.MAX_DISTANCE : sensor.distance;
		distance = Math.max(distance, 10.0);
		Point near = new Point(
				(int) (location.x + sensor.offset*Math.cos(angle)),
				(int) (location.y + sensor.offset*Math.sin(angle)));
		Point far = new Point(
				(int) (location.x + (sensor.offset+distance)*Math.cos(angle)),
				(int) (location.y + (sensor.offset+distance)*Math.sin(angle)));
		return new Line(near, far);
	}
	
	private double[] readSensors(World world) {
		double[] values = new double[sensors.size()];
		List<Line> obstacles = world.getObstacles();
		
		for (int i = 0; i < sensors.size(); i++) {
			Sensor sensor = sensors.get(i);
			Line sensorLine = getSensorLine(sensor, true);
			double distance = Sensor.MAX_DISTANCE;
			for (Line obstacle : obstacles) {
				Point collisionPoint = GeometryUtils.getIntersectionStrict(sensorLine, obstacle);
				if (collisionPoint != null) {
					Point sensorLocation = sensorLine.endPoint1;
					distance = Math.min(distance,
							GeometryUtils.getDistance(sensorLocation, collisionPoint));
				}
			}
			sensor.distance = distance;
			values[i] = distance/Sensor.MAX_DISTANCE;
		}
		
		return values;
	}
	
	private class Sensor {
		private static final double MAX_DISTANCE = 10000.0;
		
		/** The angle of the sensor, relative to the cars forward direction. */
		private final double angle;
		/** The distance from the cars center to the sensor. */
		private final double offset;
		private double distance;
		
		private Sensor(double angle, double offset) {
			this.angle = angle;
			this.offset = offset;
		}
	}
}
