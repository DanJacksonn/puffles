package screens;

import handlers.EditorHandler;
import helpers.EditorAssetLoader;
import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.puffles.Puffles;

import entities.impl.Editor;

public class EditorScreen implements Screen, InputProcessor {

	/**
	 * This class controls processes whilst the game is running. -Renders the
	 * world to the screen and processes inputs.
	 */

	private Puffles game;
	private Editor editor;

	// draws world to the screen
	private WorldRenderer renderer;

	// processes inputs and character movement
	private EditorHandler handler;

	public EditorScreen(Puffles game) {
		this.game = game;
		this.editor = new Editor();
	}

	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		handler = new EditorHandler(game.getWorld(), editor);

		// renmakes the controllersder world with editing enabled
		renderer = new WorldRenderer(game.getWorld(), editor);

		// set this screen as current input processor
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// clear and set background
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// update world
		handler.update(delta);

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
		if (keycode == Keys.E) {
			handler.applyEdits();
			// resume game
			game.setScreen(game.getGameScreen());
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// store click location
		float clickX = screenX / renderer.getPpu();
		float clickY = screenY / renderer.getPpu();

		if (inDoneButtonBounds(clickX, clickY)) {
			game.setScreen(game.getGameScreen());
		} else {
			// store click as position on level
			int selectedX = (int) Math.floor(renderer.getCameraPosition().x
					+ clickX);
			int selectedY = (int) Math.floor(renderer.getCameraPosition().y
					+ renderer.getCameraHeight() - clickY);

			handler.placePressed(selectedX, selectedY);
		}
		return true;
	}

	private boolean inDoneButtonBounds(float clickX, float clickY) {
		// store Setting location
		Rectangle doneButtonBounds = EditorAssetLoader.doneButtonBounds;
		float doneButtonLocationX = doneButtonBounds.x;
		float doneButtonLocationY = doneButtonBounds.y;
		float doneButtonWidth = doneButtonBounds.width;
		float doneButtonHeight = doneButtonBounds.height;

		// if settings button clicked
		if (clickX > doneButtonLocationX
				&& clickX < (doneButtonLocationX + doneButtonWidth)
				&& clickY > doneButtonLocationY
				&& clickY < doneButtonLocationY + doneButtonHeight) {
			return true;
		} else {
			return false;
		}
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

}
