package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.puffles.Puffles;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 900;
		config.height = 500;
		config.resizable = false;
		config.samples = 8;
		config.title = "Puffles";
		new LwjglApplication(new Puffles(), config);
	}
}