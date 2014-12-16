package com.mygdx.puffles;

import screens.GameScreen;

import com.badlogic.gdx.Game;

public class Puffles extends Game {
	
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
