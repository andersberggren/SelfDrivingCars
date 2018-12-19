package com.mountainbranch.cars;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mountainbranch.ze.geom.GeometryUtils;
import com.mountainbranch.ze.geom.Line;

public class FitnessEvaluator {
	private final Map<Car, Integer> carToFitness = new HashMap<Car, Integer>();
	private final Map<Car, Double> carToDistance = new HashMap<Car, Double>();
	private final World world;
	
	public FitnessEvaluator(World world) {
		this.world = world;
	}
	
	public void sortOnFitness(List<Car> cars) {
		Collections.sort(cars, new CarComparator());
		Collections.reverse(cars);
	}
	
	public void updateFitness(Car car, double time) {
		Point center = new Point(world.getSize().width/2, world.getSize().height/2);
		Point bottomRight = new Point(world.getSize().width, world.getSize().height);
		Line midwayLine = new Line(center, bottomRight);
		Line carLine = new Line(center, car.getLocation());
		double angleRadians = GeometryUtils.getAngle(midwayLine, carLine);
		//updateDistance(car);
		double fitness = angleRadians;
		if (world.getGoal().contains(car.getLocation())) {
			double timeBonus = 1.0/time;
			//double distanceBonus = carToDistance.get(car);
			fitness = Math.round(Math.PI+1.0) + timeBonus;
		}
		Integer fitnessAsInt = (int) (fitness * 1000000.0);
		Integer maxFitnessSoFar = carToFitness.get(car);
		if (maxFitnessSoFar == null || fitnessAsInt > maxFitnessSoFar) {
			carToFitness.put(car, fitnessAsInt);
		}
		Debug.bestFitness = Math.max(Debug.bestFitness, fitnessAsInt);
	}
	
	public void resetFitness() {
		carToFitness.clear();
		Debug.bestFitness = -Integer.MIN_VALUE;
	}
	
	@SuppressWarnings("unused")
	private void updateDistance(Car car) {
		Collection<Line> carLines = car.asLines();
		Collection<Line> obstacles = world.getObstacles();
		double distance = Double.MAX_VALUE;
		for (Line line1 : carLines) {
			for (Line line2 : obstacles) {
				distance = Math.min(distance,
						GeometryUtils.getShortestDistanceFromLineToPoint(line1, line2.endPoint1));
				distance = Math.min(distance,
						GeometryUtils.getShortestDistanceFromLineToPoint(line1, line2.endPoint2));
				distance = Math.min(distance,
						GeometryUtils.getShortestDistanceFromLineToPoint(line2, line1.endPoint1));
				distance = Math.min(distance,
						GeometryUtils.getShortestDistanceFromLineToPoint(line2, line1.endPoint2));
			}
		}
		Double currentClosestDistance = carToDistance.get(car);
		if (currentClosestDistance == null || distance < currentClosestDistance) {
			carToDistance.put(car, (Double) distance);
		}
	}
	
	private class CarComparator implements Comparator<Car> {
		@Override
		public int compare(Car o1, Car o2) {
			Integer fitness1 = carToFitness.get(o1);
			if (fitness1 == null) {
				fitness1 = 0;
			}
			Integer fitness2 = carToFitness.get(o2);
			if (fitness2 == null) {
				fitness2 = 0;
			}
			return (int) ((fitness1 - fitness2) * 1000.0);
		}
		
	}
}
