package controllers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import entities.Block;
import entities.Editor;
import entities.Inventory;
import entities.Level;
import entities.Puffle;
import entities.World;

public class EditorController {

	/**
	 * This class controls edit mode. -Reacts to particular keys being pressed.
	 */

	enum Keys {
		PLACE
	}

	static Map<Keys, Boolean> keys;

	private Editor editor;

	// world entities
	private Puffle puffle;
	private Level level;
	private Inventory inventory;

	private Vector2 selectedBlock;

	public EditorController(World world, Editor editor) {
		this.puffle = world.getPuffle();
		this.level = world.getLevel();
		this.inventory = world.getInventory();

		this.editor = editor;
		this.selectedBlock = new Vector2();

		keys = new HashMap<EditorController.Keys, Boolean>();
		resetKeys();
	}

	public void resetKeys() {
		keys.put(Keys.PLACE, false);
	}

	public void update(float delta) {
		processInput();
	}

	// Events ----------------
	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Keys.PLACE, true));
		selectedBlock.set(selectedX, selectedY);
	}
	
	// -------------------------

	private boolean processInput() {
		if (keys.get(Keys.PLACE)) {
			if (placeable()) {
				// place block
				editor.placeBlock(new Block(new Vector2(selectedBlock.x,
						selectedBlock.y), 1));
				inventory.removeBlock();
			}
			keys.get(keys.put(Keys.PLACE, false));
		}
		return false;
	}
	
	public void applyEdits() {
		// switch back to game screen
		resetKeys();
		
		// add placed blocks to the world
		level.addBlocks(editor.getPlacedBlocks());
		editor.clearPlacedBlocks();
	}

	/** True if block can be placed in the currently selected block */
	private boolean placeable() {
		// puffle in the way
		Rectangle selectedBounds = new Rectangle(selectedBlock.x, selectedBlock.y,
				Block.SIZE, Block.SIZE);
		if (Intersector.overlaps(puffle.getBounds(), selectedBounds)) {
			return false;
		}
		// already placed a block here
		if (editor.isBlockPlacedAt(selectedBlock)) {
			return false;
		}
		if (!level.isEmpty(selectedBlock)) {
			return false;
		}
		// no blocks in inventory
		if (inventory.isEmpty()) {
			return false;
		}
		return true;
	}

}
