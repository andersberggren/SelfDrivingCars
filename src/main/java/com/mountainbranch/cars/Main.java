package com.mountainbranch.cars;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import com.mountainbranch.gameframework.builder.GameBuilder;
import com.mountainbranch.gui.Settings;

public class Main {

	public static void main(String[] args) {
		final Settings settings = new Settings();
		new GameBuilder(new SimulationGameState(settings), 60, false, null).startGame();
		createSettingsFrame(settings);
	}
	
	public static void createSettingsFrame(final Settings settings) {
		JFrame settingsFrame = new JFrame("Settings");
		settingsFrame.setLayout(new GridLayout(0, 1));
		
		final JCheckBox showSensorsCheckBox = new JCheckBox("Show sensors");
		showSensorsCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setShowSensors(showSensorsCheckBox.isSelected());
			}});
		settingsFrame.add(showSensorsCheckBox);
		
		JButton skipButton = new JButton("Skip");
		skipButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setSkip();
			}});
		settingsFrame.add(skipButton);
		
		settingsFrame.pack();
		settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsFrame.setVisible(true);
	}
}
