package controllers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.puffles.Puffles;

import entities.Block;
import entities.Editor;
import entities.Inventory;
import entities.World;

public class EditorController {

	/**
	 * This class controls the puffle's movement.
	 * -Reacts to particular keys being pressed.
	 */
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN, PLACE, BACK
	}

	Puffles game;
	private World world;
	private Inventory inventory;
	private Editor editor;
	
	Vector2 selectedBlock;

	static Map<Keys, Boolean> keys = new HashMap<EditorController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.PLACE, false);
		keys.put(Keys.BACK, false);
	};

	public EditorController(Puffles game, World world, Editor editor) {
		this.game = game;
		this.world = world;
		this.inventory = world.getInventory();
		this.editor = editor;
		selectedBlock = new Vector2();
	}
	
	public void update(float delta) {
		processInput();
		
		// do things depending on input
		
	}

	// Events ----------------
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}

	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}
	
	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Keys.PLACE, true));
		selectedBlock.set(selectedX, selectedY);
	}
	
	public void backPressed() {
		keys.get(keys.put(Keys.BACK, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}
	
	// -------------------------

	private boolean processInput() {
		// if left key is pressed
		if (keys.get(Keys.PLACE)) {
			if (placeable()) {
				// place block
				editor.placeBlock(new Block(new Vector2(selectedBlock.x, selectedBlock.y), 1, false));
				inventory.removeBlock();
			}
			keys.get(keys.put(Keys.PLACE, false));
		} else if (keys.get(Keys.BACK)) {
			keys.get(keys.put(Keys.BACK, false));
			// add placed blocks to the world
			world.addBlocks(editor.getPlacedBlocks());
			// update the game world
			game.gameScreen.updateWorld(world);
			// resume game
			game.setScreen(game.gameScreen);
		}
		return false;
	}
	
	private boolean placeable() {
		if (!world.getLevel().isEmpty(selectedBlock)) return false;
		if (inventory.isEmpty()) return false;
		if (editor.isBlockPlacedAt(selectedBlock)) return false;
		return true;
	}
	
}
