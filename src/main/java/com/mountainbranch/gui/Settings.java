package com.mountainbranch.gui;

public class Settings {
	private boolean skip = false;
	
	public synchronized void setSkip() {
		skip = true;
	}
	
	public synchronized boolean getSkip() {
		boolean prevValue = skip;
		skip = false;
		return prevValue;
	}
}
