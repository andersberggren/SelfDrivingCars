package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mountainbranch.neuralnetwork.NeuralNetwork;
import com.mountainbranch.ze.geom.Line;

public class Car {
	// Distance is mm.
	// Speed is mm/s.
	// Rotation is radians/s.
	private static Dimension SIZE = new Dimension(4000, 2000);
	private static final double MAX_SPEED = 30000.0;
	private static final double MAX_STEERING = Math.PI;
	
	private final NeuralNetwork neuralNetwork;
	private final Point2D.Double location;
	private double direction;
	
	public Car(NeuralNetwork neuralNetwork, Point startLocation, double direction) {
		this.neuralNetwork = neuralNetwork;
		this.location = new Point2D.Double(startLocation.x, startLocation.y);
		this.direction = direction;
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}
	
	public void update(double deltaTime) {
		// TODO Read sensors (input)
		double[] input = new double[8];
		
		// Feed input values to neural network
		neuralNetwork.setInputs(input);
		
		// Use neural network output to determine velocity and steering
		double[] output = neuralNetwork.getOutputs();
		double velocity = output[0]*2.0 - 1.0;
		double steering = output[1]*2.0 - 1.0;
		
		// Move car
		direction += steering * MAX_STEERING * deltaTime;
		location.x += velocity * MAX_SPEED * deltaTime * Math.cos(direction);
		location.y += velocity * MAX_SPEED * deltaTime * Math.sin(direction);
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
}
