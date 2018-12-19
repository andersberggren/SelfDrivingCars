package com.mountainbranch.cars;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mountainbranch.gameframework.core.GameEngine;
import com.mountainbranch.gameframework.core.GameState;
import com.mountainbranch.gui.Settings;
import com.mountainbranch.neuralnetwork.Evolution;
import com.mountainbranch.neuralnetwork.NeuralNetwork;
import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class SimulationGameState implements GameState {
	private final Settings settings;
	private World world = new World2();
	private SimulationGameStateRenderer renderer = new SimulationGameStateRenderer();
	private FitnessEvaluator fitnessEvaluator = new FitnessEvaluator(world);
	private List<Car> allCars = new ArrayList<Car>();
	private Set<Car> activeCars = new HashSet<Car>();
	private double time;
	private int generation = 0;
	
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
		Collection<Line> obstacles = world.getObstacles();
		for (Car car : new LinkedList<Car>(activeCars)) {
			car.update(deltaTime, obstacles);
			fitnessEvaluator.updateFitness(car, time);
			if (hasCollided(car) || hasFinished(car)) {
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
		renderer.render(g, screenSize, world, settings, allCars, activeCars, generation);
	}
	
	private void reset() {
		System.out.println("Best fitness: " + Debug.bestFitness);
		List<NeuralNetwork> neuralNetworks;
		if (allCars.isEmpty()) {
			// This is the first generation
			int numberOfIndividuals = 1+1+2+3+4+5+6+7+8+9+10;
			int[] neuronsPerLayer = new int[3];
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
			fitnessEvaluator.sortOnFitness(allCars);
			List<NeuralNetwork> currentGeneration = new LinkedList<NeuralNetwork>();
			for (Car car : allCars) {
				currentGeneration.add(car.getNeuralNetwork());
			}
			Evolution evolution = new Evolution(0.05);
			neuralNetworks = evolution.generateNextGeneration(currentGeneration);
		}
		
		allCars.clear();
		activeCars.clear();
		fitnessEvaluator.resetFitness();
		time = 0.0;
		
		Point startLocation = world.getStartLocation();
		double startAngle = world.getStartAngle();
		for (NeuralNetwork nn : neuralNetworks) {
			allCars.add(new Car(nn, startLocation, startAngle));
		}
		activeCars.addAll(allCars);
		settings.setGeneration(++generation);
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
	
	private boolean hasFinished(Car car) {
		return world.getGoal().contains(car.getLocation());
	}
}
