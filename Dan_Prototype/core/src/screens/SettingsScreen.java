package screens;

import renderers.SettingsRenderer;
import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.puffles.Puffles;

import entities.Settings;

public class SettingsScreen implements Screen, InputProcessor {
	private Puffles game;
	public SettingsRenderer settingsRenderer;
	public WorldRenderer worldRenderer;
	public SettingsScreen(Puffles game) {
		//this will handle all the inputs for the settings screen
		this.game = game;
	}	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float clickX = screenX / worldRenderer.getPpu();
		float clickY = screenY / worldRenderer.getPpu();
		if(testSettingsButtonBounds(clickX,clickY)){
			
			game.setScreen(game.getGameScreen());
			
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// this is called when the screen is set to this 
		worldRenderer = new WorldRenderer(game.getWorld(), null);
		settingsRenderer = new SettingsRenderer(game.getWorld(), null);
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl20.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//World render for background
		worldRenderer.render();
		settingsRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		worldRenderer.setSize(width, height);
		settingsRenderer.setSize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	public boolean testSettingsButtonBounds(float clickX,float clickY){
		// store Setting location
		Settings settings = game.getWorld().getSettings();
		float settingsButtonLocationX = settings.getButtonBounds().x;
		float settingsButtonLocationY = settings.getButtonBounds().y;
		float settingsButtonWidth = settings.getButtonBounds().width;
		float settingsButtonHeight = settings.getButtonBounds().height;
		// if settings button clicked
					if(clickX > settingsButtonLocationX
						&& clickX < (settingsButtonLocationX + settingsButtonWidth)
						&& clickY > settingsButtonLocationY
						&& clickY < settingsButtonLocationY +  settingsButtonHeight) {
						settings.setIfSettingsOn(false);
						return true;
					}else{
						return false;
					}
	}

}
