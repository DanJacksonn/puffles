package handlers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import entities.api.IBlock;
import entities.impl.Block;
import entities.impl.Editor;
import entities.impl.Inventory;
import entities.impl.Level;
import entities.impl.Puffle;
import entities.impl.TilePosition;
import entities.impl.World;

public class EditorHandler {

	/**
	 * This class controls edit mode. -Reacts to particular keys being pressed.
	 */

	enum Inputs {
		CLICK
	}

	static Map<Inputs, Boolean> keys;

	private Editor editor;

	// world entities
	private Puffle puffle;
	private Level level;
	private Inventory inventory;

	private TilePosition selectedBlock;

	public EditorHandler(World world, Editor editor) {
		this.puffle = world.getPuffle();
		this.level = world.getLevel();
		this.inventory = world.getInventory();
		this.selectedBlock = new TilePosition();

		this.editor = editor;

		keys = new HashMap<EditorHandler.Inputs, Boolean>();
		resetKeys();
	}

	public void resetKeys() {
		keys.put(Inputs.CLICK, false);
	}

	public void update(float delta) {
		processInput();
	}

	// Events ----------------
	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Inputs.CLICK, true));
		selectedBlock.set(selectedX, selectedY);
	}
	
	// -------------------------

	private boolean processInput() {
		if (keys.get(Inputs.CLICK)) {
			if (editor.isBlockPlacedAt(selectedBlock)) {
				// remove placed block
				editor.unplaceBlock(selectedBlock);
				inventory.addBlock();
			} else if (placeable()) {
				// place block
				editor.placeBlock(new Block(selectedBlock, IBlock.Type.GRASS));
				inventory.removeBlock();
			}
			keys.get(keys.put(Inputs.CLICK, false));
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
