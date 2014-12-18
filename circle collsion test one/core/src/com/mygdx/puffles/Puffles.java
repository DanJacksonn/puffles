package com.mygdx.puffles;

import screens.EditorScreen;
import screens.GameScreen;

import com.badlogic.gdx.Game;

public class Puffles extends Game {
	
	public GameScreen gameScreen;
	public EditorScreen editorScreen;
	
	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		editorScreen = new EditorScreen(this);
		setScreen(gameScreen);
	}
}
