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
		updateDistance(car);
		double fitness = angleRadians;
		if (hasFinished(car)) {
			double timeBonus = 1.0/time;
			double distanceBonus = carToDistance.get(car);
			fitness = Math.PI + timeBonus * distanceBonus;
			System.out.println("Car has reached goal! Fitness bonus: " + (timeBonus*distanceBonus));
		}
		Integer fitnessAsInt = (int) (fitness * 1000.0);
		Integer maxFitnessSoFar = carToFitness.get(car);
		if (maxFitnessSoFar == null || fitnessAsInt > maxFitnessSoFar) {
			carToFitness.put(car, fitnessAsInt);
		}
	}
	
	public void resetFitness() {
		carToFitness.clear();
	}
	
	public boolean hasFinished(Car car) {
		return car.getLocation().x < 10000 && car.getLocation().y < world.getSize().height/2;
	}
	
	private void updateDistance(Car car) {
		Collection<Line> carLines = car.asLines();
		Collection<Line> obstacles = world.getObstacles();
		double distance = world.getSize().width + world.getSize().height;
		for (Line carLine : carLines) {
			for (Line obstacle : obstacles) {
				distance = Math.min(distance, getDistanceBetweenLines(carLine, obstacle));
			}
		}
		Double currentClosestDistance = carToDistance.get(car);
		if (currentClosestDistance == null || distance < currentClosestDistance) {
			carToDistance.put(car, (Double) distance);
		}
	}
	
	private double getDistanceBetweenLines(Line line1, Line line2) {
		Point v1 = GeometryUtils.getShortestVectorFromLineToPoint(line1, line2.endPoint1);
		Point v2 = GeometryUtils.getShortestVectorFromLineToPoint(line1, line2.endPoint2);
		Point v3 = GeometryUtils.getShortestVectorFromLineToPoint(line2, line1.endPoint1);
		Point v4 = GeometryUtils.getShortestVectorFromLineToPoint(line2, line1.endPoint2);
		Double shortestDistance = null;
		for (Point vector : new Point[]{v1, v2, v3, v4}) {
			double distanceToThisPoint = GeometryUtils.getLength(vector);
			if (shortestDistance == null || distanceToThisPoint < shortestDistance) {
				shortestDistance = distanceToThisPoint;
			}
		}
		return shortestDistance;
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
