package com.mygdx.puffles;

import helpers.EditorAssetLoader;
import helpers.WorldAssetLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import screens.EditorScreen;
import screens.GameScreen;
import screens.SettingsScreen;
import entities.impl.World;

public class Puffles extends Game {

	private World world;
	
	// screens
	private GameScreen gameScreen;
	private EditorScreen editorScreen;
	private SettingsScreen settingsScreen;
	
	@Override
	public void create () {
		// load world
		WorldAssetLoader.Load();
		EditorAssetLoader.Load();
		this.world = new World();
		
		// load screens
		this.gameScreen = new GameScreen(this);
		this.editorScreen = new EditorScreen(this);
		this.settingsScreen = new SettingsScreen(this);
		setScreen(gameScreen);
	}
	
	public World getWorld() {
		return world;
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
