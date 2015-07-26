package screens;

import handlers.EditorHandler;
import handlers.PuffleHandler;
import helpers.EditorAssetLoader;
import helpers.WorldAssetLoader;
import renderers.GameRenderer;
import resources.Bounds;
import resources.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.mygdx.puffles.Puffles;

import entities.impl.Editor;
import entities.impl.World;

public class GameScreen implements Screen, InputProcessor {

	/**
	 * This class renders the world to the screen and processes inputs.
	 */

	private Puffles game;
	private GameState state;
	
	private World world;
	private Editor editor;
	private PuffleHandler puffleHandler;
	private EditorHandler editorHandler;
	private GameRenderer renderer;

	public GameScreen(final Puffles game) {
		this.game = game;
		this.world = new World();
		this.editor = new Editor();
		this.state = GameState.RUNNING;
		this.puffleHandler = new PuffleHandler(world);
		this.editorHandler = new EditorHandler(world, editor);
		this.renderer = new GameRenderer(world, editor);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		switch (state) {
		case RUNNING:
			puffleHandler.update(delta);
			break;
		case EDITING:
			editorHandler.update(delta);
			break;
		default:
			break;
		}
		renderer.render(state);
	}

	@Override
	public void resize(int width, int height) {
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
		switch (state) {
		case RUNNING:
			keyDownRunning(keycode);
			break;
		case EDITING:
			keyDownEditing(keycode);
			break;
		case PAUSED:
			break;
		default:
			break;
		}
		return true;
	}

	private void keyDownRunning(int keycode) {
		switch (keycode) {
		case Keys.A:
		case Keys.LEFT:
				puffleHandler.leftPressed();
			break;
		case Keys.D:
		case Keys.RIGHT:
				puffleHandler.rightPressed();
			break;
		case Keys.W:
		case Keys.UP:
				puffleHandler.upPressed();
			break;
		case Keys.E:
			puffleHandler.resetKeys();
			state = GameState.EDITING;
			break;
		}
	}
	
	private void keyDownEditing(int keycode) {
		switch(keycode) {
		case Keys.E:
			world.level.addBlocks(editor.getPlacedBlocks());
			editor.clearPlacedBlocks();
			state = GameState.RUNNING;
			editorHandler.resetKeys();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.A:
		case Keys.LEFT:
				puffleHandler.upReleased();
			break;
		case Keys.D:
		case Keys.RIGHT:
				puffleHandler.rightReleased();
			break;
		case Keys.W:
		case Keys.UP:
				puffleHandler.upReleased();
			break;
		}
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
		float clickX = screenX / ppu;
		float clickY = screenY / ppu;
		switch (state) {
		case RUNNING:
			clickedWhilstRunning(clickX, clickY);
			break;
		case EDITING:
			clickedWhilstEditing(clickX, clickY);
			break;
		default:
			break;
		}
		return true;
	}

	private void clickedWhilstRunning(float clickX, float clickY) {
		if (clickedOnInventoryButton(clickX, clickY)) {
			 state = GameState.EDITING;
		} else if (clickedOnSettingsButton(clickX, clickY)) {
			 game.setScreen(game.getMenuScreen());
		} else {
			puffleHandler.upPressed();
		}
	}
	
	public boolean clickedOnInventoryButton(float clickX, float clickY) {
		Bounds invBounds = WorldAssetLoader.invBounds;
		return !world.inventory.isEmpty() && invBounds.isWithinBounds(clickX, clickY);
	}
	
	public boolean clickedOnSettingsButton(float clickX, float clickY) {
		Bounds settingsBounds = WorldAssetLoader.settingsBounds;
		return settingsBounds.isWithinBounds(clickX, clickY);
	}

	private void clickedWhilstEditing(float clickX, float clickY) {
		if (inDoneButtonBounds(clickX, clickY)) {
			state = GameState.RUNNING;
		} else {
			int selectedX = (int) Math.floor(renderer.getCameraPosition().x
					+ clickX);
			int selectedY = (int) Math.floor(renderer.getCameraPosition().y
					+ (renderer.getCameraHeight() - clickY));
			editorHandler.placePressed(selectedX, selectedY);
		}
	}
	
	private boolean inDoneButtonBounds(float clickX, float clickY) {
		Bounds doneButtonBounds = EditorAssetLoader.doneButtonBounds;
		return doneButtonBounds.isWithinBounds(clickX, clickY);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (state) {
		case RUNNING:
			puffleHandler.upReleased();
			break;
		default:
			break;
		}
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