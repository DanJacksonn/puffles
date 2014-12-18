package screens;

import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.puffles.Puffles;

import controllers.EditorController;
import entities.World;

public class EditorScreen implements Screen, InputProcessor {

	/**
	 * This class controls processes whilst the game is running. 
	 * -Renders the world to the screen and processes inputs.
	 */

	Puffles puffles;
	private World world;
	
	// draws world to the screen
	private WorldRenderer renderer;
	
	// processes inputs and character movement
	private EditorController controller;

	public EditorScreen(Puffles game) {
		this.puffles = game;
		this.world = new World();
	}
	
	public void updateWorld(World world) {
		this.world = world;
	}
	
	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		System.out.println("EDITING MODE");
		renderer = new WorldRenderer(world, true);
		controller = new EditorController(world);
		// set this screen as current input processor
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// clear and set background
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// update world
		controller.update(delta);

		// draw world to screen
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// screen size changed
		renderer.setSize(width, height);
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
		Gdx.input.setInputProcessor(null);

	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public boolean keyDown(int keycode) {
		// keyboard key pressed
		if (keycode == Keys.LEFT)
			controller.leftPressed();
		if (keycode == Keys.RIGHT)
			controller.rightPressed();
		if (keycode == Keys.UP)
			controller.upPressed();
		if (keycode == Keys.DOWN)
			controller.downPressed();
		if (keycode == Keys.B)
			controller.backPressed();
		if (keycode == Keys.E) {
			puffles.gameScreen.updateWorld(world);
			puffles.setScreen(puffles.gameScreen);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// keyboard key released
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		if (keycode == Keys.UP)
			controller.upReleased();
		if (keycode == Keys.DOWN)
			controller.downReleased();
		if (keycode == Keys.B)
			controller.backReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float ppu = renderer.getPpu();
		int selectedX = (int)Math.floor(screenX / ppu);	
		int selectedY = (int)Math.floor((renderer.getScreenHeight() - screenY) / ppu);
		controller.placePressed(selectedX, selectedY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		controller.placeReleased();
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

}

