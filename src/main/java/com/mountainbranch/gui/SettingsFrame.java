package com.mountainbranch.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class SettingsFrame implements SettingsListener {
	private JLabel generationLabel;
	
	public SettingsFrame(final Settings settings) {
		JFrame settingsFrame = new JFrame("Settings");
		settingsFrame.setLayout(new GridLayout(0, 1));
		
		generationLabel = new JLabel();
		updateGenerationLabelText(settings);
		generationLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
		settingsFrame.add(generationLabel);
		final JCheckBox showSensorsCheckBox = new JCheckBox("Show sensors");
		showSensorsCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setShowSensors(showSensorsCheckBox.isSelected());
			}});
		showSensorsCheckBox.setSelected(settings.getShowSensors());
		settingsFrame.add(showSensorsCheckBox);
		
		JButton fastForwardButton = new JButton("Fast-forward to next generation");
		fastForwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setFastForward();
			}});
		settingsFrame.add(fastForwardButton);
		
		settingsFrame.pack();
		settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsFrame.setVisible(true);
		settings.addListener(this);
	}

	@Override
	public void onUpdate(Settings settings) {
		updateGenerationLabelText(settings);
	}
	
	private void updateGenerationLabelText(Settings settings) {
		generationLabel.setText("Generation: " + settings.getGeneration());
		generationLabel.repaint();
	}
}
