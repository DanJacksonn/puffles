package entities.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import entities.api.IEditor;

/**
 * Represents the current state of the edit screen.
 */
public class Editor implements IEditor {

	Array<Block> placedBlocks;
	
	public Editor() {
		placedBlocks = new Array<Block>();
	}
	
	@Override
	public void placeBlock(Block block) {
		placedBlocks.add(block);
	}
	
	@Override
	public void unplaceBlock(TilePosition tilePosition) {
		for (Block placedBlock : placedBlocks) {
			if (placedBlock.getTilePosition().equals(tilePosition)) {
				placedBlocks.removeValue(placedBlock, true);
				break;
			}
		}
	}
	
	@Override
	public void clearPlacedBlocks() {
		placedBlocks.clear();
	}
	
	public Array<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	
	public boolean isBlockPlacedAt(TilePosition tilePosition) {
		for (Block placedBlock : placedBlocks) {
			if (placedBlock.getTilePosition().equals(tilePosition)) {
				return true;
			}
		}
		return false;
	}

}
