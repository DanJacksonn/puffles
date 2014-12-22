package com.mygdx.puffles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import screens.EditorScreen;
import screens.GameScreen;
import screens.SettingsScreen;
import entities.Settings;
import entities.World;

public class Puffles extends Game {

	private World world;
	private GameScreen gameScreen;
	private EditorScreen editorScreen;
	private SettingsScreen settingsScreen;
	private Settings settings = new Settings();
	
	@Override
	public void create () {
		this.world = new World(settings);
		this.gameScreen = new GameScreen(this);
		this.editorScreen = new EditorScreen(this);
		this.settingsScreen = new SettingsScreen(this);
		setScreen(gameScreen);
	}
	
	public World getWorld() {
		return world;
	}
	public Settings getSettings() {
		return settings;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
	public EditorScreen getEditorScreen() {
		return editorScreen;
	}


	public Screen getSettingScreen() {
		return settingsScreen;
	}
	

}
