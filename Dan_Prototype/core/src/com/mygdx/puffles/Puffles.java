package com.mygdx.puffles;

import com.badlogic.gdx.Game;

import helpers.EditorAssetLoader;
import helpers.WorldAssetLoader;
import screens.GameScreen;
import screens.MenuScreen;

public class Puffles extends Game {

	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	
	@Override
	public void create () {
		WorldAssetLoader.Load();
		EditorAssetLoader.Load();
		this.gameScreen = new GameScreen(this);
		this.menuScreen = new MenuScreen(this);
		this.setScreen(new GameScreen(this));
	}
	
	public void render() {
		super.render();
	}
	
	public void dispose() {
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
	public MenuScreen getMenuScreen() {
		return menuScreen;
	}
	

}
