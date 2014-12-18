package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Editor {
	
	/**
	 * This class stores the state of edit mode.
	 * -Keeps track of the blocks that have been placed.
	 */
	
	boolean enabled;
	Array<Block> placedBlocks;
	
	public Editor(boolean enabled) {
		this.enabled = enabled;
		this.placedBlocks = new Array<Block>();
	}
	
	public void placeBlock(Block block) {
		placedBlocks.add(block);
	}
	
	public boolean isBlockPlacedAt(Vector2 pos) {
		for (Block block : placedBlocks) {
			if (pos.equals(block.getPosition())) return true;
		}
		return false;
	}
	
	// Getters ------------
	public boolean isEnabled() {
		return enabled;
	}
	
	public Array<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	// ---------------------

}
