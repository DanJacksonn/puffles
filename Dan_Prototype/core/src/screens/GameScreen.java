package screens;

import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.puffles.Puffles;

import controllers.PuffleController;
import entities.Editor;
import entities.Level;
import entities.World;

public class GameScreen implements Screen, InputProcessor {

	/**
	 * This class controls processes whilst the game is running. 
	 * -Renders the world to the screen and processes inputs.
	 */
	
	Puffles game;
	private World world;
	
	// draws world to the screen
	private WorldRenderer renderer;
	
	// processes inputs and character movement
	private PuffleController controller;

	public GameScreen(Puffles puffles) {
		this.game = puffles;
		this.world = new World();
	}
	
	public void updateWorld(World world) {
		this.world = world;
	}
	
	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		renderer = new WorldRenderer(world, null);
		controller = new PuffleController(game, world);
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
		if (keycode == Keys.A)
			controller.leftPressed();
		if (keycode == Keys.D)
			controller.rightPressed();
		if (keycode == Keys.W)
			controller.jumpPressed();
		if (keycode == Keys.E) {
			controller.editPressed(game);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// keyboard key released
		if (keycode == Keys.A)
			controller.leftReleased();
		if (keycode == Keys.D)
			controller.rightReleased();
		if (keycode == Keys.W)
			controller.jumpReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// screen touched- jump!
		controller.jumpPressed();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// screen not being touched- stop jumping
		controller.jumpReleased();
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
