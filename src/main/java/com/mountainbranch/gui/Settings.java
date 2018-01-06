package com.mountainbranch.gui;

public class Settings {
	private boolean skip = false;
	private boolean showSensors = false;
	
	public synchronized void setSkip() {
		skip = true;
	}
	
	public synchronized boolean getSkip() {
		boolean prevValue = skip;
		skip = false;
		return prevValue;
	}
	
	public void setShowSensors(boolean value) {
		showSensors = value;
	}
	
	public boolean getShowSensors() {
		return showSensors;
	}
}
