package com.mountainbranch.cars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mountainbranch.gameframework.core.GameEngine;
import com.mountainbranch.gameframework.core.GameState;
import com.mountainbranch.neuralnetwork.Evolution;
import com.mountainbranch.neuralnetwork.NeuralNetwork;
import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class SimulationGameState implements GameState {
	private World world = new World();
	private List<Car> allCars = new ArrayList<Car>();
	private Set<Car> activeCars = new HashSet<Car>();
	private Map<Car, Double> fitness = new HashMap<Car, Double>();
	private double time;
	
	public SimulationGameState() {
		reset();
	}
	
	@Override
	public void update(double deltaTime, GameEngine gameEngine) {
		time += deltaTime;
		
		for (Car car : new LinkedList<Car>(activeCars)) {
			car.update(deltaTime, world);
			updateFitness(car);
			if (hasCollided(car)) {
				activeCars.remove(car);
			}
		}
		
		if (time > 15.0 || activeCars.isEmpty()) {
			reset();
		}
	}

	@Override
	public void render(Graphics2D g, Dimension screenSize) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(new Color(64, 64, 64));
		g.fillRect(0, 0, screenSize.width, screenSize.height);
		g.scale(screenSize.getWidth()/world.getSize().getWidth(),
				screenSize.getHeight()/world.getSize().getHeight());
		
		// Draw car sensors
		g.setColor(new Color(255, 192, 0, 64));
		g.setStroke(new BasicStroke(50f));
		for (Car car : allCars) {
			for (Line line : car.getSensorLines()) {
				g.drawLine(line.endPoint1.x, line.endPoint1.y, line.endPoint2.x, line.endPoint2.y);
			}
		}
		
		// Draw cars
		for (int i = allCars.size()-1; i >= 0; i--) {
			Car car = allCars.get(i);
			Polygon carShape = new Polygon();
			for (Line line : car.asLines()) {
				carShape.addPoint(line.endPoint1.x, line.endPoint1.y);
			}
			float gradient = ((float) i) / allCars.size();
			g.setColor(new Color(gradient, 1f-gradient, 0f));
			g.fill(carShape);
			
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(50f));
			g.draw(carShape);
		}
		
		// Draw obstacles
		g.setStroke(new BasicStroke(100f));
		g.setColor(Color.LIGHT_GRAY);
		for (Line line : world.getObstacles()) {
			g.drawLine(line.endPoint1.x, line.endPoint1.y, line.endPoint2.x, line.endPoint2.y);
		}
	}
	
	private void reset() {
		List<NeuralNetwork> neuralNetworks;
		if (allCars.isEmpty()) {
			// This is the first generation
			Evolution evolution = new Evolution(1.0);
			neuralNetworks = evolution.generatePopulation(100, 8, 8, 8, 2);
		} else {
			// This is not the first generation.
			// Create a new generation based on the current generation.
			
			// Sort cars by fitness (highest fitness first)
			Collections.sort(allCars, new CarComparator());
			Collections.reverse(allCars);
			
			List<NeuralNetwork> currentGeneration = new LinkedList<NeuralNetwork>();
			for (Car car : allCars) {
				currentGeneration.add(car.getNeuralNetwork());
			}
			
			Evolution evolution = new Evolution(0.05);
			neuralNetworks = evolution.generateNextGeneration(currentGeneration);
		}
		
		allCars.clear();
		activeCars.clear();
		fitness.clear();
		time = 0.0;
		
		Point startLocation = new Point(4000, 4000);
		for (NeuralNetwork nn : neuralNetworks) {
			allCars.add(new Car(nn, startLocation, 0.0));
		}
		activeCars.addAll(allCars);
	}
	
	private boolean hasCollided(Car car) {
		for (Line carLine : car.asLines()) {
			for (Line obstacle : world.getObstacles()) {
				if (GeometryUtils.getIntersectionStrict(carLine, obstacle) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void updateFitness(Car car) {
		Point p = car.getLocation();
		Double distance = (double) (p.x*p.x + p.y*p.y);
		Double maxDistance = fitness.get(car);
		if (maxDistance == null || distance > maxDistance) {
			fitness.put(car, distance);
		}
	}
	
	private class CarComparator implements Comparator<Car> {
		@Override
		public int compare(Car o1, Car o2) {
			Double fitness1 = fitness.get(o1);
			if (fitness1 == null) {
				fitness1 = 0.0;
			}
			Double fitness2 = fitness.get(o2);
			if (fitness2 == null) {
				fitness2 = 0.0;
			}
			return (int) (fitness1 - fitness2);
		}
		
	}
}
