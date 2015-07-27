package handlers;

import java.util.HashMap;
import java.util.Map;

import resources.BlockType;
import resources.TilePosition;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import entities.impl.Block;
import entities.impl.Editor;
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

	private World world;

	private TilePosition selectedBlockPosition;

	public EditorHandler(World world, Editor editor) {
		this.world = world;
		this.editor = editor;
		this.selectedBlockPosition = new TilePosition();
		keys = new HashMap<EditorHandler.Inputs, Boolean>();
		resetKeys();
	}

	public void resetKeys() {
		keys.put(Inputs.CLICK, false);
	}

	public void update(float delta) {
		processInput();
	}

	private boolean processInput() {
		if (keys.get(Inputs.CLICK)) {
			if (editor.isBlockPlacedAt(selectedBlockPosition)) {
				// remove placed block
				editor.unplaceBlock(selectedBlockPosition);
				world.addBlockToInventory();
			} else if (placeable()) {
				// place block
				TilePosition positionCopy = new TilePosition(selectedBlockPosition);
				editor.placeBlock(new Block(positionCopy, BlockType.GRASS));
				world.removeBlockFromInventory();
			}
			keys.get(keys.put(Inputs.CLICK, false));
		}
		return false;
	}
	

	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Inputs.CLICK, true));
		selectedBlockPosition.set(selectedX, selectedY);
	}
	
	public void applyEdits() {
		// switch back to game screen
		resetKeys();
		
		// add placed blocks to the world
		world.addBlocksToLevel(editor.getPlacedBlocks());
		editor.clearPlacedBlocks();
	}

	/** True if block can be placed in the currently selected block */
	private boolean placeable() {
		// puffle in the way
		Rectangle selectedBounds = new Rectangle(selectedBlockPosition.x, selectedBlockPosition.y,
				Block.SIZE, Block.SIZE);
		if (Intersector.overlaps(world.puffle.getBounds(), selectedBounds)) {
			return false;
		}
		if (!world.level.isPositionEmpty(selectedBlockPosition)) {
			return false;
		}
		// no blocks in inventory
		if (world.inventory.isEmpty()) {
			return false;
		}
		return true;
	}

}
