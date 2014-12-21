package screens;

import renderers.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.puffles.Puffles;

import controllers.EditorController;
import entities.Editor;
import entities.Inventory;
import entities.World;

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
	private EditorController controller;

	public EditorScreen(Puffles game) {
		this.game = game;
		this.editor = new Editor();
	}

	@Override
	/** Called when this screen becomes the current screen for the game **/
	public void show() {
		controller = new EditorController(game.getWorld(), editor);

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
		if (keycode == Keys.E) {
			controller.applyEdits();
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

		// store inventory location
		Inventory inventory = game.getWorld().getInventory();
		float inventoryLocationX = inventory.getBounds().x;
		float inventoryLocationY = inventory.getBounds().y;
		float inventoryWidth = inventory.getBounds().width;
		float inventoryHeight = inventory.getBounds().height;

		// if inventory clicked
		if (!inventory.isEmpty()
				&& clickX > inventoryLocationX
				&& clickX < (inventoryLocationX + inventoryWidth)
				&& clickY > inventoryLocationY
				&& clickY < inventoryLocationY + inventoryHeight) {
			// switch to editor more
			game.setScreen(game.getGameScreen());
		} else {
			int selectedX = (int) Math.floor(renderer.getCameraPosition().x + clickX);
			int selectedY = (int) Math
					.floor(renderer.getCameraPosition().y + renderer.getCameraHeight() - clickY);
			controller.placePressed(selectedX, selectedY);
		}
		return true;
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
