package com.mountainbranch.cars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	private double time;
	
	public SimulationGameState() {
		reset();
	}
	
	@Override
	public void update(double deltaTime, GameEngine gameEngine) {
		time += deltaTime;
		for (Car car : new HashSet<Car>(activeCars)) {
			car.update(deltaTime);
			if (hasCollided(car)) {
				activeCars.remove(car);
			}
		}
		
		if (time > 30.0 || activeCars.isEmpty()) {
			reset();
		}
	}

	@Override
	public void render(Graphics2D g, Dimension screenSize) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(new Color(128, 128, 128));
		g.fillRect(0, 0, screenSize.width, screenSize.height);

		g.scale(screenSize.getWidth()/world.getSize().getWidth(),
				screenSize.getHeight()/world.getSize().getHeight());
		
		// Draw cars
		for (int i = allCars.size()-1; i >= 0; i--) {
			Polygon car = new Polygon();
			for (Line line : allCars.get(i).asLines()) {
				car.addPoint(line.endPoint1.x, line.endPoint1.y);
			}
			float gradient = ((float) i) / allCars.size();
			g.setColor(new Color(gradient, 1f-gradient, 0f));
			g.fill(car);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(50f));
			g.draw(car);
		}
		
		// Draw obstacles
		g.setStroke(new BasicStroke(100f));
		g.setColor(Color.BLACK);
		for (Line line : world.getObstacles()) {
			g.drawLine(line.endPoint1.x, line.endPoint1.y, line.endPoint2.x, line.endPoint2.y);
		}
	}
	
	private void reset() {
		allCars.clear();
		activeCars.clear();
		time = 0.0;
		
		Evolution evolution = new Evolution(0.1);
		List<NeuralNetwork> neuralNetworks = evolution.generatePopulation(100, 8, 8, 8, 2);
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
}
