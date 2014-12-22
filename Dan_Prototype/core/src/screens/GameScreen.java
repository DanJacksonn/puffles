package screens;

import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.puffles.Puffles;

import controllers.PuffleController;
import entities.Inventory;
import entities.Settings;

public class GameScreen implements Screen, InputProcessor {

	/**
	 * This class renders the world to the screen and processes inputs.
	 */

	private Puffles game;

	// processes inputs and character movement
	private PuffleController controller;

	// draws world to the screen
	private WorldRenderer renderer;

	public GameScreen(Puffles game) {
		this.game = game;
	}

	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		controller = new PuffleController(game.getWorld());

		// render world with editing disabled
		renderer = new WorldRenderer(game.getWorld(), null);

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
			controller.resetKeys();
			game.setScreen(game.getEditorScreen());
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
			controller.jumpPressed();
		}
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

	public boolean inInventoryBounds(float clickX, float clickY) {
		// store inventory location
		Inventory inventory = game.getWorld().getInventory();
		float inventoryLocationX = inventory.getBounds().x;
		float inventoryLocationY = inventory.getBounds().y;
		float inventoryWidth = inventory.getBounds().width;
		float inventoryHeight = inventory.getBounds().height;

		// if inventory clicked
		if (!inventory.isEmpty() && clickX > inventoryLocationX
				&& clickX < (inventoryLocationX + inventoryWidth)
				&& clickY > inventoryLocationY
				&& clickY < inventoryLocationY + inventoryHeight) {
			return true;
		} else {
			return false;
		}
	}

	public boolean inSettingsButtonBounds(float clickX, float clickY) {
		// store Setting location
		Settings settings = game.getWorld().getSettings();
		float settingsButtonLocationX = settings.getButtonBounds().x;
		float settingsButtonLocationY = settings.getButtonBounds().y;
		float settingsButtonWidth = settings.getButtonBounds().width;
		float settingsButtonHeight = settings.getButtonBounds().height;

		// if settings button clicked
		if (clickX > settingsButtonLocationX
				&& clickX < (settingsButtonLocationX + settingsButtonWidth)
				&& clickY > settingsButtonLocationY
				&& clickY < settingsButtonLocationY + settingsButtonHeight) {
			// just used to change box fill type in the world render to allow
			// for testing
			settings.setIfSettingsOn(true);
			return true;
		} else {
			return false;
		}
	}

}