package com.mountainbranch.cars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Set;

import com.mountainbranch.gui.Settings;
import com.mountainbranch.ze.geom.Line;

public class SimulationGameStateRenderer {
	private static final Color COLOR_BACKGROUND = new Color(64, 64, 64);
	private static final Color COLOR_SENSOR = new Color(255, 192, 0, 64);
	private static final Color COLOR_CAR_OUTLINE = Color.BLACK;
	private static final Color COLOR_OBSTACLE = Color.LIGHT_GRAY;

	public void render(Graphics2D g, Dimension screenSize, World world, Settings settings,
			List<Car> allCars, Set<Car> activeCars, int generation) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(0, 0, screenSize.width, screenSize.height);
		g.scale(screenSize.getWidth()/world.getSize().getWidth(),
				screenSize.getHeight()/world.getSize().getHeight());
		
		// Draw goal
		Rectangle goal = world.getGoal();
		int numberOfSquares = 6;
		int squareWidth = goal.width / numberOfSquares;
		int squareHeight = goal.height / numberOfSquares;
		for (int y = 0; y < numberOfSquares; y++) {
			for (int x = 0; x < numberOfSquares; x++) {
				g.setColor((x+y)%2==0 ? Color.WHITE : Color.BLACK);
				g.fillRect(goal.x+squareWidth*x, goal.y+squareHeight*y,
						squareWidth, squareHeight);
			}
		}
		
		// Draw car sensors
		g.setColor(COLOR_SENSOR);
		g.setStroke(new BasicStroke(100f));
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
			if (generation == 1) {
				g.setColor(new Color(0f, 0.5f, 0f));
			} else {
				g.setColor(new Color(0f, 1f-gradient, 0f));
			}
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
}
