package com.mountainbranch.gui;

public class Settings {
	private boolean fastForward = false;
	private boolean showSensors = true;
	
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
}
