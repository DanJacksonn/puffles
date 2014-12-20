package com.mygdx.puffles;

import com.badlogic.gdx.Game;

import screens.EditorScreen;
import screens.GameScreen;
import entities.World;

public class Puffles extends Game {

	private World world;
	private GameScreen gameScreen;
	private EditorScreen editorScreen;
	
	@Override
	public void create () {
		this.world = new World();
		this.gameScreen = new GameScreen(this);
		this.editorScreen = new EditorScreen(this);
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

}
