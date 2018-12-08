package com.mountainbranch.cars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
			List<Car> allCars, Set<Car> activeCars) {
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
}
