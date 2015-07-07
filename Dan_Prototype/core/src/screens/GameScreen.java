package screens;

import handlers.PuffleHandler;
import helpers.WorldAssetLoader;
import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.puffles.Puffles;

import entities.impl.Inventory;

public class GameScreen implements Screen, InputProcessor {

	/**
	 * This class renders the world to the screen and processes inputs.
	 */

	private Puffles game;
	
	private Inventory inventory;

	// processes inputs and character movement
	private PuffleHandler handler;

	// draws world to the screen
	private WorldRenderer renderer;

	public GameScreen(Puffles game) {
		this.game = game;
		this.inventory = game.getWorld().getInventory();
	}

	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		handler = new PuffleHandler(game.getWorld());

		// render world with editing disabled
		renderer = new WorldRenderer(game.getWorld(), null);

		// set this screen as current input processor
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
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
		if (keycode == Keys.A || keycode == Keys.LEFT)
			handler.leftPressed();
		if (keycode == Keys.D || keycode == Keys.RIGHT)
			handler.rightPressed();
		if (keycode == Keys.W || keycode == Keys.UP)
			handler.jumpPressed();
		if (keycode == Keys.E) {
			handler.resetKeys();
			game.setScreen(game.getEditorScreen());
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// keyboard key released
		if (keycode == Keys.A || keycode == Keys.LEFT)
			handler.leftReleased();
		if (keycode == Keys.D || keycode == Keys.RIGHT)
			handler.rightReleased();
		if (keycode == Keys.W || keycode == Keys.UP)
			handler.jumpReleased();
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

		if (inInventoryBounds(clickX, clickY)) {
			// switch to editor more
			game.setScreen(game.getEditorScreen());
		} else if (inSettingsButtonBounds(clickX, clickY)) {
			// go to settings menu screen
			game.setScreen(game.getSettingScreen());
		} else {
			// jump!
			handler.jumpPressed();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// screen not being touched- stop jumping
		handler.jumpReleased();
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

	public boolean inInventoryBounds(float clickX, float clickY) {
		// store inventory location
		Rectangle invBounds = WorldAssetLoader.invBounds;

		// if inventory clicked
		if (!inventory.isEmpty() && clickX > invBounds.x
				&& clickX < (invBounds.x + invBounds.width)
				&& clickY > invBounds.y
				&& clickY < invBounds.y + invBounds.height) {
			return true;
		} else {
			return false;
		}
	}

	public boolean inSettingsButtonBounds(float clickX, float clickY) {
		// store Setting location
		Rectangle settingsBounds = WorldAssetLoader.settingsBounds;
		float settingsButtonLocationX = settingsBounds.x;
		float settingsButtonLocationY = settingsBounds.y;
		float settingsButtonWidth = settingsBounds.width;
		float settingsButtonHeight = settingsBounds.height;

		// if settings button clicked
		if (clickX > settingsButtonLocationX
				&& clickX < (settingsButtonLocationX + settingsButtonWidth)
				&& clickY > settingsButtonLocationY
				&& clickY < settingsButtonLocationY + settingsButtonHeight) {
			return true;
		} else {
			return false;
		}
	}

}