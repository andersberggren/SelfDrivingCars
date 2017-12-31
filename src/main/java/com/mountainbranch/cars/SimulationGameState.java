package com.mountainbranch.cars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import com.mountainbranch.gameframework.core.GameEngine;
import com.mountainbranch.gameframework.core.GameState;
import com.mountainbranch.neuralnetwork.Evolution;
import com.mountainbranch.neuralnetwork.NeuralNetwork;

public class SimulationGameState implements GameState {
	private static Dimension CAR_SIZE = new Dimension(100, 50);
	
	private List<Car> cars = new ArrayList<Car>();
	
	public SimulationGameState() {
		Evolution evolution = new Evolution(0.1);
		List<NeuralNetwork> neuralNetworks = evolution.generatePopulation(10, 8, 8, 8, 2);
		for (NeuralNetwork nn : neuralNetworks) {
			cars.add(new Car(nn, new Point(1920/2, 1080/2), 0.0));
		}
	}
	
	@Override
	public void update(double deltaTime, GameEngine gameEngine) {
		for (Car car : cars) {
			car.update(deltaTime);
		}
	}

	@Override
	public void render(Graphics2D g, Dimension screenSize) {
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(new Color(32, 32, 32));
		g.fillRect(0, 0, screenSize.width, screenSize.height);

		for (int i = 0; i < cars.size(); i++) {
			float gradient = ((float) i) / cars.size();
			g.setColor(new Color(gradient, 1f-gradient, 0f));
			g.fill(carToPolygon(cars.get(i)));
		}
	}
	
	private Shape carToPolygon(Car car) {
		Polygon polygon = new Polygon();
		Point center = car.getLocation();
		
		double magnitude = Math.sqrt(Math.pow(CAR_SIZE.getWidth()/2.0, 2.0)
				+ Math.pow(CAR_SIZE.getHeight()/2.0, 2.0));
		double[] angles = new double[4];
		angles[0] = Math.acos(CAR_SIZE.getWidth()/2.0/magnitude);
		angles[1] = Math.PI - angles[0];
		angles[2] = -angles[1];
		angles[3] = -angles[0];
		
		for (double angle : angles) {
			angle += car.getDirection();
			Point p = new Point(center);
			p.x += (int) (magnitude*Math.cos(angle));
			p.y += (int) (magnitude*Math.sin(angle));
			polygon.addPoint(p.x, p.y);
		}
		
		return polygon;
	}
}
