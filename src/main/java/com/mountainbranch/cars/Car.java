package com.mountainbranch.cars;

import java.awt.Point;
import java.awt.geom.Point2D;

import com.mountainbranch.neuralnetwork.NeuralNetwork;

public class Car {
	private static final double MAX_SPEED = 500.0;         // mm/s
	private static final double MAX_STEERING = Math.PI/2.0; // radians/s
	
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
}
