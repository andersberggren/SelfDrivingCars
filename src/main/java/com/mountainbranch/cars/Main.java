package com.mountainbranch.cars;

import com.mountainbranch.gameframework.builder.GameBuilder;
import com.mountainbranch.gui.Settings;
import com.mountainbranch.gui.SettingsFrame;

public class Main {

	public static void main(String[] args) {
		final Settings settings = new Settings();
		new GameBuilder(new SimulationGameState(settings), 60, false, null).startGame();
		new SettingsFrame(settings);
	}
}
