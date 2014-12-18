package controllers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.puffles.Puffles;

import entities.Block;
import entities.Editor;
import entities.Inventory;
import entities.Level;
import entities.World;

public class EditorController {

	/**
	 * This class controls edit mode. -Reacts to particular keys being pressed.
	 */

	enum Keys {
		PLACE, BACK
	}

	Puffles game;
	private World world;
	private Editor editor;
	
	// world entities
	private Level level;
	private Inventory inventory;
	
	private Rectangle pufflePosition;
	Vector2 selectedBlock;

	static Map<Keys, Boolean> keys = new HashMap<EditorController.Keys, Boolean>();

	public EditorController(Puffles game, World world, Editor editor) {
		this.game = game;
		this.world = world;
		this.level = world.getLevel();
		this.inventory = world.getInventory();
		this.editor = editor;
		
		pufflePosition = world.getPuffle().getBounds();
		selectedBlock = new Vector2();
		resetKeys();
	}

	private void resetKeys() {
		keys.put(Keys.PLACE, false);
		keys.put(Keys.BACK, false);
	}

	public void update(float delta) {
		processInput();

		// do things depending on input

	}

	// Events ----------------
	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Keys.PLACE, true));
		selectedBlock.set(selectedX, selectedY);
	}

	public void backPressed() {
		keys.get(keys.put(Keys.BACK, true));
	}

	// -------------------------

	private boolean processInput() {
		if (keys.get(Keys.PLACE)) {
			if (placeable()) {
				// place block
				editor.placeBlock(new Block(new Vector2(selectedBlock.x,
						selectedBlock.y), 1, false));
				inventory.removeBlock();
			}
			keys.get(keys.put(Keys.PLACE, false));
		} else if (keys.get(Keys.BACK)) {
			// switch back to game screen
			resetKeys();
			// add placed blocks to the world
			level.addBlocks(editor.getPlacedBlocks());
			clearPlacedBlocks();
			// update the game world
			game.gameScreen.updateWorld(world);
			// resume game
			game.setScreen(game.gameScreen);
		}
		return false;
	}
	
	private void clearPlacedBlocks() {
		editor.clearPlacedBlocks();
	}

	/** True if block can be placed in the currently selected block */
	private boolean placeable() {
		// already a block here
		if (!level.isEmpty(selectedBlock))
			return false;
		// no blocks in inventory
		if (inventory.isEmpty())
			return false;
		// already placed a block here
		if (editor.isBlockPlacedAt(selectedBlock))
			return false;
		// puffle in the way
		Rectangle selectedBounds = new Rectangle(selectedBlock.x,
				selectedBlock.y, Block.SIZE, Block.SIZE);
		if (pufflePosition.overlaps(selectedBounds))
			return false;
		return true;
	}
}
