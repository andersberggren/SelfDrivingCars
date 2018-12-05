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
import com.mountainbranch.gui.Settings;
import com.mountainbranch.neuralnetwork.Evolution;
import com.mountainbranch.neuralnetwork.NeuralNetwork;
import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class SimulationGameState implements GameState {
	private static final Color COLOR_BACKGROUND = new Color(64, 64, 64);
	private static final Color COLOR_SENSOR = new Color(255, 192, 0, 64);
	private static final Color COLOR_CAR_OUTLINE = Color.BLACK;
	private static final Color COLOR_OBSTACLE = Color.LIGHT_GRAY;
	
	private final Settings settings;
	private World world = new World();
	private List<Car> allCars = new ArrayList<Car>();
	private Set<Car> activeCars = new HashSet<Car>();
	private Map<Car, Integer> fitness = new HashMap<Car, Integer>();
	private double time;
	
	public SimulationGameState(Settings settings) {
		this.settings = settings;
		reset();
	}
	
	@Override
	public void update(double deltaTime, GameEngine gameEngine) {
		if (settings.getFastForward()) {
			while (!update(deltaTime));
		} else {
			update(deltaTime);
		}
	}
	
	// Returns true iff this generation completed during this update
	private boolean update(double deltaTime) {
		time += deltaTime;

		for (Car car : new LinkedList<Car>(activeCars)) {
			car.update(deltaTime, world);
			updateFitness(car);
			if (hasCollided(car)) {
				activeCars.remove(car);
			}
		}
		
		if (activeCars.isEmpty() || time > 60.0) {
			reset();
			return true;
		}
		return false;
	}

	@Override
	public void render(Graphics2D g, Dimension screenSize) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(0, 0, screenSize.width, screenSize.height);
		g.scale(screenSize.getWidth()/world.getSize().getWidth(),
				screenSize.getHeight()/world.getSize().getHeight());
		
		// Draw car sensors
		g.setColor(COLOR_SENSOR);
		g.setStroke(new BasicStroke(50f));
		if (settings.getShowSensors()) {
			for (Car car : activeCars) {
				for (Line line : car.getSensorLines()) {
					g.drawLine(line.endPoint1.x, line.endPoint1.y,
							line.endPoint2.x, line.endPoint2.y);
				}
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
			
			g.setColor(COLOR_CAR_OUTLINE);
			g.setStroke(new BasicStroke(50f));
			g.draw(carShape);
		}
		
		// Draw obstacles
		g.setStroke(new BasicStroke(100f));
		g.setColor(COLOR_OBSTACLE);
		for (Line line : world.getObstacles()) {
			g.drawLine(line.endPoint1.x, line.endPoint1.y, line.endPoint2.x, line.endPoint2.y);
		}
	}
	
	private void reset() {
		List<NeuralNetwork> neuralNetworks;
		if (allCars.isEmpty()) {
			// This is the first generation
			int numberOfIndividuals = 100;
			int[] neuronsPerLayer = new int[4];
			for (int i = 0; i < neuronsPerLayer.length-1; i++) {
				neuronsPerLayer[i] = Car.NUMBER_OF_INPUTS;
			}
			neuronsPerLayer[neuronsPerLayer.length-1] = Car.NUMBER_OF_OUTPUTS;
			Evolution evolution = new Evolution(0.5);
			neuralNetworks = evolution.generatePopulation(numberOfIndividuals, neuronsPerLayer);
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
		
		Point startLocation = new Point(
				world.getSize().width - Car.SIZE.width*2,
				world.getSize().height/2 + Car.SIZE.width);
		double startAngle = 0.75*Math.PI;
		for (NeuralNetwork nn : neuralNetworks) {
			allCars.add(new Car(nn, startLocation, startAngle));
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
		Point center = new Point(world.getSize().width/2, world.getSize().height/2);
		double angleRadians = GeometryUtils.getAngle(center, car.getLocation());
		while (angleRadians < 0.0) {
			angleRadians += 2.0 * Math.PI;
		}
		while (angleRadians >= 2.0 * Math.PI) {
			angleRadians -= 2.0 * Math.PI;
		}
		Integer angle = (int) (angleRadians * 1000.0);
		if (angle < 0.0) {
			System.out.println("Angle < 0: " + angle );
		}
		Integer maxAngle = fitness.get(car);
		if (maxAngle == null || angle > maxAngle) {
			fitness.put(car, angle);
		}
	}
	
	private class CarComparator implements Comparator<Car> {
		@Override
		public int compare(Car o1, Car o2) {
			Integer fitness1 = fitness.get(o1);
			if (fitness1 == null) {
				fitness1 = 0;
			}
			Integer fitness2 = fitness.get(o2);
			if (fitness2 == null) {
				fitness2 = 0;
			}
			return (int) ((fitness1 - fitness2) * 1000.0);
		}
		
	}
}
