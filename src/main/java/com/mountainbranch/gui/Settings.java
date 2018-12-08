package com.mountainbranch.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

public class Settings {
	private int generation = 1;
	private boolean fastForward = false;
	private boolean showSensors = true;
	private List<SettingsListener> listeners = new LinkedList<SettingsListener>();

	public void setGeneration(int generation) {
		this.generation = generation;
		notifyListeners();
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public synchronized void setFastForward() {
		fastForward = true;
	}
	
	public synchronized boolean getFastForward() {
		boolean prevValue = fastForward;
		fastForward = false;
		return prevValue;
	}
	
	public void setShowSensors(boolean value) {
		showSensors = value;
	}
	
	public boolean getShowSensors() {
		return showSensors;
	}
	
	public void addListener(SettingsListener listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for (SettingsListener listener: listeners) {
			listener.onUpdate(this);
		}
	}
}
