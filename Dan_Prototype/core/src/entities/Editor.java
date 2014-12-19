package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Editor {

	boolean enabled;
	Array<Block> placedBlocks;
	
	public Editor(boolean enabled) {
		this.enabled = enabled;
		placedBlocks = new Array<Block>();
	}
	
	public void placeBlock(Block block) {
		placedBlocks.add(block);
	}
	
	public Array<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isBlockPlacedAt(Vector2 pos) {
		for (Block block : placedBlocks) {
			if (pos.equals(block.getPosition())) return true;
		}
		return false;
	}
	
	public void clearPlacedBlocks() {
		placedBlocks.clear();
	}
}
